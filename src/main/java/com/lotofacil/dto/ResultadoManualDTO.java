package com.lotofacil.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoManualDTO {

    @NotNull(message = "Número do concurso é obrigatório")
    private Integer id_sorteados;

    @NotEmpty(message = "Data do sorteio é obrigatória")
    private String dataSorteio; // Formato esperado: dd/MM/yyyy ou yyyy-MM-dd

    @NotNull(message = "Lista de números sorteados é obrigatória")
    @Size(min = 15, max = 15, message = "Devem ser fornecidos exatamente 15 números")
    private List<Integer> numeros;
}
