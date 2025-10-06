package br.com.codex.v1.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class InconsistenciaPontoDto {
    private String tipoInconsistencia;
    private String descricao;
    private LocalDate data;
    private String colaboradorNome;
    private String colaboradorPis;
    private String observacao;
    private String severidade; // BAIXA, MEDIA, ALTA

    public InconsistenciaPontoDto() {}

    public InconsistenciaPontoDto(String tipoInconsistencia, String descricao, LocalDate data,
                                  String colaboradorNome, String colaboradorPis) {
        this.tipoInconsistencia = tipoInconsistencia;
        this.descricao = descricao;
        this.data = data;
        this.colaboradorNome = colaboradorNome;
        this.colaboradorPis = colaboradorPis;
        this.severidade = "MEDIA";
    }

    public InconsistenciaPontoDto(String tipoInconsistencia, String descricao, LocalDate data,
                                  String colaboradorNome, String colaboradorPis, String severidade) {
        this(tipoInconsistencia, descricao, data, colaboradorNome, colaboradorPis);
        this.severidade = severidade;
    }
}