package com.lotofacil.service;

import com.lotofacil.dto.sugestao.FiltroSugestaoDTO;
import com.lotofacil.entity.Todos;
import com.lotofacil.repository.TodosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SugestaoPersonalizadaService {

    private static final Logger log = LoggerFactory.getLogger(SugestaoPersonalizadaService.class);

    @Autowired
    private TodosRepository todosRepository;

    public Page<Todos> buscarSugestoes(FiltroSugestaoDTO filtros) {
        log.info("Buscando sugestões personalizadas com filtros: {}", filtros);

        // Cria a especificação JPA dinamicamente com base nos filtros
        Specification<Todos> spec = criarEspecificacao(filtros);

        // Configura paginação (padrão: página 0, tamanho 50)
        Pageable pageable = PageRequest.of(0, 50);

        // Executa a busca
        return todosRepository.findAll(spec, pageable);
    }

    private Specification<Todos> criarEspecificacao(FiltroSugestaoDTO filtros) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtros de soma
            if (filtros.getSomaMinima() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("soma"), filtros.getSomaMinima()));
            }
            if (filtros.getSomaMaxima() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("soma"), filtros.getSomaMaxima()));
            }

            // Filtros de pares
            if (filtros.getParesMinimo() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("pares"), filtros.getParesMinimo()));
            }
            if (filtros.getParesMaximo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("pares"), filtros.getParesMaximo()));
            }

            // Filtros de ímpares
            if (filtros.getImparesMinimo() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("impares"), filtros.getImparesMinimo()));
            }
            if (filtros.getImparesMaximo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("impares"), filtros.getImparesMaximo()));
            }

            // Filtros de sequências
            if (filtros.getSeqDoisMinimo() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("seq_dois"), filtros.getSeqDoisMinimo()));
            }
            if (filtros.getSeqDoisMaximo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("seq_dois"), filtros.getSeqDoisMaximo()));
            }

            if (filtros.getSeqTresMinimo() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("seq_tres"), filtros.getSeqTresMinimo()));
            }
            if (filtros.getSeqTresMaximo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("seq_tres"), filtros.getSeqTresMaximo()));
            }

            if (filtros.getSeqQuatroMinimo() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("seq_quatro"), filtros.getSeqQuatroMinimo()));
            }
            if (filtros.getSeqQuatroMaximo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("seq_quatro"), filtros.getSeqQuatroMaximo()));
            }

            if (filtros.getSeqCincoMinimo() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("seq_cinco"), filtros.getSeqCincoMinimo()));
            }
            if (filtros.getSeqCincoMaximo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("seq_cinco"), filtros.getSeqCincoMaximo()));
            }

            if (filtros.getSeqSeisMinimo() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("seq_seis"), filtros.getSeqSeisMinimo()));
            }
            if (filtros.getSeqSeisMaximo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("seq_seis"), filtros.getSeqSeisMaximo()));
            }

            if (filtros.getSeqSeteMinimo() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("seq_sete"), filtros.getSeqSeteMinimo()));
            }
            if (filtros.getSeqSeteMaximo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("seq_sete"), filtros.getSeqSeteMaximo()));
            }

            if (filtros.getSeqOitoMinimo() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("seq_oito"), filtros.getSeqOitoMinimo()));
            }
            if (filtros.getSeqOitoMaximo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("seq_oito"), filtros.getSeqOitoMaximo()));
            }

            // Filtros de pontos
            if (filtros.getPontosMinimo() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("pontos"), filtros.getPontosMinimo()));
            }
            if (filtros.getPontosMaximo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("pontos"), filtros.getPontosMaximo()));
            }

            // Filtros de linha
            if (filtros.getLinhaMinimo() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("linha"), filtros.getLinhaMinimo()));
            }
            if (filtros.getLinhaMaximo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("linha"), filtros.getLinhaMaximo()));
            }

            // Filtros de coluna
            if (filtros.getColunaMinimo() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("coluna"), filtros.getColunaMinimo()));
            }
            if (filtros.getColunaMaximo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("coluna"), filtros.getColunaMaximo()));
            }

            // Filtro de já foi sorteado
            if (filtros.getJaFoiSorteado() != null) {
                if (filtros.getJaFoiSorteado()) {
                    predicates.add(criteriaBuilder.equal(root.get("sorteado"), 1));
                } else {
                    predicates.add(criteriaBuilder.equal(root.get("sorteado"), 0));
                }
            }

            // Filtros de números obrigatórios (implementação futura)
            if (filtros.getNumerosObrigatorios() != null && !filtros.getNumerosObrigatorios().isEmpty()) {
                // Implementar lógica para verificar se a sequência contém os números obrigatórios
                // Isso requer uma análise da coluna 'sequencia' da tabela 'todos'
                for (Integer numero : filtros.getNumerosObrigatorios()) {
                    String numeroStr = String.format("%02d", numero);
                    predicates.add(criteriaBuilder.like(root.get("sequencia"), "%" + numeroStr + "%"));
                }
            }

            // Filtros de números proibidos (implementação futura)
            if (filtros.getNumerosProibidos() != null && !filtros.getNumerosProibidos().isEmpty()) {
                // Implementar lógica para verificar se a sequência NÃO contém os números proibidos
                for (Integer numero : filtros.getNumerosProibidos()) {
                    String numeroStr = String.format("%02d", numero);
                    predicates.add(criteriaBuilder.notLike(root.get("sequencia"), "%" + numeroStr + "%"));
                }
            }

            // Combina todos os predicados com AND
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

