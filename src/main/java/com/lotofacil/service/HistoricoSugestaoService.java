package com.lotofacil.service;

import com.lotofacil.dto.SaveHistoricoDTO;
import com.lotofacil.entity.HistoricoSugestao;
import com.lotofacil.repository.HistoricoSugestaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoricoSugestaoService {

    private final HistoricoSugestaoRepository repository;

    public Page<HistoricoSugestao> listarTodos(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<HistoricoSugestao> listarTodos() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "dataCriacao"));
    }

    @Transactional
    public void salvar(List<SaveHistoricoDTO> itens, boolean manterExistentes) {
        if (!manterExistentes) {
            repository.deleteAllInBatch();
        }

        List<HistoricoSugestao> entidades = itens.stream()
                .map(i -> HistoricoSugestao.builder()
                        .numeros(i.getNumeros())
                        .tipo(i.getTipo())
                        .build())
                .collect(Collectors.toList());

        repository.saveAll(entidades);
    }

    @Transactional
    public HistoricoSugestao salvarUm(SaveHistoricoDTO dto) {
        HistoricoSugestao ent = HistoricoSugestao.builder()
                .numeros(dto.getNumeros())
                .tipo(dto.getTipo())
                .build();
        return repository.save(ent);
    }

    @Transactional
    public void excluir(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public void limparTudo() {
        repository.deleteAllInBatch();
    }


    /**
     * Gera CSV (bytes) com o histórico
     */
    public ByteArrayResource exportCsv() {
        List<HistoricoSugestao> all = listarTodos();
        StringBuilder sb = new StringBuilder();
        sb.append("id,numeros,tipo,data_criacao\n");
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (HistoricoSugestao h : all) {
            String data = h.getDataCriacao() != null ? h.getDataCriacao().format(fmt) : "";
            // Escape simples de aspas caso necessário
            String numerosEsc = h.getNumeros().replace("\"", "\"\"");
            sb.append(h.getId() == null ? "" : h.getId())
                    .append(",\"").append(numerosEsc).append("\",")
                    .append(h.getTipo()).append(",")
                    .append(data).append("\n");
        }

        byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        return new ByteArrayResource(bytes);
    }
}

