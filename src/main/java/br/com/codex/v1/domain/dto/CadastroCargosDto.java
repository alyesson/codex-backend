package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.rh.CadastroCargos;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

@Getter
@Setter
public class CadastroCargosDto implements Serializable {
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

    public CadastroCargosDto() {
        super();
    }

    public CadastroCargosDto(CadastroCargos obj) {
        this.id = obj.getId();
        this.codigoCargo = obj.getCodigoCargo();
        this.descricaoCargo = obj.getDescricaoCargo();
        this.codCbo = obj.getCodCbo();
        this.descricaoCbo = obj.getDescricaoCbo();
        this.escolaridade = obj.getEscolaridade();
        this.salario = obj.getSalario();
    }

    public void setDescricaoCargo(String descricaoCargo) {
        this.descricaoCargo = capitalizarPalavras(descricaoCargo);
    }

    public void setEscolaridade(String escolaridade) {
        this.escolaridade = capitalizarPalavras(escolaridade);
    }

    public void setDescricaoCbo(String descricaoCbo) {
        this.descricaoCbo = capitalizarPalavras(descricaoCbo);
    }
}
