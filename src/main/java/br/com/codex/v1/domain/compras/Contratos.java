package br.com.codex.v1.domain.compras;

import br.com.codex.v1.domain.dto.ContratosDto;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

@Entity
public class Contratos implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    protected String nomeContrato;
    protected Date inicioContrato;
    protected Date terminoContrato;
    protected String tipoPessoa;
    protected String numeroCnpj;
    protected String relacao;
    protected String razaoSocial;
    protected String diaVenceParcela;
    protected String tipoContrato;
    protected BigDecimal valorContrato;
    protected BigDecimal pis;
    protected BigDecimal ipi;
    protected BigDecimal icms;
    protected BigDecimal cofins;
    protected BigDecimal iss;
    protected BigDecimal frete;
    protected BigDecimal valorDesconto;
    protected BigDecimal valorLiquido;
    protected String renegociado;
    protected String dataRenegociacao;
    protected String observacao;
    @Lob
    protected byte[] arquivo;

    public Contratos() {
        super();
    }

    public Contratos(Integer id, String nomeContrato, Date inicioContrato, Date terminoContrato, String tipoPessoa, String numeroCnpj, String relacao, String razaoSocial, String diaVenceParcela, String tipoContrato, BigDecimal valorContrato, BigDecimal pis, BigDecimal ipi, BigDecimal icms, BigDecimal cofins, BigDecimal iss, BigDecimal frete, BigDecimal valorDesconto, BigDecimal valorLiquido, String renegociado, String dataRenegociacao, String observacao, byte[] arquivo) {
        this.id = id;
        this.nomeContrato = nomeContrato;
        this.inicioContrato = inicioContrato;
        this.terminoContrato = terminoContrato;
        this.tipoPessoa = tipoPessoa;
        this.numeroCnpj = numeroCnpj;
        this.relacao = relacao;
        this.razaoSocial = razaoSocial;
        this.diaVenceParcela = diaVenceParcela;
        this.tipoContrato = tipoContrato;
        this.valorContrato = valorContrato;
        this.pis = pis;
        this.ipi = ipi;
        this.icms = icms;
        this.cofins = cofins;
        this.iss = iss;
        this.frete = frete;
        this.valorDesconto = valorDesconto;
        this.valorLiquido = valorLiquido;
        this.renegociado = renegociado;
        this.dataRenegociacao = dataRenegociacao;
        this.observacao = observacao;
        this.arquivo = arquivo;
    }

    public Contratos(ContratosDto obj) {
        this.id = obj.getId();
        this.nomeContrato = obj.getNomeContrato();
        this.inicioContrato = obj.getInicioContrato();
        this.terminoContrato = obj.getTerminoContrato();
        this.tipoPessoa = obj.getTipoPessoa();
        this.numeroCnpj = obj.getNumeroCnpj();
        this.relacao = obj.getRelacao();
        this.razaoSocial = obj.getRazaoSocial();
        this.diaVenceParcela = obj.getDiaVenceParcela();
        this.tipoContrato = obj.getTipoContrato();
        this.valorContrato = obj.getValorContrato();
        this.pis = obj.getPis();
        this.ipi = obj.getIpi();
        this.icms = obj.getIcms();
        this.cofins = obj.getCofins();
        this.iss = obj.getIss();
        this.frete = obj.getFrete();
        this.valorDesconto = obj.getValorDesconto();
        this.valorLiquido = obj.getValorLiquido();
        this.renegociado = obj.getRenegociado();
        this.dataRenegociacao = obj.getDataRenegociacao();
        this.observacao = obj.getObservacao();
        this.arquivo = obj.getArquivo();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeContrato() {
        return nomeContrato;
    }

    public void setNomeContrato(String nomeContrato) {
        this.nomeContrato = nomeContrato;
    }

    public Date getInicioContrato() {
        return inicioContrato;
    }

    public void setInicioContrato(Date inicioContrato) {
        this.inicioContrato = inicioContrato;
    }

    public Date getTerminoContrato() {
        return terminoContrato;
    }

    public void setTerminoContrato(Date terminoContrato) {
        this.terminoContrato = terminoContrato;
    }

    public String getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(String tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public String getNumeroCnpj() {
        return numeroCnpj;
    }

    public void setNumeroCnpj(String numeroCnpj) {
        this.numeroCnpj = numeroCnpj;
    }

    public String getRelacao() {
        return relacao;
    }

    public void setRelacao(String relacao) {
        this.relacao = relacao;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getDiaVenceParcela() {
        return diaVenceParcela;
    }

    public void setDiaVenceParcela(String diaVenceParcela) {
        this.diaVenceParcela = diaVenceParcela;
    }

    public String getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public BigDecimal getValorContrato() {
        return valorContrato;
    }

    public void setValorContrato(BigDecimal valorContrato) {
        this.valorContrato = valorContrato;
    }

    public BigDecimal getPis() {
        return pis;
    }

    public void setPis(BigDecimal pis) {
        this.pis = pis;
    }

    public BigDecimal getIpi() {
        return ipi;
    }

    public void setIpi(BigDecimal ipi) {
        this.ipi = ipi;
    }

    public BigDecimal getIcms() {
        return icms;
    }

    public void setIcms(BigDecimal icms) {
        this.icms = icms;
    }

    public BigDecimal getCofins() {
        return cofins;
    }

    public void setCofins(BigDecimal cofins) {
        this.cofins = cofins;
    }

    public BigDecimal getIss() {
        return iss;
    }

    public void setIss(BigDecimal iss) {
        this.iss = iss;
    }

    public BigDecimal getFrete() {
        return frete;
    }

    public void setFrete(BigDecimal frete) {
        this.frete = frete;
    }

    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public BigDecimal getValorLiquido() {
        return valorLiquido;
    }

    public void setValorLiquido(BigDecimal valorLiquido) {
        this.valorLiquido = valorLiquido;
    }

    public String getRenegociado() {
        return renegociado;
    }

    public void setRenegociado(String renegociado) {
        this.renegociado = renegociado;
    }

    public String getDataRenegociacao() {
        return dataRenegociacao;
    }

    public void setDataRenegociacao(String dataRenegociacao) {
        this.dataRenegociacao = dataRenegociacao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public byte[] getArquivo() {
        return arquivo;
    }

    public void setArquivo(byte[] arquivo) {
        this.arquivo = arquivo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contratos contratos = (Contratos) o;
        return Objects.equals(id, contratos.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
