package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.estoque.Produto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

@Getter
@Setter
public class ProdutoDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    protected Long id;
    @NotBlank(message = "Código do produto não pode ficar em branco")
    protected String codigo;
    @NotBlank(message = "O nome do produto não pode ficar em branco")
    protected String descricao;
    protected String grupo;
    protected String subGrupo;
    @NotNull(message = "Quantidade mínima não pode ficar em branco")
    protected Integer minimo;
    @NotNull(message = "Quantidade máxima não pode ficar em branco")
    protected Integer maximo;
    @NotBlank(message = "A unidade comercial não pode ficar em branco")
    protected String unidadeComercial;
    protected String local;
    @NotBlank(message = "O código ncm não pode ficar em branco")
    protected String codigoNcm;
    @NotBlank(message = "A descrição do ncm não pode ficar em branco")
    protected String descricaoNcm;
    protected String codigoCest;
    protected String descricaoCest;
    @NotBlank(message = "A origem do produto não pode ficar em branco")
    protected String origemProduto;
    @NotBlank(message = "A categoria do produto não pode ficar em branco")
    protected String categoriaProduto;
    protected String ean;
    protected String extipi;
    @NotBlank(message = "O tipo de depósito não pode ficar em branco")
    protected String tipoDeposito;
    protected String codigoSituacaoTributaria;
    protected String peso;
    @NotNull(message = "O preço de venda não pode ficar em branco")
    protected BigDecimal precoVenda;
    protected BigDecimal precoCusto;
    protected BigDecimal margemLucro;
    protected BigDecimal percentualIcmsCst;
    protected BigDecimal percentualIcms;
    protected BigDecimal percentualIcmsRed;
    protected BigDecimal percentualIpiCst;
    protected BigDecimal percentualIpi;
    protected BigDecimal percentualPisCst;
    protected BigDecimal percentualPis;
    protected BigDecimal percentualCofinsCst;
    protected BigDecimal percentualCofins;
    @NotNull(message = "O campo produto produzido pode ficar em branco")
    protected boolean produtoProduzido;

    public ProdutoDto() {
        super();
    }

    public ProdutoDto(Produto obj) {
        this.id = obj.getId();
        this.codigo = obj.getCodigo();
        this.descricao = obj.getDescricao();
        this.grupo = obj.getGrupo();
        this.subGrupo = obj.getSubGrupo();
        this.minimo = obj.getMinimo();
        this.maximo = obj.getMaximo();
        this.unidadeComercial = obj.getUnidadeComercial();
        this.local = obj.getLocal();
        this.codigoNcm = obj.getCodigoNcm();
        this.descricaoNcm = obj.getDescricaoNcm();
        this.codigoCest = obj.getCodigoCest();
        this.descricaoCest = obj.getDescricaoCest();
        this.origemProduto = obj.getOrigemProduto();
        this.categoriaProduto = obj.getCategoriaProduto();
        this.ean = obj.getEan();
        this.extipi = obj.getExtipi();
        this.tipoDeposito = obj.getTipoDeposito();
        this.codigoSituacaoTributaria = obj.getCodigoSituacaoTributaria();
        this.peso = obj.getPeso();
        this.precoVenda = obj.getPrecoVenda();
        this.precoCusto = obj.getPrecoCusto();
        this.margemLucro = obj.getMargemLucro();
        this.percentualIcmsCst = obj.getPercentualIcmsCst();
        this.percentualIcms = obj.getPercentualIcms();
        this.percentualIcmsRed = obj.getPercentualIcmsRed();
        this.percentualIpiCst = obj.getPercentualIpiCst();
        this.percentualIpi = obj.getPercentualIpi();
        this.percentualPisCst = obj.getPercentualPisCst();
        this.percentualPis = obj.getPercentualPis();
        this.percentualCofinsCst = obj.getPercentualCofinsCst();
        this.percentualCofins = obj.getPercentualCofins();
        this.produtoProduzido = obj.isProdutoProduzido();
    }

    public void setDescricao(String descricao) {
        this.descricao = capitalizarPalavras(descricao);
    }

    public void setDescricaoCest(String descricaoCest) { this.descricaoCest = capitalizarPalavras(descricaoCest); }
}
