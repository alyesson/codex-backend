package br.com.codex.v1.domain.rh;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Getter
@Setter
public class FolhaMensalEventos implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigoEvento;
    private String descricaoEvento;
    private String referencia;

    @Column(precision = 10, scale = 2)
    private BigDecimal vencimentos;

    @Column(precision = 10, scale = 2)
    private BigDecimal descontos;

    @ManyToOne
    @JoinColumn(name = "folhaMensal_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private FolhaMensal folhaMensal;

    public FolhaMensalEventos() {
        super();
    }

    public FolhaMensalEventos(Long id, String codigoEvento, String descricaoEvento, String referencia,
                                 BigDecimal vencimentos, BigDecimal descontos,
                                 FolhaMensal folhaMensal) {
        this.id = id;
        this.codigoEvento = codigoEvento;
        this.descricaoEvento = descricaoEvento;
        this.referencia = referencia;
        this.vencimentos = vencimentos;
        this.descontos = descontos;
        this.folhaMensal = folhaMensal;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FolhaMensalEventos that = (FolhaMensalEventos) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
