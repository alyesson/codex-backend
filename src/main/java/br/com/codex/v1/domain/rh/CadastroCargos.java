package br.com.codex.v1.domain.rh;

import br.com.codex.v1.domain.dto.CadastroCargosDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

@Entity
@Getter
@Setter
public class CadastroCargos implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codigoCargo;
    private String descricaoCargo;
    private String codCbo;
    private String descricaoCbo;
    private String escolaridade;
    private BigDecimal salario;

    public CadastroCargos() {
        super();
    }

    public CadastroCargos(Long id, String codigoCargo, String descricaoCargo, String codCbo, String descricaoCbo, String escolaridade, BigDecimal salario) {
        this.id = id;
        this.codigoCargo = codigoCargo;
        this.descricaoCargo = descricaoCargo;
        this.codCbo = codCbo;
        this.descricaoCbo = descricaoCbo;
        this.escolaridade = escolaridade;
        this.salario = salario;
    }

    public CadastroCargos(CadastroCargosDto obj) {
        this.id = obj.getId();
        this.codigoCargo = obj.getCodigoCargo();
        this.descricaoCargo = obj.getDescricaoCargo();
        this.codCbo = obj.getCodCbo();
        this.descricaoCbo = obj.getDescricaoCbo();
        this.escolaridade = obj.getEscolaridade();
        this.salario = obj.getSalario();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CadastroCargos that = (CadastroCargos) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
