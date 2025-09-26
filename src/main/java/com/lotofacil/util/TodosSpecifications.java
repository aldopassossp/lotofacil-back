package com.lotofacil.util;

import com.lotofacil.dto.sugestao.FiltroSugestaoDTO;
import com.lotofacil.entity.Todos;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Path;
import java.util.ArrayList;
import java.util.List;

public class TodosSpecifications {

    public static Specification<Todos> comFiltros(FiltroSugestaoDTO filtros) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // ------- SOMA -------
            if (filtros.getSomaMinima() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("soma"), filtros.getSomaMinima()));
            }
            if (filtros.getSomaMaxima() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("soma"), filtros.getSomaMaxima()));
            }

            // ------- PARES -------
            if (filtros.getParesMinimo() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("pares"), filtros.getParesMinimo()));
            }
            if (filtros.getParesMaximo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("pares"), filtros.getParesMaximo()));
            }

            // ------- SEQUÊNCIAS (2 e 3) -------
            if (filtros.getSeqDoisMinimo() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("seq_dois"), filtros.getSeqDoisMinimo()));
            }
            if (filtros.getSeqDoisMaximo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("seq_dois"), filtros.getSeqDoisMaximo()));
            }

            if (filtros.getSeqTresMinimo() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("seq_tres"), filtros.getSeqTresMinimo()));
            }
            if (filtros.getSeqTresMaximo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("seq_tres"), filtros.getSeqTresMaximo()));
            }

            // ------- PONTOS -------
            if (filtros.getPontosMinimo() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("pontos"), filtros.getPontosMinimo()));
            }
            if (filtros.getPontosMaximo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("pontos"), filtros.getPontosMaximo()));
            }

            // ------- LINHAS (NOT IN) -------
            if (filtros.getLinhasSelecionadas() != null && !filtros.getLinhasSelecionadas().isEmpty()) {
                predicates.add(cb.not(root.get("linha").in(filtros.getLinhasSelecionadas())));
            }

            // ------- COLUNAS (NOT IN) -------
            if (filtros.getColunasSelecionadas() != null && !filtros.getColunasSelecionadas().isEmpty()) {
                predicates.add(cb.not(root.get("coluna").in(filtros.getColunasSelecionadas())));
            }

            // ------- SORTEADO (tratamento dinâmico) -------
            if (filtros.getJaFoiSorteado() != null) {
                Path<?> sorteadoPath = root.get("sorteado");
                Boolean js = filtros.getJaFoiSorteado();
                // detecta tipo do path na entidade e compara adequadamente
                if (sorteadoPath.getJavaType().equals(Boolean.class) || sorteadoPath.getJavaType().equals(boolean.class)) {
                    predicates.add(cb.equal(sorteadoPath.as(Boolean.class), js));
                } else {
                    // supõe 0/1 armazenado como Integer/TINYINT
                    int valor = js ? 1 : 0;
                    predicates.add(cb.equal(sorteadoPath.as(Integer.class), valor));
                }
            }

            // ------- NÚMEROS OBRIGATÓRIOS (sequencia) -------
            if (filtros.getNumerosObrigatorios() != null && !filtros.getNumerosObrigatorios().isEmpty()) {
                for (Integer numero : filtros.getNumerosObrigatorios()) {
                    String numFormatado = String.format("%02d", numero);
                    // use "-XX-" se sua coluna sequencia tiver hífen inicial/final garantido
                    predicates.add(cb.like(root.get("sequencia"), "%-" + numFormatado + "-%"));
                }
            }

            // ------- NÚMEROS PROIBIDOS (sequencia) -------
            if (filtros.getNumerosProibidos() != null && !filtros.getNumerosProibidos().isEmpty()) {
                for (Integer numero : filtros.getNumerosProibidos()) {
                    String numFormatado = String.format("%02d", numero);
                    predicates.add(cb.notLike(root.get("sequencia"), "%-" + numFormatado + "-%"));
                }
            }

            // Se quiser evitar usar "-XX-" e não garantir hífen nos dados, troque "%-XX-%" por "%XX%" (menos preciso).

            // Retorna o AND de todos os predicados
            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
