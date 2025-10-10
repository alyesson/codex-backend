package br.com.codex.v1.domain.rh;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Getter
@Setter
public class FolhaQuinzenalEventos implements Serializable {
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
    @JoinColumn(name = "folhaQuinzenal_id")
    private FolhaQuinzenal folhaQuinzenal;

    public FolhaQuinzenalEventos() {
        super();
    }

    public FolhaQuinzenalEventos(Long id, String codigoEvento, String descricaoEvento, String referencia,
                                 BigDecimal vencimentos, BigDecimal descontos,
                                 FolhaQuinzenal folhaQuinzenal) {
        this.id = id;
        this.codigoEvento = codigoEvento;
        this.descricaoEvento = descricaoEvento;
        this.referencia = referencia;
        this.vencimentos = vencimentos;
        this.descontos = descontos;
        this.folhaQuinzenal = folhaQuinzenal;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FolhaQuinzenalEventos that = (FolhaQuinzenalEventos) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
