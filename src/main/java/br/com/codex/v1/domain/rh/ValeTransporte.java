package br.com.codex.v1.domain.rh;

import br.com.codex.v1.domain.dto.ValeTransporteDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
public class ValeTransporte implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "colaborador_id", nullable = false)
    private CadastroColaboradores colaborador;

    @Column(nullable = false)
    private LocalDate dataInicio;

    @Column(nullable = true)
    private LocalDate dataFim;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorDiario;

    @Column(nullable = false)
    private Integer diasUtilizados;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Column(length = 20)
    private String tipoVale; // "IDV" ou "Passe"

    @Column(length = 50)
    private String numeroCartao;

    @Column(length = 100)
    private String empresaTransporte;

    @Column(length = 500)
    private String observacao;

    @Column(nullable = false)
    private Boolean ativo;

    public ValeTransporte() {
        super();
        this.ativo = true;
    }

    public ValeTransporte(Long id, CadastroColaboradores colaborador, LocalDate dataInicio,
                          LocalDate dataFim, BigDecimal valorDiario, Integer diasUtilizados,
                          BigDecimal valorTotal, String tipoVale, String numeroCartao,
                          String empresaTransporte, String observacao, Boolean ativo) {
        this.id = id;
        this.colaborador = colaborador;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.valorDiario = valorDiario;
        this.diasUtilizados = diasUtilizados;
        this.valorTotal = valorTotal;
        this.tipoVale = tipoVale;
        this.numeroCartao = numeroCartao;
        this.empresaTransporte = empresaTransporte;
        this.observacao = observacao;
        this.ativo = ativo;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ValeTransporte that = (ValeTransporte) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}