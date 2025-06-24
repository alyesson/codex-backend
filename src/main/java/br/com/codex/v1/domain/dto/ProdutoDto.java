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
    @NotBlank(message = "O ICMS situação tributária não pode ficar em branco")
    protected String icmsSituacaoTributaria;
    @NotBlank(message = "A categoria do produto não pode ficar em branco")
    protected String categoriaProduto;
    protected String ean;
    protected String extipi;
    @NotBlank(message = "O tipo de depósito não pode ficar em branco")
    protected String tipoDeposito;
    protected String peso;
    @NotNull(message = "O preço de venda não pode ficar em branco")
    protected BigDecimal precoVenda;
    protected BigDecimal precoCusto;
    protected BigDecimal margemLucro;
    protected BigDecimal creditoIcms;
    protected BigDecimal percentualIcms;
    protected BigDecimal percentualIcmsRed;
    protected String ipiSituacaotributaria;
    protected BigDecimal ipiBaseCalculo;
    protected BigDecimal percentualIpi;
    protected BigDecimal quantidadeTotalIpi; // Alterado para BigDecimal
    protected BigDecimal valorUnidTributavelIpi; // Alterado para BigDecimal
    protected BigDecimal valorIpi; // Alterado para BigDecimal
    protected String cnpjProdutorIpi;
    protected String codigoSeloControle;
    protected String quantidadeSeloControle;
    protected String classeEnquadramentoIpi;
    @NotBlank(message = "A situação tributária do PIS não pode ficar em branco")
    protected String pisSituacaoTributaria;
    protected BigDecimal percentualPis;
    protected BigDecimal pisBaseCalculo; // Alterado para BigDecimal
    protected BigDecimal valorPis; // Alterado para BigDecimal
    protected BigDecimal pisValorAliquota;
    protected String cofinsSituacaotributaria; // Alterado para String
    protected BigDecimal percentualCofins;
    protected BigDecimal cofinsBaseCalculo;
    protected BigDecimal valorAliquotaCofins;
    protected BigDecimal valorCofins;
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
        this.categoriaProduto = obj.getCategoriaProduto();
        this.ean = obj.getEan();
        this.extipi = obj.getExtipi();
        this.tipoDeposito = obj.getTipoDeposito();
        this.peso = obj.getPeso();
        this.precoVenda = obj.getPrecoVenda();
        this.precoCusto = obj.getPrecoCusto();
        this.margemLucro = obj.getMargemLucro();
        this.origemProduto = obj.getOrigemProduto();
        this.icmsSituacaoTributaria = obj.getIcmsSituacaoTributaria();
        this.creditoIcms = obj.getCreditoIcms();
        this.percentualIcms = obj.getPercentualIcms();
        this.percentualIcmsRed = obj.getPercentualIcmsRed();
        this.ipiSituacaotributaria = obj.getIpiSituacaotributaria();
        this.ipiBaseCalculo = obj.getIpiBaseCalculo();
        this.percentualIpi = obj.getPercentualIpi();
        this.quantidadeTotalIpi = obj.getQuantidadeTotalIpi();
        this.valorUnidTributavelIpi = obj.getValorUnidTributavelIpi();
        this.valorIpi = obj.getValorIpi();
        this.cnpjProdutorIpi = obj.getCnpjProdutorIpi();
        this.codigoSeloControle = obj.getCodigoSeloControle();
        this.quantidadeSeloControle = obj.getQuantidadeSeloControle();
        this.classeEnquadramentoIpi = obj.getClasseEnquadramentoIpi();
        this.pisSituacaoTributaria = obj.getPisSituacaoTributaria();
        this.percentualPis = obj.getPercentualPis();
        this.pisBaseCalculo = obj.getPisBaseCalculo();
        this.valorPis = obj.getValorPis();
        this.pisValorAliquota = obj.getPisValorAliquota();
        this.cofinsSituacaotributaria = obj.getCofinsSituacaotributaria();
        this.percentualCofins = obj.getPercentualCofins();
        this.cofinsBaseCalculo = obj.getCofinsBaseCalculo();
        this.valorAliquotaCofins = obj.getValorAliquotaCofins();
        this.valorCofins = obj.getValorCofins();
        this.produtoProduzido = obj.isProdutoProduzido();
    }

    public void setDescricao(String descricao) {
        this.descricao = capitalizarPalavras(descricao);
    }

    public void setDescricaoCest(String descricaoCest) { this.descricaoCest = capitalizarPalavras(descricaoCest); }
}
