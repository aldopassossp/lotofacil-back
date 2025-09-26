package com.lotofacil.repository;

import com.lotofacil.entity.HistoricoSugestao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoricoSugestaoRepository extends JpaRepository<HistoricoSugestao, Long> {
    Page<HistoricoSugestao> findAll(Pageable pageable);
}
