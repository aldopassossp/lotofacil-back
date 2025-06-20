package com.lotofacil.controller;

import com.lotofacil.exception.ImportacaoException;
import com.lotofacil.service.importer.ResultadoImporterService; // Corrigido o import
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/importador") // Renomeado para refletir o serviço
@Tag(name = "Importador", description = "Operações de importação de resultados")
public class ResultadoImporterController { // Renomeado para consistência

    @Autowired
    private ResultadoImporterService resultadoImporterService; // Corrigido o nome da classe e variável

    @PostMapping("/planilha")
    @Operation(summary = "Importar resultados de planilha Excel", description = "Importa resultados de sorteios de um arquivo .xlsx")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Importação concluída com sucesso",
                         content = @Content(mediaType = "application/json",
                                          schema = @Schema(example = "{\"importados\": 10, \"ignorados\": 2}"))),
            @ApiResponse(responseCode = "400", description = "Arquivo inválido ou erro na planilha"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Map<String, Object>> importarPlanilha(
            @Parameter(description = "Arquivo Excel (.xlsx) contendo os resultados", required = true)
            @RequestParam("file") MultipartFile file) {
        try {
            // Verifica se o arquivo não está vazio e se é um .xlsx
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("erro", "Arquivo vazio."));
            }
            String contentType = file.getContentType();
            if (contentType == null || 
                (!contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") && 
                 !file.getOriginalFilename().toLowerCase().endsWith(".xlsx")) ) {
                return ResponseEntity.badRequest().body(Map.of("erro", "Formato de arquivo inválido. Envie um arquivo .xlsx."));
            }

            Map<String, Object> resultado = resultadoImporterService.importarResultados(file); // Corrigido o nome da variável
            return ResponseEntity.ok(resultado);
        } catch (ImportacaoException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
             // Logar o erro completo aqui seria importante
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("erro", "Erro interno inesperado durante a importação."));
        }
    }
}

