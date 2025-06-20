package com.lotofacil.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "todos")
public class Todos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_todos")
    private Long idTodos;

    private String coluna;
    private String linha;
    private Integer pares;
    private Integer pontos;
    private Integer seq_dois;
    private Integer seq_tres;
    private Integer seq_quatro;
    private Integer seq_cinco;
    private Integer seq_seis;
    private Integer seq_sete;
    private Integer seq_oito;
    private String sequencia;
    private Integer soma;
    private Integer sorteado;
}
