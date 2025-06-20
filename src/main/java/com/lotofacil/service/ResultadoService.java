package com.lotofacil.service;

import com.lotofacil.dto.ResultadoManualDTO;
import com.lotofacil.entity.Sorteados;
import com.lotofacil.entity.Todos;
import com.lotofacil.exception.ValidationException;
import com.lotofacil.repository.SorteadosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ResultadoService {

    private static final Logger log = LoggerFactory.getLogger(ResultadoService.class);

    @Autowired
    private SorteadosRepository sorteadosRepository;

    @Autowired
    private ResultadoUpdateService resultadoUpdateService;

    @Transactional
    public Sorteados adicionarResultadoManual(ResultadoManualDTO dto) {
        log.info("Recebendo solicitação para adicionar resultado manual para o concurso {}", dto.getId_sorteados());

        // Validações básicas
        validarResultadoManualDTO(dto);

        // Verifica se o concurso já existe
        if (sorteadosRepository.existsByIdSorteados(dto.getId_sorteados())) {
            throw new ValidationException("Concurso " + dto.getId_sorteados() + " já existe na base de dados.");
        }

        // Chama o serviço para atualizar as tabelas atraso e todos
        Todos todosCorrespondente = resultadoUpdateService.atualizarDadosAposNovoResultado(
                dto.getNumeros(),
                dto.getDataSorteio()
        );

        if (todosCorrespondente == null) {
            // Erro já logado em ResultadoUpdateService
            throw new ValidationException("Não foi possível encontrar a combinação correspondente na tabela \"todos\". Verifique a integridade da tabela.");
        }

        // Cria e salva a nova entidade Sorteados
        Sorteados novoSorteado = new Sorteados();
        novoSorteado.setIdSorteados(Long.valueOf(dto.getId_sorteados()));
        novoSorteado.setSorteio(dto.getDataSorteio()); // Considerar converter formato da data se necessário
        novoSorteado.setIdSorteados(todosCorrespondente.getIdTodos()); // Usa o ID da tabela todos
        
        // Preenche as bolas individuais (b1 a b15)
        List<Integer> numerosOrdenados = dto.getNumeros();
        Collections.sort(numerosOrdenados); // Garante a ordem para salvar nas colunas b1 a b15
        for (int i = 0; i < numerosOrdenados.size(); i++) {
            try {
                Sorteados.class.getMethod("setBola" + (i + 1), Integer.class).invoke(novoSorteado, numerosOrdenados.get(i));
            } catch (Exception e) {
                log.error("Erro ao definir bola" + (i + 1) + " para o concurso {}", dto.getId_sorteados(), e);
                throw new RuntimeException("Erro interno ao processar números sorteados.");
            }
        }
        
        // TODO: Calcular e definir os pontos (se aplicável neste contexto)
        // novoSorteado.setPontos(...);

        Sorteados resultadoSalvo = sorteadosRepository.save(novoSorteado);
        log.info("Resultado do concurso {} adicionado manualmente com sucesso (ID: {}).", dto.getId_sorteados(), resultadoSalvo.getIdSorteados());

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
        // Adicionar outras validações se necessário (formato da data, etc.)
    }
}
