package br.com.codex.v1.domain.rh;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ImportacaoPontoResultado {
    private boolean sucesso;
    private String mensagem;
    private String nomeArquivo;
    private LocalDateTime dataImportacao;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Integer totalRegistros;
    private Integer registrosSalvos; // NOVO
    private Integer espelhosProcessados;
    private List<String> erros = new ArrayList<>();

    public ImportacaoPontoResultado() {
        this.dataImportacao = LocalDateTime.now();
    }
}
