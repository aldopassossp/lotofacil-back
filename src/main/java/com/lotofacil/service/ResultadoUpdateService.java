package com.lotofacil.service;

import com.lotofacil.entity.Atraso;
import com.lotofacil.entity.Todos;
import com.lotofacil.repository.AtrasoRepository;
import com.lotofacil.repository.TodosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ResultadoUpdateService {

    private static final Logger log = LoggerFactory.getLogger(ResultadoUpdateService.class);
    private static final int BATCH_SIZE = 10000; // Process records in batches for performance

    @Autowired
    private AtrasoRepository atrasoRepository;

    @Autowired
    private TodosRepository todosRepository;

    /**
     * Atualiza as tabelas 'atraso' e 'todos' (campo 'sorteado') de forma síncrona
     * e dispara a atualização assíncrona da coluna 'pontos' na tabela 'todos'.
     *
     * @param numerosSorteados Lista dos 15 números sorteados.
     * @param dataSorteio Data do sorteio.
     * @return O objeto Todos correspondente à combinação sorteada, ou null se não encontrado.
     */
    @Transactional
    public Todos atualizarDadosAposNovoResultado(List<Integer> numerosSorteados, String dataSorteio) {
        log.info("Atualizando dados síncronos para o sorteio de {} com números: {}", dataSorteio, numerosSorteados);

        // 1. Atualizar Tabela Atraso (Síncrono)
        atualizarTabelaAtraso(numerosSorteados, dataSorteio);

        // 2. Atualizar Tabela Todos (campo 'sorteado') (Síncrono)
        Todos todosCorrespondente = atualizarCampoTodosSorteado(numerosSorteados);

        // 3. Disparar atualização assíncrona da coluna 'pontos' na tabela 'todos'
        if (todosCorrespondente != null) {
            atualizarPontosAsync(numerosSorteados);
        } else {
             log.error("Não foi possível encontrar a combinação correspondente em 'todos' para o sorteio {}. A atualização de pontos não será disparada.", dataSorteio);
        }

        return todosCorrespondente;
    }

    // Método síncrono para atualizar a tabela de atraso
    private void atualizarTabelaAtraso(List<Integer> numerosSorteados, String dataSorteio) {
        // (Lógica existente para atualizar atraso - sem alterações)
        List<Atraso> atrasosParaAtualizar = atrasoRepository.findAll();
        if (atrasosParaAtualizar.size() != 25) {
            log.warn("Tabela 'atraso' incompleta ou vazia. Inicializando...");
            for (int numero = 1; numero <= 25; numero++) {
                final int num = numero;
                if (atrasosParaAtualizar.stream().noneMatch(a -> a.getNumero().equals(num))) {
                    Atraso novoAtraso = new Atraso();
                    novoAtraso.setNumero(num);
                    novoAtraso.setContagem(0);
                    atrasosParaAtualizar.add(novoAtraso);
                }
            }
        }
        for (Atraso atraso : atrasosParaAtualizar) {
            int numeroAtual = atraso.getNumero();
            if (numerosSorteados.contains(numeroAtual)) {
                atraso.setContagem(0);
                atraso.setUltimo(dataSorteio);
            } else {
                atraso.setContagem(atraso.getContagem() + 1);
            }
        }
        atrasoRepository.saveAll(atrasosParaAtualizar);
        log.info("Tabela 'atraso' atualizada.");
    }

    // Método síncrono para encontrar e atualizar o campo 'sorteado' na tabela 'todos'
    private Todos atualizarCampoTodosSorteado(List<Integer> numerosSorteados) {
        Collections.sort(numerosSorteados);
        String sequenciaFormatada = formatarSequencia(numerosSorteados);
        log.debug("Buscando combinação na tabela 'todos' com sequência: {}", sequenciaFormatada);

        Optional<Todos> todosOptional = todosRepository.findBySequencia(sequenciaFormatada);

        if (todosOptional.isPresent()) {
            Todos todos = todosOptional.get();
            todos.setSorteado(1); // Marca como sorteado
            todosRepository.save(todos); // Salva a atualização do campo 'sorteado'
            log.info("Campo 'sorteado' atualizado na tabela 'todos' (ID: {}).", todos.getIdTodos());
            return todos;
        } else {
            log.error("Combinação com sequência {} não encontrada na tabela 'todos'! Verifique a integridade da tabela.", sequenciaFormatada);
            return null;
        }
    }

    /**
     * Método assíncrono para atualizar a coluna 'pontos' em toda a tabela 'todos'.
     * Compara os números sorteados com cada combinação possível.
     * Executa em uma transação separada para não interferir na principal.
     *
     * @param numerosSorteados Lista dos 15 números do último sorteio.
     */
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW) // Executa em nova transação
    public void atualizarPontosAsync(List<Integer> numerosSorteados) {
        log.info("Iniciando atualização assíncrona da coluna 'pontos' na tabela 'todos' para os números: {}", numerosSorteados);
        long startTime = System.currentTimeMillis();
        Set<Integer> numerosSorteadosSet = new HashSet<>(numerosSorteados);
        long totalAtualizado = 0;
        int pageNumber = 0;

        try {
            Page<Todos> paginaTodos;
            do {
                Pageable pageable = PageRequest.of(pageNumber, BATCH_SIZE);
                paginaTodos = todosRepository.findAll(pageable);
                List<Todos> batchParaSalvar = new ArrayList<>();

                log.debug("Processando página {} ({} registros) para atualização de pontos...", pageNumber, paginaTodos.getNumberOfElements());

                for (Todos todos : paginaTodos.getContent()) {
                    List<Integer> numerosCombinacao = parseSequencia(todos.getSequencia());
                    int pontos = calcularPontos(numerosSorteadosSet, numerosCombinacao);
                    if (todos.getPontos() == null || todos.getPontos() != pontos) {
                         todos.setPontos(pontos);
                         batchParaSalvar.add(todos);
                    }
                }
                
                if (!batchParaSalvar.isEmpty()) {
                    todosRepository.saveAll(batchParaSalvar); // Salva o lote atualizado
                    totalAtualizado += batchParaSalvar.size();
                    log.debug("Lote de {} registros atualizado.", batchParaSalvar.size());
                }

                pageNumber++;
            } while (paginaTodos.hasNext());

            long endTime = System.currentTimeMillis();
            log.info("Atualização assíncrona da coluna 'pontos' concluída. {} registros atualizados em {} ms.", totalAtualizado, (endTime - startTime));

        } catch (Exception e) {
            log.error("Erro durante a atualização assíncrona da coluna 'pontos': {}", e.getMessage(), e);
            // Considerar mecanismos de retentativa ou notificação de falha
        }
    }

    // Helper para formatar a sequência
    private String formatarSequencia(List<Integer> numeros) {
         // Garante ordenação e formatação com dois dígitos
        return numeros.stream()
                      .sorted()
                      .map(n -> String.format("%02d", n))
                      .collect(Collectors.joining("-"));
    }

    // Helper para converter a string 'sequencia' de volta para lista de inteiros
    private List<Integer> parseSequencia(String sequencia) {
        if (sequencia == null || sequencia.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            return Arrays.stream(sequencia.split("-"))
                         .map(Integer::parseInt)
                         .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            log.warn("Erro ao parsear sequência: {}", sequencia);
            return Collections.emptyList();
        }
    }

    // Helper para calcular os pontos (números em comum)
    private int calcularPontos(Set<Integer> sorteados, List<Integer> combinacao) {
        int pontos = 0;
        for (Integer numero : combinacao) {
            if (sorteados.contains(numero)) {
                pontos++;
            }
        }
        return pontos;
    }
}
