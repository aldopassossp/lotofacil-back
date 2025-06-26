package com.lotofacil.service.importer;

import com.lotofacil.entity.Sorteados;
import com.lotofacil.entity.Todos;
import com.lotofacil.exception.ImportacaoException;
import com.lotofacil.repository.SorteadosRepository;
import com.lotofacil.service.ResultadoUpdateService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ResultadoImporterService {

    private static final Logger log = LoggerFactory.getLogger(ResultadoImporterService.class);

    @Autowired
    private SorteadosRepository sorteadosRepository;

    @Autowired
    private ResultadoUpdateService resultadoUpdateService; // Injetar o novo serviço

  //  @Transactional
    public Map<String, Object> importarResultados(MultipartFile file) {
        log.info("Iniciando importação do arquivo: {}", file.getOriginalFilename());
        int adicionados = 0;
        int ignorados = 0;
        List<Integer> ultimaListaNumerosImportados = null;

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            // Pular cabeçalho se houver
            if (rows.hasNext()) {
                rows.next();
            }

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                try {
                    Integer concurso = getIntValue(currentRow.getCell(0));
                    String dataSorteioStr = getStringValue(currentRow.getCell(1));
                    List<Integer> numeros = new ArrayList<>();

                    // Verificar se o concurso já existe
                    if (sorteadosRepository.existsByIdSorteados(Long.valueOf(concurso))) {
                        log.debug("Concurso {} já existe, ignorando.", concurso);
                        ignorados++;
                        continue;
                    } else {
                        for (int i = 2; i < 17; i++) { // Colunas 3 a 17 (índice 2 a 16)
                            numeros.add(getIntValue(currentRow.getCell(i)));
                        }
                    }

                    if (concurso == null || dataSorteioStr == null || numeros.contains(null) || numeros.size() != 15) {
                        log.warn("Linha inválida ou incompleta ignorada: Concurso={}, Data={}, Números={}", concurso, dataSorteioStr, numeros);
                        ignorados++;
                        continue;
                    }

                    // Validar números (1-25, únicos)
                    if (!validarNumeros(numeros)) {
                         log.warn("Números inválidos na linha do concurso {}: {}. Linha ignorada.", concurso, numeros);
                         ignorados++;
                         continue;
                    }

                    // Usar o ResultadoUpdateService para atualizar atraso e todos
                    Todos todosCorrespondente = resultadoUpdateService.atualizarDadosAposNovoResultado(numeros, dataSorteioStr);

                    if (todosCorrespondente == null) {
                        log.error("Não foi possível processar a linha do concurso {} devido a erro na busca/atualização da tabela todos. Linha ignorada.", concurso);
                        ignorados++;
                        continue; // Pula para a próxima linha
                    }

                    // Criar e salvar a entidade Sorteados
                    Sorteados novoSorteado = new Sorteados();
                    novoSorteado.setIdSorteados(Long.valueOf(concurso));
                    novoSorteado.setSorteio(dataSorteioStr); // Manter como string por enquanto
                    novoSorteado.setTodos(todosCorrespondente);

                    // Preencher bolas b1 a b15 (números já validados e ordenados implicitamente pela busca)
                    Collections.sort(numeros); // Garante ordem
                    for (int i = 0; i < numeros.size(); i++) {
                        try {
                            Sorteados.class.getMethod("setBola" + (i + 1), Integer.class).invoke(novoSorteado, numeros.get(i));
                        } catch (Exception e) {
                            log.error("Erro ao definir bola{} para o concurso {}", (i + 1), concurso, e);
                            throw new ImportacaoException("Erro interno ao processar números sorteados para o concurso " + concurso);
                        }
                    }
                    // novoSorteado.setPontos(...); // Calcular pontos se necessário

                    sorteadosRepository.save(novoSorteado);
                    adicionados++;
                    ultimaListaNumerosImportados = numeros;
                    log.debug("Concurso {} importado com sucesso.", concurso);

                } catch (Exception e) {
                    log.error("Erro ao processar linha {}: {}", currentRow.getRowNum() + 1, " -$$$$$- ", " ###### ");
                    ignorados++;
                }
            }

        } catch (Exception e) {
            log.error("Erro geral durante a importação do arquivo: {}", e.getMessage(), e);
            throw new ImportacaoException("Erro ao ler o arquivo Excel: " + e.getMessage());
        }

        log.info("Importação concluída. Adicionados: {}, Ignorados: {}", adicionados, ignorados);
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("adicionados", adicionados);
        resultado.put("ignorados", ignorados);

        if (adicionados > 0) {
            log.info("Iniciando atualização final de pontos após a importação.");
            resultadoUpdateService.atualizarPontosAsync(ultimaListaNumerosImportados);
        }

        return resultado;
    }

    private boolean validarNumeros(List<Integer> numeros) {
        if (numeros == null || numeros.size() != 15) return false;
        Set<Integer> unicos = new HashSet<>();
        for (Integer n : numeros) {
            if (n == null || n < 1 || n > 25 || !unicos.add(n)) {
                return false;
            }
        }
        return true;
    }

    private Integer getIntValue(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC) {
            return (int) cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return Integer.parseInt(cell.getStringCellValue().trim());
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private String getStringValue(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue().trim();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                // Formatar data
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // Ou outro formato desejado
                return sdf.format(cell.getDateCellValue());
            } else {
                return String.valueOf((int) cell.getNumericCellValue());
            }
        }
        return null;
    }
}
