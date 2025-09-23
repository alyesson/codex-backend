package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.estoque.SolicitacaoMaterial;
import br.com.codex.v1.domain.estoque.SolicitacaoMaterialItens;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class SolicitacaoMaterialItensDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String codigo;
    private String descricao;
    private Integer quantidade;
    private String unidadeMedida;
    private SolicitacaoMaterialDto solicitacaoMaterial;

    public SolicitacaoMaterialItensDto() {
        super();
    }

    public SolicitacaoMaterialItensDto(SolicitacaoMaterialItens obj) {
        this.id = obj.getId();
        this.codigo = obj.getCodigo();
        this.descricao = obj.getDescricao();
        this.quantidade = obj.getQuantidade();
        this.unidadeMedida = obj.getUnidadeMedida();
        this.solicitacaoMaterial = new SolicitacaoMaterialDto(obj.getSolicitacaoMaterial());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SolicitacaoMaterialItensDto that = (SolicitacaoMaterialItensDto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
