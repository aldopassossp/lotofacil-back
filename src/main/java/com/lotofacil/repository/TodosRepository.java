package com.lotofacil.repository;

import com.lotofacil.entity.Todos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface TodosRepository extends JpaRepository<Todos, Long>, JpaSpecificationExecutor<Todos> {

    // Método para buscar pela sequência exata de números (requer índice na coluna 'sequencia')
    Optional<Todos> findBySequencia(String sequencia);

    Page<Todos> findAll(Specification<Todos> spec, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE Todos t SET t.pontos = 0")
    void zeraPontos();

    @Transactional
    @Modifying
    @Query(value = "UPDATE todos SET pontos = pontos + 1 WHERE sequencia LIKE %:numero%", nativeQuery = true) // [1, 2, 6]
    void atualizaPontos(@Param("numero") String numero);
}
