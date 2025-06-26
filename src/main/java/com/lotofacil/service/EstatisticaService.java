package com.lotofacil.service;

import com.lotofacil.dto.EstatisticaDTO;
import com.lotofacil.dto.FrequenciaNumeroDTO;
import com.lotofacil.dto.AtrasoNumeroDTO;
import com.lotofacil.dto.DistribuicaoDTO;
import com.lotofacil.dto.PadraoDTO;
import com.lotofacil.entity.Sorteados;
import com.lotofacil.repository.SorteadosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class EstatisticaService {

    @Autowired
    private SorteadosRepository sorteadosRepository;

    public EstatisticaDTO gerarEstatisticas() {
        List<Sorteados> sorteios = sorteadosRepository.findAllByOrderByIdSorteadosDesc();
        
        if (sorteios.isEmpty()) {
            return new EstatisticaDTO(new ArrayList<>(), new ArrayList<>(), new DistribuicaoDTO(), new ArrayList<>());
        }
        
        List<FrequenciaNumeroDTO> frequencias = calcularFrequencias(sorteios);
        List<AtrasoNumeroDTO> atrasos = calcularAtrasos(sorteios);
        DistribuicaoDTO distribuicao = calcularDistribuicao(sorteios);
        List<PadraoDTO> padroes = identificarPadroes(sorteios);
        
        return new EstatisticaDTO(frequencias, atrasos, distribuicao, padroes);
    }
    
    private List<FrequenciaNumeroDTO> calcularFrequencias(List<Sorteados> sorteios) {
        Map<Integer, Integer> frequenciaMap = new HashMap<>();
        
        // Inicializa o mapa com todos os números de 1 a 25
        for (int i = 1; i <= 25; i++) {
            frequenciaMap.put(i, 0);
        }
        
        // Conta a frequência de cada número
        for (Sorteados sorteio : sorteios) {
            List<Integer> numeros = extrairNumerosSorteio(sorteio);
            for (Integer numero : numeros) {
                frequenciaMap.put(numero, frequenciaMap.get(numero) + 1);
            }
        }
        
        int totalSorteios = sorteios.size();
        
        // Converte o mapa para lista de DTOs
        return frequenciaMap.entrySet().stream()
                .map(entry -> {
                    double percentual = (double) entry.getValue() / totalSorteios * 100;
                    return new FrequenciaNumeroDTO(entry.getKey(), entry.getValue(), Math.round(percentual * 100.0) / 100.0);
                })
                .sorted(Comparator.comparing(FrequenciaNumeroDTO::getFrequencia).reversed())
                .collect(Collectors.toList());
    }
    
    private List<AtrasoNumeroDTO> calcularAtrasos(List<Sorteados> sorteios) {
        Map<Integer, Integer> ultimoSorteioMap = new HashMap<>();
        Map<Integer, String> dataSorteioMap = new HashMap<>();
        
        // Inicializa o mapa com todos os números de 1 a 25
        for (int i = 1; i <= 25; i++) {
            ultimoSorteioMap.put(i, -1);
            dataSorteioMap.put(i, "Nunca sorteado");
        }
        
        // Identifica o último concurso em que cada número foi sorteado
        for (int i = 0; i < sorteios.size(); i++) {
            Sorteados sorteio = sorteios.get(i);
            List<Integer> numeros = extrairNumerosSorteio(sorteio);
            
            for (Integer numero : numeros) {
                if (ultimoSorteioMap.get(numero) == -1) {
                    ultimoSorteioMap.put(numero, Math.toIntExact(sorteio.getIdSorteados()));
                    dataSorteioMap.put(numero, sorteio.getSorteio());
                }
            }
        }
        
        int ultimoConcurso = Math.toIntExact(sorteios.get(0).getIdSorteados());
        
        // Converte o mapa para lista de DTOs
        return IntStream.rangeClosed(1, 25)
                .mapToObj(numero -> {
                    int ultimoSorteio = ultimoSorteioMap.get(numero);
                    int atraso = ultimoSorteio == -1 ? ultimoConcurso : ultimoConcurso - ultimoSorteio;
                    return new AtrasoNumeroDTO(atraso, dataSorteioMap.get(numero));
                })
                .sorted(Comparator.comparing(AtrasoNumeroDTO::getConcursosAtraso).reversed())
                .collect(Collectors.toList());
    }
    
    private DistribuicaoDTO calcularDistribuicao(List<Sorteados> sorteios) {
        // Considerando apenas o último sorteio para simplificar
        Sorteados ultimoSorteio = sorteios.get(0);
        List<Integer> numeros = extrairNumerosSorteio(ultimoSorteio);
        
        int pares = 0;
        int primos = 0;
        int soma = 0;
        
        // Números primos até 25
        Set<Integer> numerosPrimos = new HashSet<>(Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19, 23));
        
        // Mapa para contar números por linha (5 linhas de 5 números)
        Map<Integer, Integer> linhas = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            linhas.put(i, 0);
        }
        
        // Mapa para contar números por coluna (5 colunas de 5 números)
        Map<Integer, Integer> colunas = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            colunas.put(i, 0);
        }
        
        for (Integer numero : numeros) {
            // Verifica se é par
            if (numero % 2 == 0) {
                pares++;
            }
            
            // Verifica se é primo
            if (numerosPrimos.contains(numero)) {
                primos++;
            }
            
            // Soma
            soma += numero;
            
            // Calcula linha e coluna
            int linha = (numero - 1) / 5 + 1;
            int coluna = (numero - 1) % 5 + 1;
            
            linhas.put(linha, linhas.get(linha) + 1);
            colunas.put(coluna, colunas.get(coluna) + 1);
        }
        
        // Formata distribuição de linhas e colunas
        String distribuicaoLinhas = linhas.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> "L" + e.getKey() + ":" + e.getValue())
                .collect(Collectors.joining(", "));
        
        String distribuicaoColunas = colunas.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> "C" + e.getKey() + ":" + e.getValue())
                .collect(Collectors.joining(", "));
        
        return new DistribuicaoDTO(
                pares, 
                15 - pares, // ímpares
                primos, 
                15 - primos, // não primos
                soma,
                distribuicaoLinhas,
                distribuicaoColunas
        );
    }
    
    private List<PadraoDTO> identificarPadroes(List<Sorteados> sorteios) {
        List<PadraoDTO> padroes = new ArrayList<>();
        
        // Análise de sequências
        Map<String, Integer> sequenciasMap = new HashMap<>();
        
        for (Sorteados sorteio : sorteios) {
            List<Integer> numeros = extrairNumerosSorteio(sorteio);
            Collections.sort(numeros);
            
            // Identifica sequências de números consecutivos
            int sequenciasCount = 0;
            for (int i = 0; i < numeros.size() - 1; i++) {
                if (numeros.get(i + 1) - numeros.get(i) == 1) {
                    sequenciasCount++;
                }
            }
            
            String chave = "Sequências: " + sequenciasCount;
            sequenciasMap.put(chave, sequenciasMap.getOrDefault(chave, 0) + 1);
        }
        
        int totalSorteios = sorteios.size();
        
        // Converte o mapa de sequências para DTOs
        sequenciasMap.forEach((chave, valor) -> {
            double percentual = (double) valor / totalSorteios * 100;
            padroes.add(new PadraoDTO("Sequência", chave, valor, Math.round(percentual * 100.0) / 100.0));
        });
        
        // Análise de distribuição par/ímpar
        Map<String, Integer> parImparMap = new HashMap<>();
        
        for (Sorteados sorteio : sorteios) {
            List<Integer> numeros = extrairNumerosSorteio(sorteio);
            
            int pares = 0;
            for (Integer numero : numeros) {
                if (numero % 2 == 0) {
                    pares++;
                }
            }
            
            String chave = pares + " pares, " + (15 - pares) + " ímpares";
            parImparMap.put(chave, parImparMap.getOrDefault(chave, 0) + 1);
        }
        
        // Converte o mapa de par/ímpar para DTOs
        parImparMap.forEach((chave, valor) -> {
            double percentual = (double) valor / totalSorteios * 100;
            padroes.add(new PadraoDTO("Par/Ímpar", chave, valor, Math.round(percentual * 100.0) / 100.0));
        });
        
        // Ordena os padrões por frequência
        padroes.sort(Comparator.comparing(PadraoDTO::getFrequencia).reversed());
        
        return padroes;
    }
    
    private List<Integer> extrairNumerosSorteio(Sorteados sorteio) {
        List<Integer> numeros = new ArrayList<>();
        numeros.add(sorteio.getBola1());
        numeros.add(sorteio.getBola2());
        numeros.add(sorteio.getBola3());
        numeros.add(sorteio.getBola4());
        numeros.add(sorteio.getBola5());
        numeros.add(sorteio.getBola6());
        numeros.add(sorteio.getBola7());
        numeros.add(sorteio.getBola8());
        numeros.add(sorteio.getBola9());
        numeros.add(sorteio.getBola10());
        numeros.add(sorteio.getBola11());
        numeros.add(sorteio.getBola12());
        numeros.add(sorteio.getBola13());
        numeros.add(sorteio.getBola14());
        numeros.add(sorteio.getBola15());
        return numeros;
    }
}
