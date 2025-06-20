package com.lotofacil.repository;

import com.lotofacil.entity.Todos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TodosRepository extends JpaRepository<Todos, Long> {
    
    // Método para buscar pela sequência exata de números (requer índice na coluna 'sequencia')
    Optional<Todos> findBySequencia(String sequencia);

    Page<Todos> findAll(Specification<Todos> spec, Pageable pageable);
}
