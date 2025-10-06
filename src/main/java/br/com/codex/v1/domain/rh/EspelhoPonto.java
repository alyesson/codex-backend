package br.com.codex.v1.domain.rh;

import br.com.codex.v1.domain.dto.EspelhoPontoDto;
import br.com.codex.v1.domain.enums.Situacao;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Getter
@Setter
public class EspelhoPonto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colaborador_id", nullable = false)
    private CadastroColaboradores colaborador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jornada_id")
    private CadastroJornada jornada;

    @Column(name = "data_referencia", nullable = false)
    private LocalDate data;

    private LocalTime entrada;
    private LocalTime saidaAlmoco;
    private LocalTime retornoAlmoco;
    private LocalTime saida;

    @Column(name = "horas_trabalhadas_minutos")
    private Integer horasTrabalhadasMinutos;

    @Column(name = "horas_extras_minutos")
    private Integer horasExtrasMinutos;

    @Column(name = "horas_faltantes_minutos")
    private Integer horasFaltantesMinutos;

    @Column(name = "custo_horas_extras", precision = 10, scale = 2)
    private BigDecimal custoHorasExtras;

    @Column(name = "horas_deveria_trabalhar_minutos")
    private Integer horasDeveriaTrabalharMinutos;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Situacao situacao;

    @Column(length = 500)
    private String observacoes;

    @CreationTimestamp
    @Column(name = "data_processamento")
    private LocalDateTime dataProcessamento;

    public EspelhoPonto() {
        super();
    }

    // Construtor para DTO
    public EspelhoPonto(EspelhoPontoDto obj) {
        this.id = obj.getId();
        this.colaborador = obj.getColaborador();
        this.jornada = obj.getJornada();
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

    // Métodos de utilidade
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EspelhoPonto that = (EspelhoPonto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}