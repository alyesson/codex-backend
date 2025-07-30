package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.financeiro.EmissorBoleto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;
import static br.com.codex.v1.utilitario.MinimizarPalavras.minimizarPalavras;

@Getter
@Setter
public class EmissorBoletoDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Long id;
    protected String aceite;
    @NotBlank(message = "Agência não pode estar em branco")
    protected String agencia;
    protected String bairroCedente;
    protected String bairroSacado;
    protected String bancoCedente;
    @NotBlank(message = "Carteira não pode estar em branco")
    protected String carteira;
    protected String cedente;
    protected String cepCedente;
    protected String cepSacado;
    protected String cidadeCedente;
    protected String cidadeSacado;
    @NotBlank(message = "Conta não pode estar em branco")
    protected String conta;
    @NotBlank(message = "Convênio não pode ficar em branco")
    protected String convenio;
    @NotBlank(message = "Data do vencimento não pode estar em branco")
    protected LocalDate dataVencimento;
    protected String demonstrativo;
    protected String digitoAgencia;
    protected String digitoConta;
    protected String digitoNossoNumero;
    @NotBlank(message = "Documento cedente não pode estar em branco")
    protected String documentoCedente;
    @NotBlank(message = "Documento sacado não pode estar em branco")
    protected String documentoSacado;
    protected LocalDate emissao;
    @NotBlank(message = "Espécie do documento não pode estar em branco")
    protected String especieDocumento;
    @NotBlank(message = "Espécie moeda não pode estar em branco")
    protected String especieMoeda;
    @NotBlank(message = "Instrução não pode estar em branco")
    protected String instrucao;
    protected String logradouroCedente;
    protected String logradouroSacado;
    protected String nossoNumero;
    protected String numeroDocumento;
    protected String sacado;
    protected String ufCedente;
    protected String ufSacado;
    @NotBlank(message = "Valor do boleto não pode estar em branco")
    protected BigDecimal valorBoleto;

    public EmissorBoletoDto() {
        super();
    }

    public EmissorBoletoDto(EmissorBoleto obj) {
        this.id = obj.getId();
        this.aceite = obj.getAceite();
        this.agencia = obj.getAgencia(); // Corrigido para usar obj.getAgencia()
        this.bairroCedente = obj.getBairroCedente();
        this.bairroSacado = obj.getBairroSacado();
        this.bancoCedente = obj.getBancoCedente();
        this.carteira = obj.getCarteira();
        this.cedente = obj.getCedente();
        this.cepCedente = obj.getCepCedente();
        this.cepSacado = obj.getCepSacado();
        this.cidadeCedente = obj.getCidadeCedente();
        this.cidadeSacado = obj.getCidadeSacado();
        this.conta = obj.getConta();
        this.convenio = obj.getConvenio();
        this.dataVencimento = obj.getDataVencimento();
        this.demonstrativo = obj.getDemonstrativo();
        this.digitoAgencia = obj.getDigitoAgencia();
        this.digitoConta = obj.getDigitoConta();
        this.digitoNossoNumero = obj.getDigitoNossoNumero();
        this.documentoCedente = obj.getDocumentoCedente();
        this.documentoSacado = obj.getDocumentoSacado();
        this.emissao = obj.getEmissao();
        this.especieDocumento = obj.getEspecieDocumento();
        this.especieMoeda = obj.getEspecieMoeda();
        this.instrucao = obj.getInstrucao();
        this.logradouroCedente = obj.getLogradouroCedente();
        this.logradouroSacado = obj.getLogradouroSacado();
        this.nossoNumero = obj.getNossoNumero();
        this.numeroDocumento = obj.getNumeroDocumento();
        this.sacado = obj.getSacado();
        this.ufCedente = obj.getUfCedente();
        this.ufSacado = obj.getUfSacado();
        this.valorBoleto = obj.getValorBoleto();
    }

    public void setInstrucao(String instrucao) {
        this.instrucao = minimizarPalavras(instrucao);
    }

    public void setDemonstrativo(String demonstrativo) {
        this.demonstrativo = minimizarPalavras(demonstrativo);
    }

    public void setLogradouroSacado(String logradouroSacado) {
        this.logradouroSacado = capitalizarPalavras(logradouroSacado);
    }

    public void setLogradouroCedente(String logradouroCedente) {
        this.logradouroCedente = capitalizarPalavras(logradouroCedente);
    }

    public void setCedente(String cedente) {
        this.cedente = capitalizarPalavras(cedente);
    }

    public void setSacado(String sacado) {
        this.sacado = capitalizarPalavras(sacado);
    }
}
