package com.lotofacil.entity;

import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "filtros_favoritos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FiltroFavorito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long usuarioId; // opcional

    private String nome;

    @Column(columnDefinition = "json")
    private String filtrosJson; // armazena o DTO convertido em JSON

    private java.time.LocalDateTime createdAt;
}

