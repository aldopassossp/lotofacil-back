package com.lotofacil.repository;

import com.lotofacil.dto.dashboard.ConcursoParesDTO;
import com.lotofacil.dto.dashboard.ConcursoSomaDTO;
import com.lotofacil.entity.Sorteados;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SorteadosRepository extends JpaRepository<Sorteados, Long> {

    // Verifica se existe sorteio por ID
    boolean existsByIdSorteados(Integer idSorteados);

    // 🔸 Consulta a soma dos últimos N resultados (JOIN com Todos)
    @Query("SELECT new com.lotofacil.dto.dashboard.ConcursoSomaDTO(s.idSorteados, s.sorteio, t.soma) " +
            "FROM Sorteados s JOIN s.todos t ORDER BY s.idSorteados DESC")
    List<ConcursoSomaDTO> findUltimosNComSoma(Pageable pageable);

    // 🔸 Consulta a contagem de pares dos últimos N resultados (JOIN com Todos)
    @Query("SELECT new com.lotofacil.dto.dashboard.ConcursoParesDTO(s.idSorteados, s.sorteio, t.pares) " +
            "FROM Sorteados s JOIN s.todos t ORDER BY s.idSorteados DESC")
    List<ConcursoParesDTO> findUltimosNComPares(Pageable pageable);

    // 🔸 Consulta os N concursos mais recentes
    List<Sorteados> findTopByOrderByIdSorteadosDesc(Pageable pageable);

    // 🔸 Consulta todos ordenados decrescente
    List<Sorteados> findAllByOrderByIdSorteadosDesc();

    // 🔥 Contagem de seq_dois usando nativeQuery
    @Query(value = "SELECT t.seq_dois AS valor, COUNT(s.id_sorteados) AS contagem " +
            "FROM sorteados s " +
            "JOIN todos t ON t.id_todos = s.id_todos " +
            "GROUP BY t.seq_dois " +
            "ORDER BY contagem DESC",
            nativeQuery = true)
    List<Object[]> findContagemSeqDoisUltimosN(Pageable pageable);

    // 🔥 Contagem de seq_tres usando nativeQuery
    @Query(value = "SELECT t.seq_tres AS valor, COUNT(s.id_sorteados) AS contagem " +
            "FROM sorteados s " +
            "JOIN todos t ON t.id_todos = s.id_todos " +
            "GROUP BY t.seq_tres " +
            "ORDER BY contagem DESC",
            nativeQuery = true)
    List<Object[]> findContagemSeqTresUltimosN(Pageable pageable);

    // 🔥 Contagem de seq_quatro usando nativeQuery
    @Query(value = "SELECT t.seq_quatro AS valor, COUNT(s.id_sorteados) AS contagem " +
            "FROM sorteados s " +
            "JOIN todos t ON t.id_todos = s.id_todos " +
            "GROUP BY t.seq_quatro " +
            "ORDER BY contagem DESC",
            nativeQuery = true)
    List<Object[]> findContagemSeqQuatroUltimosN(Pageable pageable);

    // 🔥 Ocorrência por linha usando nativeQuery
    @Query(value = "SELECT t.linha AS valor, COUNT(s.id_sorteados) AS contagem " +
            "FROM sorteados s " +
            "JOIN todos t ON t.id_todos = s.id_todos " +
            "GROUP BY t.linha " +
            "ORDER BY contagem DESC",
            nativeQuery = true)
    List<Object[]> findOcorrenciaLinhaUltimosN(Pageable pageable);

    // 🔥 Ocorrência por coluna usando nativeQuery
    @Query(value = "SELECT t.coluna AS valor, COUNT(s.id_sorteados) AS contagem " +
            "FROM sorteados s " +
            "JOIN todos t ON t.id_todos = s.id_todos " +
            "GROUP BY t.coluna " +
            "ORDER BY contagem DESC",
            nativeQuery = true)
    List<Object[]> findOcorrenciaColunaUltimosN(Pageable pageable);

    @Query(value = "SELECT dezena, COUNT(*) as contagem FROM ( " +
            "SELECT bola1 AS dezena FROM (SELECT * FROM sorteados ORDER BY id_sorteados DESC LIMIT :n) s UNION ALL " +
            "SELECT bola2 FROM (SELECT * FROM sorteados ORDER BY id_sorteados DESC LIMIT :n) s UNION ALL " +
            "SELECT bola3 FROM (SELECT * FROM sorteados ORDER BY id_sorteados DESC LIMIT :n) s UNION ALL " +
            "SELECT bola4 FROM (SELECT * FROM sorteados ORDER BY id_sorteados DESC LIMIT :n) s UNION ALL " +
            "SELECT bola5 FROM (SELECT * FROM sorteados ORDER BY id_sorteados DESC LIMIT :n) s UNION ALL " +
            "SELECT bola6 FROM (SELECT * FROM sorteados ORDER BY id_sorteados DESC LIMIT :n) s UNION ALL " +
            "SELECT bola7 FROM (SELECT * FROM sorteados ORDER BY id_sorteados DESC LIMIT :n) s UNION ALL " +
            "SELECT bola8 FROM (SELECT * FROM sorteados ORDER BY id_sorteados DESC LIMIT :n) s UNION ALL " +
            "SELECT bola9 FROM (SELECT * FROM sorteados ORDER BY id_sorteados DESC LIMIT :n) s UNION ALL " +
            "SELECT bola10 FROM (SELECT * FROM sorteados ORDER BY id_sorteados DESC LIMIT :n) s UNION ALL " +
            "SELECT bola11 FROM (SELECT * FROM sorteados ORDER BY id_sorteados DESC LIMIT :n) s UNION ALL " +
            "SELECT bola12 FROM (SELECT * FROM sorteados ORDER BY id_sorteados DESC LIMIT :n) s UNION ALL " +
            "SELECT bola13 FROM (SELECT * FROM sorteados ORDER BY id_sorteados DESC LIMIT :n) s UNION ALL " +
            "SELECT bola14 FROM (SELECT * FROM sorteados ORDER BY id_sorteados DESC LIMIT :n) s UNION ALL " +
            "SELECT bola15 FROM (SELECT * FROM sorteados ORDER BY id_sorteados DESC LIMIT :n) s " +
            ") AS todas_dezenas " +
            "GROUP BY dezena " +
            "ORDER BY contagem DESC",
            nativeQuery = true)
    List<Object[]> getFrequenciaNumerosUltimosN(@Param("n") int n);

    @Query(value = "SELECT dezena, COUNT(*) as contagem FROM ( " +
            "SELECT bola1 AS dezena FROM sorteados UNION ALL " +
            "SELECT bola2 FROM sorteados UNION ALL " +
            "SELECT bola3 FROM sorteados UNION ALL " +
            "SELECT bola4 FROM sorteados UNION ALL " +
            "SELECT bola5 FROM sorteados UNION ALL " +
            "SELECT bola6 FROM sorteados UNION ALL " +
            "SELECT bola7 FROM sorteados UNION ALL " +
            "SELECT bola8 FROM sorteados UNION ALL " +
            "SELECT bola9 FROM sorteados UNION ALL " +
            "SELECT bola10 FROM sorteados UNION ALL " +
            "SELECT bola11 FROM sorteados UNION ALL " +
            "SELECT bola12 FROM sorteados UNION ALL " +
            "SELECT bola13 FROM sorteados UNION ALL " +
            "SELECT bola14 FROM sorteados UNION ALL " +
            "SELECT bola15 FROM sorteados " +
            ") AS todas_dezenas " +
            "GROUP BY dezena " +
            "ORDER BY contagem DESC",
            nativeQuery = true)
    List<Object[]> getFrequenciaNumerosTodos();


}
