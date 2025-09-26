package com.lotofacil.service;

import com.lotofacil.dto.request.GerarCombinacoesRequest;
import com.lotofacil.entity.Todos;
import com.lotofacil.repository.TodosRepository;
import com.lotofacil.util.TodosSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class GeradorCombinacoesService {

    private final TodosRepository todosRepository;

    public List<List<Integer>> gerarCombinacoes(GerarCombinacoesRequest request) {
        List<Todos> candidatos = todosRepository.findAll(
                TodosSpecifications.comFiltros(request.getFiltros())
        );

        Random random = new Random();
        List<List<Integer>> resultado = new ArrayList<>();

        for (int i = 0; i < request.getQuantidade(); i++) {
            // Sorteia um candidato qualquer
            Todos sorteado = candidatos.get(random.nextInt(candidatos.size()));

            // Extrai a sequência
            List<Integer> numeros = Arrays.stream(sorteado.getSequencia().split("-"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            // Se tamanho > 15, sorteia mais alguns números que não estão na sequência
            if (request.getTamanho() > 15) {
                List<Integer> extras = IntStream.rangeClosed(1, 25)
                        .boxed()
                        .filter(n -> !numeros.contains(n))
                        .collect(Collectors.toList());
                Collections.shuffle(extras);

                numeros.addAll(extras.subList(0, request.getTamanho() - 15));
                Collections.sort(numeros);
            }

            resultado.add(numeros);
        }

        return resultado;
    }
}

