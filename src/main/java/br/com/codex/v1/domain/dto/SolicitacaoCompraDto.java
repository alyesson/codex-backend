package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.compras.SolicitacaoCompra;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

@Getter
@Setter
public class SolicitacaoCompraDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Long id;
    protected String solicitante;
    protected String departamento;
    protected LocalDate dataSolicitacao;
    @NotNull(message = "Centro de custo não pode estar em branco")
    protected String centroCusto;
    protected String motivoCompra;
    protected String destinoMaterial;
    @NotNull(message = "Campo é urgente não pode estar em branco")
    protected String urgente;
    @NotNull(message = "Campo opção de marca não pode estar em branco")
    protected String opcaoMarca;
    @NotNull(message = "Campo item de estoque não pode estar em branco")
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

    public void setMotivoCompra(String motivoCompra) {
        this.motivoCompra = capitalizarPalavras(motivoCompra);
    }

    public void setDestinoMaterial(String destinoMaterial) {
        this.destinoMaterial = capitalizarPalavras(destinoMaterial);
    }
}
