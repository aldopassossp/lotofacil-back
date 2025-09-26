package com.lotofacil.entity;

import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "historico_sugestoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoSugestao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Ex: "01-02-05-07-09-12-15-16-18-20-21-23-24-25"
     */
    @Column(nullable = false, length = 200)
    private String numeros;

    /**
     * "15", "16" ou "17" (ou "15 números", etc.)
     */
    @Column(nullable = false, length = 20)
    private String tipo;

    @CreationTimestamp
    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    private Integer acertos;
}
