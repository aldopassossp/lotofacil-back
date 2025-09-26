package com.lotofacil.controller;

import com.lotofacil.dto.ResultadoManualDTO;
import com.lotofacil.entity.Sorteados;
import com.lotofacil.service.ResultadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/resultados")
@Tag(name = "Resultados", description = "Operações relacionadas aos resultados dos sorteios")
public class ResultadoController {

    @Autowired
    private ResultadoService resultadoService;

    @PostMapping("/manual")
    @Operation(summary = "Adiciona um novo resultado manualmente", 
               description = "Permite a inserção manual de um resultado de sorteio, atualizando as tabelas relacionadas.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Resultado adicionado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou concurso já existente"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Sorteados> adicionarResultadoManual(@Valid @RequestBody ResultadoManualDTO resultadoManualDTO) {
        System.out.println(resultadoManualDTO.toString());
        Sorteados novoResultado = resultadoService.adicionarResultadoManual(resultadoManualDTO);
        return new ResponseEntity<>(novoResultado, HttpStatus.CREATED);
    }
}
