package br.com.codex.v1.domain.rh;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
public class CalculoFeriasEventos implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer codigoEvento;
    private String descricaoEvento;
    private BigDecimal referencia;

    @Column(precision = 10, scale = 2)
    private BigDecimal vencimentos;

    @Column(precision = 10, scale = 2)
    private BigDecimal descontos;

    private LocalDate dataProcessamento;

    @ManyToOne
    @JoinColumn(name = "calculoFerias_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CalculoFerias calculoFerias;

    public CalculoFeriasEventos() {
        super();
    }

    public CalculoFeriasEventos(Long id, Integer codigoEvento, String descricaoEvento, BigDecimal referencia,
                                BigDecimal vencimentos, BigDecimal descontos, LocalDate dataProcessamento,
                                CalculoFerias calculoFerias) {
        this.id = id;
        this.codigoEvento = codigoEvento;
        this.descricaoEvento = descricaoEvento;
        this.referencia = referencia;
        this.vencimentos = vencimentos;
        this.descontos = descontos;
        this.dataProcessamento = dataProcessamento;
        this.calculoFerias = calculoFerias;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CalculoFeriasEventos that = (CalculoFeriasEventos) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
