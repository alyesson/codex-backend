package br.com.codex.v1.domain.estoque;

import br.com.codex.v1.domain.dto.ProdutoDto;
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
    protected String origemProduto;
    protected String categoriaProduto;
    protected String ean;
    protected String extipi;
    protected String tipoDeposito;
    protected String codigoSituacaoTributaria;
    protected String peso;
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
    protected boolean produtoProduzido;

    public Produto() {
        super();
    }

    public Produto(Long id, String codigo, String descricao, String grupo, String subGrupo, Integer minimo,
                   Integer maximo, String unidadeComercial, String local, String codigoNcm, String descricaoNcm,
                   String codigoCest, String descricaoCest, String origemProduto, String categoriaProduto, String ean,
                   String extipi, String tipoDeposito, String codigoSituacaoTributaria, String peso, BigDecimal precoVenda,
                   BigDecimal precoCusto, BigDecimal margemLucro, BigDecimal percentualIcmsCst, BigDecimal percentualIcms,
                   BigDecimal percentualIcmsRed, BigDecimal percentualIpiCst, BigDecimal percentualIpi, BigDecimal percentualPisCst,
                   BigDecimal percentualPis, BigDecimal percentualCofinsCst, BigDecimal percentualCofins, boolean produtoProduzido) {
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
        this.origemProduto = origemProduto;
        this.categoriaProduto = categoriaProduto;
        this.ean = ean;
        this.extipi = extipi;
        this.tipoDeposito = tipoDeposito;
        this.codigoSituacaoTributaria = codigoSituacaoTributaria;
        this.peso = peso;
        this.precoVenda = precoVenda;
        this.precoCusto = precoCusto;
        this.margemLucro = margemLucro;
        this.percentualIcmsCst = percentualIcmsCst;
        this.percentualIcms = percentualIcms;
        this.percentualIcmsRed = percentualIcmsRed;
        this.percentualIpiCst = percentualIpiCst;
        this.percentualIpi = percentualIpi;
        this.percentualPisCst = percentualPisCst;
        this.percentualPis = percentualPis;
        this.percentualCofinsCst = percentualCofinsCst;
        this.percentualCofins = percentualCofins;
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
