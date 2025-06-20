package com.lotofacil.controller;

import com.lotofacil.dto.sugestao.FiltroSugestaoDTO;
import com.lotofacil.entity.Todos;
import com.lotofacil.service.SugestaoPersonalizadaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/sugestoes")
@Tag(name = "Sugestões", description = "Operações relacionadas à geração de sugestões de jogos")
public class SugestaoController {

    @Autowired
    private SugestaoPersonalizadaService sugestaoPersonalizadaService;

    // Endpoint para buscar sugestões personalizadas com base em filtros
    @PostMapping("/personalizadas")
    @Operation(summary = "Busca sugestões de jogos personalizadas",
               description = "Retorna uma página de combinações da tabela \"todos\" que atendem aos critérios de filtro especificados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sugestões encontradas com sucesso"),
        @ApiResponse(responseCode = "400", description = "Filtros inválidos")
    })
    public ResponseEntity<Page<Todos>> buscarSugestoesPersonalizadas(
            @Valid @RequestBody FiltroSugestaoDTO filtros) {
        Page<Todos> sugestoes = sugestaoPersonalizadaService.buscarSugestoes(filtros);
        return ResponseEntity.ok(sugestoes);
    }

    // Manter o endpoint antigo de sugestões (fechamentos) se ainda for necessário
    // ou refatorá-lo/removê-lo conforme a necessidade.
    // Exemplo:
    // @GetMapping("/fechamento/{tipo}")
    // public ResponseEntity<?> getSugestoesFechamento(...) { ... }

}
