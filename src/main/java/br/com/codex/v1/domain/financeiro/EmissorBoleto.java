package br.com.codex.v1.domain.financeiro;

import br.com.codex.v1.domain.dto.EmissorBoletoDto;
import br.com.codex.v1.utilitario.MinimizarPalavras;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;
import static br.com.codex.v1.utilitario.MinimizarPalavras.minimizarPalavras;

@Entity
@Getter
@Setter
public class EmissorBoleto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @Column(length = 10)
    protected String aceite;
    @Column(length = 15)
    protected String agencia;
    @Column(length = 100)
    protected String bairroCedente;
    @Column(length = 100)
    protected String bairroSacado;
    @Column(length = 35)
    protected String bancoCedente;
    @Column(length = 25)
    protected String carteira;
    @Column(length = 60)
    protected String cedente;
    @Column(length = 10)
    protected String cepCedente;
    @Column(length = 10)
    protected String cepSacado;
    @Column(length = 50)
    protected String cidadeCedente;
    @Column(length = 50)
    protected String cidadeSacado;
    @Column(length = 20)
    protected String conta;
    @Column(length = 20)
    protected String convenio;
    @Column(length = 12)
    protected LocalDate dataVencimento;
    @Column(columnDefinition = "TEXT")
    protected String demonstrativo;
    @Column(length = 5)
    protected String digitoAgencia;
    @Column(length = 5)
    protected String digitoConta;
    @Column (length = 5)
    protected String digitoNossoNumero;
    @Column(length = 15)
    protected String documentoCedente;
    @Column(length = 15)
    protected String documentoSacado;
    protected LocalDate emissao;
    @Column(length = 55)
    protected String especieDocumento;
    @Column(length = 25)
    protected String especieMoeda;
    @Column(columnDefinition = "TEXT")
    protected String instrucao;
    @Column(length = 100)
    protected String logradouroCedente;
    @Column(length = 100)
    protected String logradouroSacado;
    @Column(length = 25)
    protected String nossoNumero;
    @Column(length = 65)
    protected String numeroDocumento;
    @Column(length = 65)
    protected String sacado;
    @Column(length = 5)
    protected String ufCedente;
    @Column(length = 5)
    protected String ufSacado;
    @Column(length = 12)
    protected BigDecimal valorBoleto;

    public EmissorBoleto() {
        super();
    }

    public EmissorBoleto(Long id, String aceite, String agencia, String bairroCedente, String bairroSacado, String bancoCedente,
                         String carteira, String cedente, String cepCedente, String cepSacado, String cidadeCedente, String cidadeSacado,
                         String conta, String convenio, LocalDate dataVencimento, String demonstrativo, String digitoAgencia, String digitoConta, String digitoNossoNumero,
                         String documentoCedente, String documentoSacado, LocalDate emissao, String especieDocumento, String especieMoeda, String instrucao,
                         String logradouroCedente, String logradouroSacado, String nossoNumero, String numeroDocumento, String sacado,
                         String ufCedente, String ufSacado, BigDecimal valorBoleto) {
        this.id = id;
        this.aceite = aceite;
        this.agencia = agencia;
        this.bairroCedente = bairroCedente;
        this.bairroSacado = bairroSacado;
        this.bancoCedente = bancoCedente;
        this.carteira = carteira;
        this.cedente = cedente;
        this.cepCedente = cepCedente;
        this.cepSacado = cepSacado;
        this.cidadeCedente = cidadeCedente;
        this.cidadeSacado = cidadeSacado;
        this.conta = conta;
        this.convenio = convenio;
        this.dataVencimento = dataVencimento;
        this.demonstrativo = demonstrativo;
        this.digitoAgencia = digitoAgencia;
        this.digitoConta = digitoConta;
        this.digitoNossoNumero = digitoNossoNumero;
        this.documentoCedente = documentoCedente;
        this.documentoSacado = documentoSacado;
        this.emissao = emissao;
        this.especieDocumento = especieDocumento;
        this.especieMoeda = especieMoeda;
        this.instrucao = instrucao;
        this.logradouroCedente = logradouroCedente;
        this.logradouroSacado = logradouroSacado;
        this.nossoNumero = nossoNumero;
        this.numeroDocumento = numeroDocumento;
        this.sacado = sacado;
        this.ufCedente = ufCedente;
        this.ufSacado = ufSacado;
        this.valorBoleto = valorBoleto;
    }

    public EmissorBoleto(EmissorBoletoDto obj) {
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
}
