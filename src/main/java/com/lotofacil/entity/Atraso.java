package com.lotofacil.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "atraso")
public class Atraso {

    @Id
    @Column(name = "id_atraso")
    private Long idAtraso;

//    @NotNull
//    @Min(1)
//    @Max(25)
//    @Column(nullable = false, unique = true)
//    private Integer numero; // Número da loteria (1 a 25)

    @NotNull
    @Column(nullable = false)
    private Integer contagem = 0; // Quantidade de concursos em atraso

    private String ultimo; // Data do último sorteio em que apareceu
}
