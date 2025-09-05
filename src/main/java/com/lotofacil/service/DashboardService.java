package com.lotofacil.service;

import com.lotofacil.dto.AtrasoNumeroDTO;
import com.lotofacil.dto.dashboard.ConcursoParesDTO;
import com.lotofacil.dto.dashboard.ConcursoSomaDTO;
import com.lotofacil.dto.dashboard.ContagemLinhaDTO;
import com.lotofacil.dto.dashboard.ValorContagemDTO;
import com.lotofacil.entity.Sorteados;
import com.lotofacil.repository.AtrasoRepository;
import com.lotofacil.repository.SorteadosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private static final Logger log = LoggerFactory.getLogger(DashboardService.class);

    @Autowired
    private SorteadosRepository sorteadosRepository;

    @Autowired
    private AtrasoRepository atrasoRepository;

    // Método para buscar a soma dos últimos N resultados
    public List<ConcursoSomaDTO> getSomaUltimosNResultados(int n) {
        log.debug("Buscando soma dos últimos {} resultados.", n);
        Pageable pageable = PageRequest.of(0, n, Sort.by(Sort.Direction.DESC, "idSorteados"));
        // A query para buscar a soma precisará de um JOIN com a tabela Todos
        // Usando projeção JPA para retornar diretamente o DTO
        return sorteadosRepository.findUltimosNComSoma(pageable);
    }

    // Método para buscar a contagem de pares dos últimos N resultados
    public List<ConcursoParesDTO> getParesUltimosNResultados(int n) {
        log.debug("Buscando contagem de pares dos últimos {} resultados.", n);
        Pageable pageable = PageRequest.of(0, n, Sort.by(Sort.Direction.DESC, "idSorteados"));
        // Similar à soma, precisará de JOIN e projeção
        return sorteadosRepository.findUltimosNComPares(pageable);
    }

    public List<ValorContagemDTO> getFrequenciaNumerosTodos() {
        var dados = sorteadosRepository.getFrequenciaNumerosTodos();
        return dados.stream()
                .map(o -> new ValorContagemDTO(o[0].toString(), ((Number) o[1]).longValue()))
                .collect(Collectors.toList());
    }

    // Método para calcular a frequência dos números nos últimos N resultados
    public List<ValorContagemDTO> getFrequenciaNumerosUltimosN(int n) {
        var dados = sorteadosRepository.getFrequenciaNumerosUltimosN(n);
        return dados.stream()
                .map(o -> new ValorContagemDTO(o[0].toString(), ((Number) o[1]).longValue()))
                .collect(Collectors.toList());
    }

    // Método para calcular a contagem de sequências (seq_dois, seq_tres, etc.) nos últimos N
    public List<ValorContagemDTO> getContagemSequenciaDoisUltimosN(int n) {
        log.debug("Calculando contagem de sequencia de dois nos últimos {} resultados.", n);
        Pageable pageable = PageRequest.of(0, n);
        // Query dinâmica ou separada por tipo de sequência, com JOIN e agregação
        List<Object[]> resultados = sorteadosRepository.findContagemSeqDoisUltimosN(n);
        return resultados.stream()
                .map(r -> new ValorContagemDTO(String.valueOf(r[0]), ((Number) r[1]).longValue()))
                .collect(Collectors.toList());
    }

    // Método para calcular a contagem de sequências (seq_dois, seq_tres, etc.) nos últimos N
    public List<ValorContagemDTO> getContagemSequenciaTresUltimosN(int n) {
        log.debug("Calculando contagem de sequencia de três nos últimos {} resultados.", n);
        Pageable pageable = PageRequest.of(0, n);
        // Query dinâmica ou separada por tipo de sequência, com JOIN e agregação
        List<Object[]> resultados = sorteadosRepository.findContagemSeqTresUltimosN(n);
        return resultados.stream()
                .map(r -> new ValorContagemDTO(String.valueOf(r[0]), ((Number) r[1]).longValue()))
                .collect(Collectors.toList());
    }

    // Método para calcular a contagem de sequências (seq_dois, seq_tres, etc.) nos últimos N
    public List<ValorContagemDTO> getContagemSequenciaQuatroUltimosN(int n) {
        log.debug("Calculando contagem de sequencia de quatro nos últimos {} resultados.", n);
        Pageable pageable = PageRequest.of(0, n);
        // Query dinâmica ou separada por tipo de sequência, com JOIN e agregação
        List<Object[]> resultados = sorteadosRepository.findContagemSeqQuatroUltimosN(n);
        return resultados.stream()
                .map(r -> new ValorContagemDTO(String.valueOf(r[0]), ((Number) r[1]).longValue()))
                .collect(Collectors.toList());
    }

//    // Método para calcular as ocorrências de linha/coluna nos últimos N
//    public List<ValorContagemDTO> getOcorrenciaLinhaColunaUltimosN(String tipo, int n) {
//        log.debug("Calculando ocorrências de {} nos últimos {} resultados.", tipo, n);
//        Pageable pageable = PageRequest.of(0, n);
//        // Query dinâmica ou separada por tipo (linha/coluna), com JOIN e agregação
//        List<Object[]> resultados = sorteadosRepository.findContagemSeqQuatroUltimosN(pageable);
//        return resultados.stream()
//                .map(r -> new ValorContagemDTO(String.valueOf(r[0]), ((Number) r[1]).longValue()))
//                .collect(Collectors.toList());
//    }

    // Nota: Os métodos no SorteadosRepository (findUltimosNComSoma, findUltimosNComImpares, etc.)
    // precisam ser implementados com as queries JPQL ou nativas apropriadas,
    // incluindo os JOINs necessários com a entidade Todos.

    public List<ContagemLinhaDTO> ocorrenciaColunaUltimosN(int n) {
        var dados = sorteadosRepository.findOcorrenciaColunaUltimosN(n);
        return dados.stream()
                .map(o -> new ContagemLinhaDTO(o[0].toString(), ((Number) o[1]).longValue()))
                .collect(Collectors.toList());
    }

    public List<ContagemLinhaDTO> ocorrenciaTodasColunas() {
        var dados = sorteadosRepository.findOcorrenciaTodasColunas();
        return dados.stream()
                .map(o -> new ContagemLinhaDTO(o[0].toString(), ((Number) o[1]).longValue()))
                .collect(Collectors.toList());
    }

    public List<ContagemLinhaDTO> ocorrenciaLinhaUltimosN(int n) {
        var dados = sorteadosRepository.findOcorrenciaLinhaUltimosN(n);
        return dados.stream()
                .map(o -> new ContagemLinhaDTO(o[0].toString(), ((Number) o[1]).longValue()))
                .collect(Collectors.toList());
    }

    public List<ContagemLinhaDTO> ocorrenciaTodasLinhas() {
        var dados = sorteadosRepository.findOcorrenciaTodasLinhas();
        return dados.stream()
                .map(o -> new ContagemLinhaDTO(o[0].toString(), ((Number) o[1]).longValue()))
                .collect(Collectors.toList());
    }

    public List<AtrasoNumeroDTO> getAtrasos() {
        var dados = atrasoRepository.findAtrasos();
        return dados.stream()
                .map(o -> new AtrasoNumeroDTO(
                        ((Number) o[0]).intValue(),   // id_atraso
                        ((Number) o[1]).intValue(),   // contagem
                        o[2].toString()               // ultimo
                ))
                .collect(Collectors.toList());
    }
}


