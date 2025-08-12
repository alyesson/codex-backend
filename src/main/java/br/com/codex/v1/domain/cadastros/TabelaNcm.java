package br.com.codex.v1.domain.cadastros;

import br.com.codex.v1.domain.dto.TabelaNcmDto;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@Entity
public class TabelaNcm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Column(length = 10)
    protected String codigo;
    protected String descricao;

    @Column(length = 10)
    protected String codigoCest;

    @Column(length = 10)
    protected String dataInicio;

    @Column(length = 10)
    protected String unidadeMedida;

    @Column(length = 25)
    protected String categopria;

    public TabelaNcm() {
        super();
    }

    public TabelaNcm(Integer id, String codigo, String descricao, String codigoCest, String dataInicio, String unidadeMedida, String categopria) {
        this.id = id;
        this.codigo = codigo;
        this.descricao = descricao;
        this.codigoCest = codigoCest;
        this.dataInicio = dataInicio;
        this.unidadeMedida = unidadeMedida;
        this.categopria = categopria;
    }

    public TabelaNcm(TabelaNcmDto obj) {
        this.id = obj.getId();
        this.codigo = obj.getCodigo();
        this.descricao = obj.getDescricao();
        this.codigoCest = obj.getCodigoCest();
        this.dataInicio = obj.getDataInicio();
        this.unidadeMedida = obj.getUnidadeMedida();
        this.categopria = obj.getCategopria();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TabelaNcm tabelaNcm = (TabelaNcm) o;
        return Objects.equals(id, tabelaNcm.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
