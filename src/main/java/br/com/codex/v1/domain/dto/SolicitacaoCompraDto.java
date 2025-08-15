package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.compras.SolicitacaoCompra;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
public class SolicitacaoCompraDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Long id;
    protected String solicitante;
    protected String departamento;
    protected Date dataSolicitacao;
    @NotNull(message = "Centro de custo não pode estar em branco")
    protected String centroCusto;
    protected String motivoCompra;
    protected String destinoMaterial;
    protected String urgente;
    protected String opcaoMarca;
    protected String itemEstoque;
    private List<SolicitacaoItensCompraDto> itens;
    protected String situacao;

    public SolicitacaoCompraDto() {
        super();
    }

    public SolicitacaoCompraDto(SolicitacaoCompra obj) {
        this.id = obj.getId();
        this.solicitante = obj.getSolicitante();
        this.departamento = obj.getDepartamento();
        this.dataSolicitacao = obj.getDataSolicitacao();
        this.centroCusto = obj.getCentroCusto();
        this.motivoCompra = obj.getMotivoCompra();
        this.destinoMaterial = obj.getDestinoMaterial();
        this.urgente = obj.getUrgente();
        this.opcaoMarca = obj.getOpcaoMarca();
        this.itemEstoque = obj.getItemEstoque();
        this.situacao = obj.getSituacao();
    }
}
