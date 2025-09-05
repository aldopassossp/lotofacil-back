package com.lotofacil.controller;

import com.lotofacil.dto.AtrasoNumeroDTO;
import com.lotofacil.dto.dashboard.ConcursoParesDTO;
import com.lotofacil.dto.dashboard.ConcursoSomaDTO;
import com.lotofacil.dto.dashboard.ContagemLinhaDTO;
import com.lotofacil.dto.dashboard.ValorContagemDTO;
import com.lotofacil.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Endpoints para obter dados estatísticos para o dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    private static final int DEFAULT_N = 20; // Default number of results

    @GetMapping("/soma")
    @Operation(summary = "Obtém a soma das dezenas dos últimos N resultados")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Sucesso")})
    public ResponseEntity<List<ConcursoSomaDTO>> getSomaUltimosN(
            @Parameter(description = "Número de últimos resultados a considerar", example = "20")
            @RequestParam(required = false) Integer n) {
        int limit = (n != null && n > 0) ? n : DEFAULT_N;
        return ResponseEntity.ok(dashboardService.getSomaUltimosNResultados(limit));
    }

    @GetMapping("/pares")
    @Operation(summary = "Obtém a contagem de números pares dos últimos N resultados")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Sucesso")})
    public ResponseEntity<List<ConcursoParesDTO>> getParesUltimosN(
            @Parameter(description = "Número de últimos resultados a considerar", example = "20")
            @RequestParam(required = false) Integer n) {
        int limit = (n != null && n > 0) ? n : DEFAULT_N;
        return ResponseEntity.ok(dashboardService.getParesUltimosNResultados(limit));
    }

    @GetMapping("/frequencia-numeros")
    @Operation(summary = "Obtém a frequência de cada número sorteado nos últimos N resultados")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Sucesso")})
    public ResponseEntity<List<ValorContagemDTO>> getFrequenciaNumerosUltimosN(
            @Parameter(description = "Número de últimos resultados a considerar", example = "100")
            @RequestParam(required = false) Integer n) {
        int limit = (n != null && n > 0) ? n : 10; // Default larger for frequency
        return ResponseEntity.ok(dashboardService.getFrequenciaNumerosUltimosN(limit));
    }

    @GetMapping("/contagem-sequencia-dois/{n}")
    @Operation(summary = "Obtém a contagem de ocorrências de um tipo de sequência (seq_dois, seq_tres, etc.) nos últimos N resultados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso"),
        @ApiResponse(responseCode = "400", description = "Tipo de sequência inválido")
    })
    public ResponseEntity<List<ValorContagemDTO>> getContagemSequenciaDoisUltimosN(
            @Parameter(description = "Tipo da sequência (ex: seq_dois, seq_tres, seq_quatro)", required = true, example = "seq_dois")
            @PathVariable Integer n){
        // Basic validation for tipoSequencia
//        if (!List.of("seq_dois", "seq_tres", "seq_quatro", "seq_cinco", "seq_seis", "seq_sete", "seq_oito").contains(tipoSequencia)) {
//            return ResponseEntity.badRequest().build();
//        }
        int limit = (n != null && n > 0) ? n : 50;
        return ResponseEntity.ok(dashboardService.getContagemSequenciaDoisUltimosN(limit));
    }

    @GetMapping("/contagem-sequencia-tres/{n}")
    @Operation(summary = "Obtém a contagem de ocorrências de um tipo de sequência (seq_dois, seq_tres, etc.) nos últimos N resultados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso"),
            @ApiResponse(responseCode = "400", description = "Tipo de sequência inválido")
    })
    public ResponseEntity<List<ValorContagemDTO>> getContagemSequenciaTresUltimosN(
            @Parameter(description = "Tipo da sequência (ex: seq_dois, seq_tres, seq_quatro)", required = true, example = "seq_dois")
            @PathVariable Integer n) {
        // Basic validation for tipoSequencia
//        if (!List.of("seq_dois", "seq_tres", "seq_quatro", "seq_cinco", "seq_seis", "seq_sete", "seq_oito").contains(tipoSequencia)) {
//            return ResponseEntity.badRequest().build();
//        }
        int limit = (n != null && n > 0) ? n : 50;
        return ResponseEntity.ok(dashboardService.getContagemSequenciaTresUltimosN(limit));
    }

    @GetMapping("/contagem-sequencia-quatro/{n}")
    @Operation(summary = "Obtém a contagem de ocorrências de um tipo de sequência (seq_dois, seq_tres, etc.) nos últimos N resultados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso"),
            @ApiResponse(responseCode = "400", description = "Tipo de sequência inválido")
    })
    public ResponseEntity<List<ValorContagemDTO>> getContagemSequenciaQuatroUltimosN(
            @Parameter(description = "Tipo da sequência (ex: seq_dois, seq_tres, seq_quatro)", required = true, example = "seq_dois")
            @PathVariable Integer n) {
        // Basic validation for tipoSequencia
//        if (!List.of("seq_dois", "seq_tres", "seq_quatro", "seq_cinco", "seq_seis", "seq_sete", "seq_oito").contains(tipoSequencia)) {
//            return ResponseEntity.badRequest().build();
//        }
        int limit = (n != null && n > 0) ? n : 50;
        return ResponseEntity.ok(dashboardService.getContagemSequenciaQuatroUltimosN(limit));
    }

//    @GetMapping("/ocorrencia-linha-coluna/{tipo}")
//    @Operation(summary = "Obtém a contagem de ocorrências de linha ou coluna nos últimos N resultados")
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "Sucesso"),
//        @ApiResponse(responseCode = "400", description = "Tipo inválido (deve ser 'linha' ou 'coluna')")
//    })
//    public ResponseEntity<List<ValorContagemDTO>> getOcorrenciaLinhaColunaUltimosN(
//            @Parameter(description = "Tipo da análise ('linha' ou 'coluna')", required = true, example = "linha")
//            @PathVariable String tipo,
//            @Parameter(description = "Número de últimos resultados a considerar", example = "50")
//            @RequestParam(required = false) Integer n) {
//        if (!List.of("linha", "coluna").contains(tipo)) {
//            return ResponseEntity.badRequest().build();
//        }
//        int limit = (n != null && n > 0) ? n : 50;
//        return ResponseEntity.ok(dashboardService.getOcorrenciaLinhaColunaUltimosN(tipo, limit));
//    }

    @GetMapping("/ocorrencias-linha/{n}")
    public ResponseEntity<List<ContagemLinhaDTO>> getOcorrenciasLinha(@PathVariable Integer n) {
        List<ContagemLinhaDTO> ocorrencias = dashboardService.ocorrenciaLinhaUltimosN(n);
        return ResponseEntity.ok(ocorrencias);
    }

    @GetMapping("/ocorrencias-linha/")
    public ResponseEntity<List<ContagemLinhaDTO>> getOcorrenciasTodasLinhas() {
        List<ContagemLinhaDTO> ocorrencias = dashboardService.ocorrenciaTodasLinhas();
        return ResponseEntity.ok(ocorrencias);
    }

    @GetMapping("/ocorrencias-coluna/{n}")
    public ResponseEntity<List<ContagemLinhaDTO>> getOcorrenciasColuna(@PathVariable Integer n) {
        List<ContagemLinhaDTO> ocorrencias = dashboardService.ocorrenciaColunaUltimosN(n);
        return ResponseEntity.ok(ocorrencias);
    }

    @GetMapping("/ocorrencias-coluna/")
    public ResponseEntity<List<ContagemLinhaDTO>> getOcorrenciasTodasColunas() {
        List<ContagemLinhaDTO> ocorrencias = dashboardService.ocorrenciaTodasColunas();
        return ResponseEntity.ok(ocorrencias);
    }

    @GetMapping("/atraso")
    public ResponseEntity<List<AtrasoNumeroDTO>> listarAtrasos() {
        return ResponseEntity.ok(dashboardService.getAtrasos());
    }
}
