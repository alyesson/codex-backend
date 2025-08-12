package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.cadastros.TabelaNcm;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

@Getter
public class TabelaNcmDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Integer id;
    @NotBlank(message = "Código Ncm não pode ficar em branco")
    protected String codigo;
    @NotBlank(message = "Descrição não pode ficar em branco")
    protected String descricao;
    @Column(length = 10)
    @NotBlank(message = "Código cest não pode ficar em branco")
    protected String codigoCest;
    @Column(length = 10)
    protected String dataInicio;
    @Column(length = 10)
    protected String unidadeMedida;
    @Column(length = 25)
    protected String categopria;

    public TabelaNcmDto() {
        super();
    }

    public TabelaNcmDto(TabelaNcm obj) {
        this.id = obj.getId();
        this.codigo = obj.getCodigo();
        this.descricao = obj.getDescricao();
        this.codigoCest = obj.getCodigoCest();
        this.dataInicio = obj.getDataInicio();
        this.unidadeMedida = obj.getUnidadeMedida();
        this.categopria = obj.getCategopria();
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setDescricao(String descricao) {
        this.descricao = capitalizarPalavras(descricao);
    }

    public void setCodigoCest(String codigoCest) {
        this.codigoCest = codigoCest;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public void setCategopria(String categopria) {
        this.categopria = categopria;
    }
}
