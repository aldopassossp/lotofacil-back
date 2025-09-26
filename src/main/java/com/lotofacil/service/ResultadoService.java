package com.lotofacil.service;

import com.lotofacil.dto.ResultadoManualDTO;
import com.lotofacil.entity.HistoricoSugestao;
import com.lotofacil.entity.Sorteados;
import com.lotofacil.entity.Todos;
import com.lotofacil.exception.ValidationException;
import com.lotofacil.repository.HistoricoSugestaoRepository;
import com.lotofacil.repository.SorteadosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ResultadoService {

    private static final Logger log = LoggerFactory.getLogger(ResultadoService.class);

    @Autowired
    private SorteadosRepository sorteadosRepository;

    @Autowired
    private ResultadoUpdateService resultadoUpdateService;

    @Autowired
    private HistoricoSugestaoRepository historicoSugestaoRepository;

    @Transactional
    public Sorteados adicionarResultadoManual(ResultadoManualDTO dto) {
        log.info("Recebendo solicitação para adicionar resultado manual para o concurso {}", dto.getId_sorteados());

        // Validações básicas
        validarResultadoManualDTO(dto);

        // Verifica se o concurso já existe
        if (sorteadosRepository.existsByIdSorteados(Long.valueOf(dto.getId_sorteados()))) {
            throw new ValidationException("Concurso " + dto.getId_sorteados() + " já existe na base de dados.");
        }

        // Chama o serviço para atualizar as tabelas atraso e todos
        Todos todosCorrespondente = resultadoUpdateService.atualizarDadosAposNovoResultado(
                dto.getNumeros(),
                dto.getDataSorteio()
        );

        if (todosCorrespondente == null) {
            throw new ValidationException("Não foi possível encontrar a combinação correspondente na tabela \"todos\". Verifique a integridade da tabela.");
        }

        // Cria e salva a nova entidade Sorteados
        Sorteados novoSorteado = new Sorteados();
        novoSorteado.setIdSorteados(Long.valueOf(dto.getId_sorteados()));
        novoSorteado.setSorteio(dto.getDataSorteio());
        novoSorteado.setTodos(todosCorrespondente);

        // Preenche as bolas individuais (b1 a b15)
        List<Integer> numerosOrdenados = new ArrayList<>(dto.getNumeros());
        Collections.sort(numerosOrdenados);
        for (int i = 0; i < numerosOrdenados.size(); i++) {
            try {
                Sorteados.class.getMethod("setBola" + (i + 1), Integer.class)
                        .invoke(novoSorteado, numerosOrdenados.get(i));
            } catch (Exception e) {
                log.error("Erro ao definir bola" + (i + 1) + " para o concurso {}", dto.getId_sorteados(), e);
                throw new RuntimeException("Erro interno ao processar números sorteados.");
            }
        }

        // Inicializa pontos sempre em 0
        int pontos = 0;

        // Busca o concurso anterior para calcular pontos
        Sorteados concursoAnterior = sorteadosRepository.findConcursoAnterior(Long.valueOf(dto.getId_sorteados()));

        if (concursoAnterior != null) {
            Set<Integer> numerosAnteriores = new HashSet<>();
            numerosAnteriores.add(concursoAnterior.getBola1());
            numerosAnteriores.add(concursoAnterior.getBola2());
            numerosAnteriores.add(concursoAnterior.getBola3());
            numerosAnteriores.add(concursoAnterior.getBola4());
            numerosAnteriores.add(concursoAnterior.getBola5());
            numerosAnteriores.add(concursoAnterior.getBola6());
            numerosAnteriores.add(concursoAnterior.getBola7());
            numerosAnteriores.add(concursoAnterior.getBola8());
            numerosAnteriores.add(concursoAnterior.getBola9());
            numerosAnteriores.add(concursoAnterior.getBola10());
            numerosAnteriores.add(concursoAnterior.getBola11());
            numerosAnteriores.add(concursoAnterior.getBola12());
            numerosAnteriores.add(concursoAnterior.getBola13());
            numerosAnteriores.add(concursoAnterior.getBola14());
            numerosAnteriores.add(concursoAnterior.getBola15());


            for (Integer numero : dto.getNumeros()) {
                if (numerosAnteriores.contains(numero)) {
                    pontos++;
                }
            }

        } else {
            log.warn("Não encontrado concurso anterior para calcular pontos do concurso {}", dto.getId_sorteados());
            novoSorteado.setPontos(0);
        }

        novoSorteado.setPontos(pontos);

        log.info("Concurso {} calculado com {} pontos", dto.getId_sorteados(), pontos);

        Sorteados resultadoSalvo = sorteadosRepository.save(novoSorteado);
        log.info("Resultado do concurso {} adicionado manualmente com sucesso (ID: {}).", dto.getId_sorteados(), resultadoSalvo.getIdSorteados());

        // 🔹 Atualizar acertos no histórico
        atualizarAcertosComNovoResultado(numerosOrdenados);

        return resultadoSalvo;
    }

    private void validarResultadoManualDTO(ResultadoManualDTO dto) {
        if (dto.getNumeros() == null || dto.getNumeros().size() != 15) {
            throw new ValidationException("Devem ser fornecidos exatamente 15 números.");
        }
        Set<Integer> numerosUnicos = new HashSet<>();
        for (Integer numero : dto.getNumeros()) {
            if (numero == null || numero < 1 || numero > 25) {
                throw new ValidationException("Todos os números devem estar entre 1 e 25.");
            }
            if (!numerosUnicos.add(numero)) {
                throw new ValidationException("Os números fornecidos devem ser únicos.");
            }
        }
    }

    // 🔹 Novo método: atualizar acertos nas sugestões
    private void atualizarAcertosComNovoResultado(List<Integer> numerosResultado) {
        String resultadoFormatado = numerosResultado.stream()
                .sorted()
                .map(n -> String.format("%1d", n))
                .collect(Collectors.joining("-"));

        List<HistoricoSugestao> historicos = historicoSugestaoRepository.findAll();

        for (HistoricoSugestao h : historicos) {
            int acertos = calcularAcertos(h.getNumeros(), resultadoFormatado);
            h.setAcertos(acertos);
        }

        historicoSugestaoRepository.saveAll(historicos);
        log.info("Histórico atualizado com os acertos para o resultado {}", resultadoFormatado);
    }

    private int calcularAcertos(String numerosSugestao, String numerosResultado) {
        System.out.println(" Numeros Sugestão  = " + numerosSugestao);
        System.out.println(" Numeros Resultado = " + numerosResultado);
        Set<String> sugestao = new HashSet<>(Arrays.asList(numerosSugestao.split("-")));
        System.out.println(" Numeros Resultado = " + sugestao);
        Set<String> resultado = new HashSet<>(Arrays.asList(numerosResultado.split("-")));
        System.out.println(" Numeros Resultado = " + resultado);
        sugestao.retainAll(resultado); // mantém apenas os coincidentes
        return sugestao.size();
    }
}
