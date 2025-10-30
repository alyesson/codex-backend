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
public class FolhaFeriasEventos implements Serializable {
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
    @JoinColumn(name = "folhaFerias_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private FolhaFerias folhaFerias;

    public FolhaFeriasEventos() {
        super();
    }

    public FolhaFeriasEventos(Long id, Integer codigoEvento, String descricaoEvento, BigDecimal referencia,
                              BigDecimal vencimentos, BigDecimal descontos, LocalDate dataProcessamento,
                              FolhaFerias folhaFerias) {
        this.id = id;
        this.codigoEvento = codigoEvento;
        this.descricaoEvento = descricaoEvento;
        this.referencia = referencia;
        this.vencimentos = vencimentos;
        this.descontos = descontos;
        this.dataProcessamento = dataProcessamento;
        this.folhaFerias = folhaFerias;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FolhaFeriasEventos that = (FolhaFeriasEventos) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
