package com.lotofacil.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sorteados")
public class Sorteados {

    @Id
    @Column(name = "id_sorteados")
    private Long idSorteados;

    private String sorteio;
    
    @ManyToOne
    @JoinColumn(name = "id_todos")
    private Todos todos;
    
    private Integer bola1;
    private Integer bola2;
    private Integer bola3;
    private Integer bola4;
    private Integer bola5;
    private Integer bola6;
    private Integer bola7;
    private Integer bola8;
    private Integer bola9;
    private Integer bola10;
    private Integer bola11;
    private Integer bola12;
    private Integer bola13;
    private Integer bola14;
    private Integer bola15;
    private Integer pontos;
}
