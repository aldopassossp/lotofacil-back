package com.lotofacil.util;

import com.lotofacil.dto.sugestao.FiltroSugestaoDTO;
import com.lotofacil.entity.Todos;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.List;

public class TodosSpecifications {

    public static Specification<Todos> comFiltros(FiltroSugestaoDTO filtros) {
        return (root, query, cb) -> {
            Specification<Todos> spec = Specification.where(null);

            // -------- SOMA --------
            if (filtros.getSomaMinima() != null) {
                spec = spec.and((r, q, c) -> c.greaterThanOrEqualTo(r.get("soma"), filtros.getSomaMinima()));
            }
            if (filtros.getSomaMaxima() != null) {
                spec = spec.and((r, q, c) -> c.lessThanOrEqualTo(r.get("soma"), filtros.getSomaMaxima()));
            }

            // -------- PARES --------
            if (filtros.getParesMinimo() != null) {
                spec = spec.and((r, q, c) -> c.greaterThanOrEqualTo(r.get("pares"), filtros.getParesMinimo()));
            }
            if (filtros.getParesMaximo() != null) {
                spec = spec.and((r, q, c) -> c.lessThanOrEqualTo(r.get("pares"), filtros.getParesMaximo()));
            }

            // -------- SEQUÊNCIAS --------
            // Seq 2
            if (filtros.getSeqDoisMinimo() != null) {
                spec = spec.and((r, q, c) -> c.greaterThanOrEqualTo(r.get("seq_dois"), filtros.getSeqDoisMinimo()));
            }
            if (filtros.getSeqDoisMaximo() != null) {
                spec = spec.and((r, q, c) -> c.lessThanOrEqualTo(r.get("seq_dois"), filtros.getSeqDoisMaximo()));
            }

            // Seq 3
            if (filtros.getSeqTresMinimo() != null) {
                spec = spec.and((r, q, c) -> c.greaterThanOrEqualTo(r.get("seq_tres"), filtros.getSeqTresMinimo()));
            }
            if (filtros.getSeqTresMaximo() != null) {
                spec = spec.and((r, q, c) -> c.lessThanOrEqualTo(r.get("seq_tres"), filtros.getSeqTresMaximo()));
            }

            // -------- PONTOS --------
            if (filtros.getPontosMinimo() != null) {
                spec = spec.and((r, q, c) -> c.greaterThanOrEqualTo(r.get("pontos"), filtros.getPontosMinimo()));
            }
            if (filtros.getPontosMaximo() != null) {
                spec = spec.and((r, q, c) -> c.lessThanOrEqualTo(r.get("pontos"), filtros.getPontosMaximo()));
            }

            // -------- LINHAS -------- (NOT IN → excluir selecionadas)
            if (filtros.getLinhasSelecionadas() != null && !filtros.getLinhasSelecionadas().isEmpty()) {
                spec = spec.and((r, q, c) -> c.not(r.get("linha").in(filtros.getLinhasSelecionadas())));
            }

            // -------- COLUNAS -------- (NOT IN → excluir selecionadas)
            if (filtros.getColunasSelecionadas() != null && !filtros.getColunasSelecionadas().isEmpty()) {
                spec = spec.and((r, q, c) -> c.not(r.get("coluna").in(filtros.getColunasSelecionadas())));
            }

            // -------- SORTEADO --------
            if (filtros.getJaFoiSorteado() != null) {
                int valor = filtros.getJaFoiSorteado() ? 1 : 0;
                spec = spec.and((r, q, c) -> cb.equal(root.get("sorteado"), valor));
            }

            // -------- NÚMEROS OBRIGATÓRIOS --------
            if (filtros.getNumerosObrigatorios() != null && !filtros.getNumerosObrigatorios().isEmpty()) {
                for (Integer numero : filtros.getNumerosObrigatorios()) {
                    String numFormatado = String.format("%02d", numero); // garante 2 dígitos
                    spec = spec.and((r, q, c) -> c.like(r.get("sequencia"), "%" + numFormatado + "%"));
                }
            }

            // -------- NÚMEROS PROIBIDOS --------
            if (filtros.getNumerosProibidos() != null && !filtros.getNumerosProibidos().isEmpty()) {
                for (Integer numero : filtros.getNumerosProibidos()) {
                    String numFormatado = String.format("%02d", numero); // garante 2 dígitos
                    spec = spec.and((r, q, c) -> c.notLike(r.get("sequencia"), "%" + numFormatado + "%"));
                }
            }

            return spec.toPredicate(root, query, cb);
        };
    }
}

