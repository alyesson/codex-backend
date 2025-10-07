package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.enums.Situacao;
import br.com.codex.v1.domain.rh.CadastroColaboradores;
import br.com.codex.v1.domain.rh.CadastroJornada;
import br.com.codex.v1.domain.rh.EspelhoPonto;
import br.com.codex.v1.utilitario.LocalTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class EspelhoPontoDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private CadastroColaboradores colaborador;
    private CadastroJornada jornada;
    private LocalDate data;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime entrada;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime saidaAlmoco;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime retornoAlmoco;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime saida;

    private Integer horasTrabalhadasMinutos;
    private Integer horasExtrasMinutos;
    private Integer horasFaltantesMinutos;
    private BigDecimal custoHorasExtras;
    private Integer horasDeveriaTrabalharMinutos;
    private Situacao situacao;
    private String observacoes;
    private LocalDateTime dataProcessamento;

    public EspelhoPontoDto() {
        super();
    }

    public EspelhoPontoDto(EspelhoPonto obj) {
        this.id = obj.getId();
        this.colaborador = obj.getColaborador();
        this.data = obj.getData();
        this.entrada = obj.getEntrada();
        this.saidaAlmoco = obj.getSaidaAlmoco();
        this.retornoAlmoco = obj.getRetornoAlmoco();
        this.saida = obj.getSaida();
        this.horasTrabalhadasMinutos = obj.getHorasTrabalhadasMinutos();
        this.horasExtrasMinutos = obj.getHorasExtrasMinutos();
        this.horasFaltantesMinutos = obj.getHorasFaltantesMinutos();
        this.custoHorasExtras = obj.getCustoHorasExtras();
        this.horasDeveriaTrabalharMinutos = obj.getHorasDeveriaTrabalharMinutos();
        this.situacao = obj.getSituacao();
        this.observacoes = obj.getObservacoes();
        this.dataProcessamento = obj.getDataProcessamento();
    }

    // Métodos de utilidade (mantidos iguais - estão corretos)
    public Duration getHorasTrabalhadas() {
        return horasTrabalhadasMinutos != null ? Duration.ofMinutes(horasTrabalhadasMinutos) : Duration.ZERO;
    }

    public void setHorasTrabalhadas(Duration duration) {
        this.horasTrabalhadasMinutos = (int) duration.toMinutes();
    }

    public Duration getHorasExtras() {
        return horasExtrasMinutos != null ? Duration.ofMinutes(horasExtrasMinutos) : Duration.ZERO;
    }

    public void setHorasExtras(Duration duration) {
        this.horasExtrasMinutos = (int) duration.toMinutes();
    }

    public Duration getHorasFaltantes() {
        return horasFaltantesMinutos != null ? Duration.ofMinutes(horasFaltantesMinutos) : Duration.ZERO;
    }

    public void setHorasFaltantes(Duration duration) {
        this.horasFaltantesMinutos = (int) duration.toMinutes();
    }

    public Duration getHorasDeveriaTrabalhar() {
        return horasDeveriaTrabalharMinutos != null ? Duration.ofMinutes(horasDeveriaTrabalharMinutos) : Duration.ZERO;
    }

    public void setHorasDeveriaTrabalhar(Duration duration) {
        this.horasDeveriaTrabalharMinutos = (int) duration.toMinutes();
    }
}