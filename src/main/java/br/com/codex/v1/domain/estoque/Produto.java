package br.com.codex.v1.domain.estoque;

import br.com.codex.v1.domain.dto.ProdutoDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

@Entity
@Getter
@Setter
public class Produto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String codigo;
    protected String descricao;
    protected String grupo;
    protected String subGrupo;
    protected Integer minimo;
    protected Integer maximo;
    protected String unidadeComercial;
    protected String local;
    protected String codigoNcm;
    protected String descricaoNcm;
    protected String codigoCest;
    protected String descricaoCest;
    protected String categoriaProduto;
    protected String ean;
    protected String extipi;
    protected String tipoDeposito;
    protected String peso;
    protected BigDecimal precoVenda;
    protected BigDecimal precoCusto;
    protected BigDecimal margemLucro;
    protected String origemProduto;
    protected String icmsSituacaoTributaria;
    protected BigDecimal creditoIcms;
    protected BigDecimal percentualIcms;
    protected BigDecimal percentualDiferimento;
    protected BigDecimal percentualFcp;
    protected BigDecimal percentualIcmsRed;
    protected String ipiSituacaotributaria;
    protected BigDecimal ipiBaseCalculo;
    protected BigDecimal percentualIpi;
    protected BigDecimal quantidadeTotalIpi;
    protected BigDecimal valorUnidTributavelIpi;
    protected BigDecimal valorIpi;
    protected String cnpjProdutorIpi;
    protected String codigoSeloControle;
    protected String quantidadeSeloControle;
    protected String classeEnquadramentoIpi;
    protected String pisSituacaoTributaria;
    protected BigDecimal percentualPis;
    protected BigDecimal pisBaseCalculo;
    protected BigDecimal valorPis;
    protected BigDecimal pisValorAliquota;
    protected String cofinsSituacaotributaria;
    protected BigDecimal percentualCofins;
    protected BigDecimal cofinsBaseCalculo;
    protected BigDecimal valorAliquotaCofins;
    protected BigDecimal valorCofins;
    protected boolean produtoProduzido;


    public Produto() {
        super();
    }

    public Produto(Long id, String codigo, String descricao, String grupo, String subGrupo, Integer minimo, Integer maximo, String unidadeComercial, String local, String codigoNcm,
                   String descricaoNcm, String codigoCest, String descricaoCest, String categoriaProduto, String ean, String extipi, String tipoDeposito, String peso, BigDecimal precoVenda,
                   BigDecimal precoCusto, BigDecimal margemLucro, String origemProduto, String icmsSituacaoTributaria, BigDecimal creditoIcms, BigDecimal percentualIcms, BigDecimal percentualDiferimento,
                   BigDecimal percentualFcp, BigDecimal percentualIcmsRed, String ipiSituacaotributaria, BigDecimal ipiBaseCalculo, BigDecimal percentualIpi, BigDecimal quantidadeTotalIpi, BigDecimal valorUnidTributavelIpi,
                   BigDecimal valorIpi, String cnpjProdutorIpi, String codigoSeloControle, String quantidadeSeloControle, String classeEnquadramentoIpi, String pisSituacaoTributaria,
                   BigDecimal percentualPis, BigDecimal pisBaseCalculo, BigDecimal valorPis, BigDecimal pisValorAliquota, String cofinsSituacaotributaria, BigDecimal percentualCofins,
                   BigDecimal cofinsBaseCalculo, BigDecimal valorAliquotaCofins, BigDecimal valorCofins, boolean produtoProduzido) {
        this.id = id;
        this.codigo = codigo;
        this.descricao = descricao;
        this.grupo = grupo;
        this.subGrupo = subGrupo;
        this.minimo = minimo;
        this.maximo = maximo;
        this.unidadeComercial = unidadeComercial;
        this.local = local;
        this.codigoNcm = codigoNcm;
        this.descricaoNcm = descricaoNcm;
        this.codigoCest = codigoCest;
        this.descricaoCest = descricaoCest;
        this.categoriaProduto = categoriaProduto;
        this.ean = ean;
        this.extipi = extipi;
        this.tipoDeposito = tipoDeposito;
        this.peso = peso;
        this.precoVenda = precoVenda;
        this.precoCusto = precoCusto;
        this.margemLucro = margemLucro;
        this.origemProduto = origemProduto;
        this.icmsSituacaoTributaria = icmsSituacaoTributaria;
        this.creditoIcms = creditoIcms;
        this.percentualIcms = percentualIcms;
        this.percentualDiferimento = percentualDiferimento;
        this.percentualFcp = percentualFcp;
        this.percentualIcmsRed = percentualIcmsRed;
        this.ipiSituacaotributaria = ipiSituacaotributaria;
        this.ipiBaseCalculo = ipiBaseCalculo;
        this.percentualIpi = percentualIpi;
        this.quantidadeTotalIpi = quantidadeTotalIpi;
        this.valorUnidTributavelIpi = valorUnidTributavelIpi;
        this.valorIpi = valorIpi;
        this.cnpjProdutorIpi = cnpjProdutorIpi;
        this.codigoSeloControle = codigoSeloControle;
        this.quantidadeSeloControle = quantidadeSeloControle;
        this.classeEnquadramentoIpi = classeEnquadramentoIpi;
        this.pisSituacaoTributaria = pisSituacaoTributaria;
        this.percentualPis = percentualPis;
        this.pisBaseCalculo = pisBaseCalculo;
        this.valorPis = valorPis;
        this.pisValorAliquota = pisValorAliquota;
        this.cofinsSituacaotributaria = cofinsSituacaotributaria;
        this.percentualCofins = percentualCofins;
        this.cofinsBaseCalculo = cofinsBaseCalculo;
        this.valorAliquotaCofins = valorAliquotaCofins;
        this.valorCofins = valorCofins;
        this.produtoProduzido = produtoProduzido;
    }

    public Produto(ProdutoDto obj) {
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
        this.percentualDiferimento = obj.getPercentualDiferimento();
        this.percentualFcp = obj.getPercentualFcp();
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
        this.pisValorAliquota = obj.getPisValorAliquota();
        this.pisBaseCalculo = obj.getPisBaseCalculo();
        this.valorPis = obj.getValorPis();
        this.cofinsSituacaotributaria = obj.getCofinsSituacaotributaria();
        this.percentualCofins = obj.getPercentualCofins();
        this.cofinsBaseCalculo = obj.getCofinsBaseCalculo();
        this.valorAliquotaCofins = obj.getValorAliquotaCofins();
        this.valorCofins = obj.getValorCofins();
        this.produtoProduzido = obj.isProdutoProduzido();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return Objects.equals(id, produto.id) && Objects.equals(codigo, produto.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codigo);
    }

}
