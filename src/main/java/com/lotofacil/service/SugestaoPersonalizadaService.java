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

        // Configura paginação
        Pageable pageable = PageRequest.of(filtros.getPage(), filtros.getSize());

        // Executa a busca
        return todosRepository.findAll(spec, pageable);
    }

    private Specification<Todos> criarEspecificacao(FiltroSugestaoDTO filtros) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Adiciona predicados para cada filtro fornecido

            // Filtros de Range Numérico
            addRangePredicate(predicates, criteriaBuilder, root, "pontos", filtros.getPontosMin(), filtros.getPontosMax());
            addRangePredicate(predicates, criteriaBuilder, root, "soma", filtros.getSomaMin(), filtros.getSomaMax());
            addRangePredicate(predicates, criteriaBuilder, root, "impares", filtros.getImparesMin(), filtros.getImparesMax());
            addRangePredicate(predicates, criteriaBuilder, root, "seqDois", filtros.getSeqDoisMin(), filtros.getSeqDoisMax());
            addRangePredicate(predicates, criteriaBuilder, root, "seqTres", filtros.getSeqTresMin(), filtros.getSeqTresMax());
            addRangePredicate(predicates, criteriaBuilder, root, "seqQuatro", filtros.getSeqQuatroMin(), filtros.getSeqQuatroMax());
            addRangePredicate(predicates, criteriaBuilder, root, "seqCinco", filtros.getSeqCincoMin(), filtros.getSeqCincoMax());
            addRangePredicate(predicates, criteriaBuilder, root, "seqSeis", filtros.getSeqSeisMin(), filtros.getSeqSeisMax());
            addRangePredicate(predicates, criteriaBuilder, root, "seqSete", filtros.getSeqSeteMin(), filtros.getSeqSeteMax());
            addRangePredicate(predicates, criteriaBuilder, root, "seqOito", filtros.getSeqOitoMin(), filtros.getSeqOitoMax());

            // Filtros de Lista (Valores Exatos)
            if (filtros.getLinhas() != null && !filtros.getLinhas().isEmpty()) {
                predicates.add(root.get("linha").in(filtros.getLinhas()));
            }
            if (filtros.getColunas() != null && !filtros.getColunas().isEmpty()) {
                predicates.add(root.get("coluna").in(filtros.getColunas()));
            }

            // Filtro para excluir sorteados anteriormente
            if (filtros.getNaoIncluirSorteadosAnteriormente() != null && filtros.getNaoIncluirSorteadosAnteriormente()) {
                // Assumindo que 0 significa não sorteado
                predicates.add(criteriaBuilder.equal(root.get("sorteado"), 0));
            }

            // Combina todos os predicados com AND
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    // Helper para adicionar predicados de range (min/max)
    private void addRangePredicate(List<Predicate> predicates, javax.persistence.criteria.CriteriaBuilder cb, 
                                   javax.persistence.criteria.Root<Todos> root, String fieldName, 
                                   Integer min, Integer max) {
        if (min != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get(fieldName), min));
        }
        if (max != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get(fieldName), max));
        }
    }
}
