package com.lotofacil.repository;

import com.lotofacil.entity.Atraso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AtrasoRepository extends JpaRepository<Atraso, Long> {
    
    Optional<Atraso> findByIdAtraso(Integer numero);
    
    List<Atraso> findByIdAtrasoIn(List<Integer> numeros);

    @Query(value = " SELECT a.id_atraso, a.contagem, a.ultimo " +
            "   FROM atraso a " +
            "   WHERE a.contagem > 0 " +
            "   ORDER BY a.id_atraso ",
            nativeQuery = true)
    List<Object[]> findAtrasos();
}
