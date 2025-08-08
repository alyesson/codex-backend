package br.com.codex.v1.domain.fiscal;

import br.com.codex.v1.domain.dto.ImportarXmlDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
public class ImportarXml implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String xml;

    @Column(length = 8)
    private String codigoUf;

    @Column(length = 8)
    private String codigoNf;

    @Column
    private String naturezaOperacao;

    @Column(length = 45)
    private String modelo;

    @Column(length = 5)
    private String serie;

    @Column(length = 15)
    private String numero;

    @Column(length = 15)
    private LocalDate emissao;

    @Column(length = 45)
    private String dhSaidaEntrada;

    @Column(length = 45)
    private String tipo;

    @Column(length = 45)
    private String indicadorPresenca;

    @Column(length = 150)
    private String razaoSocialEmitente;

    @Column(length = 150)
    private String nomeFantasiaEmitente;

    @Column(length = 45)
    private String documentoEmitente;

    @Column(length = 45)
    private String inscricaoEstadualEmitente;

    @Column(length = 15)
    private String cepEmitente;

    @Column(length = 8)
    private String codigoMunicipioEmitente;

    @Column(length = 45)
    private String nomeMunicipioEmitente;

    @Column(length = 150)
    private String bairroEmitente;

    @Column(length = 45)
    private String telefoneEmitente;

    @Column
    private String logradouroEmitente;

    @Column(length = 15)
    private String numeroEnderecoEmitente;

    @Column(length = 8)
    private String ufEmitente;

    @Column(length = 150)
    private String razaoSocialDestinatario;

    @Column(length = 45)
    private String documentoDestinatario;

    @Column(length = 45)
    private String inscricaoEstadualDestinatario;

    @Column(length = 15)
    private String cepDestinatario;

    @Column(length = 8)
    private String codigoMunicipioDestinatario;

    @Column(length = 100)
    private String nomeMunicipioDestinatario;

    private String bairroDestinatario;

    @Column(length = 25)
    private String telefoneDestinatario;

    private String logradouroDestinatario;

    @Column(length = 5)
    private String numeroEnderecoDestinatario;

    @Column(length = 15)
    private String ufDestinatario;
    private BigDecimal valorBaseCalculo;
    private BigDecimal valorIcms;
    private BigDecimal valorIcmsDesonerado;
    private BigDecimal valorFcp;
    private BigDecimal valorBaseCalculoSt;
    private BigDecimal valorSt;
    private BigDecimal valorFcpSt;
    private BigDecimal valorFcpStRetido;
    private BigDecimal valorProdutos;
    private BigDecimal valorFrete;
    private BigDecimal valorSeguro;
    private BigDecimal valorDesconto;
    private BigDecimal valorIi;
    private BigDecimal valorIpi;
    private BigDecimal valorIpiDevolucao;
    private BigDecimal valorPis;
    private BigDecimal valorCofins;
    private BigDecimal valorOutros;
    private BigDecimal valorTotal;

    @Column(length = 5)
    private String modalidadeFrete;

    @Column(length = 45)
    private String cnpjTransportador;

    @Column(length = 150)
    private String nomeTransportador;
    private String enderecoTransportador;
    private String municipioTransportador;

    @Column(length = 15)
    private String numeroFatura;
    private BigDecimal valorOriginalFatura;
    private BigDecimal valorDescontoFatura;
    private BigDecimal valorLiquidoFatura;

    @Lob
    private String informacaoAdicionalFisco;

    @Lob
    private String informacaoAdicionalContribuinte;

    @Column(length = 46)
    private String chave;

    @Column(length = 45)
    private String cstat;

    @Column(length = 45)
    private String numeroProtocolo;

    @Column(length = 45)
    private String dataHoraProtocolo;

    @Lob
    private String motivoProtocolo;

    @Column(length = 15)
    private Date dataImportacao;

    public ImportarXml() {
        super();
    }

    public ImportarXml(Long id, String xml, String codigoUf, String codigoNf, String naturezaOperacao, String modelo, String serie, String numero, LocalDate emissao, String dhSaidaEntrada, String tipo, String indicadorPresenca, String razaoSocialEmitente, String nomeFantasiaEmitente, String documentoEmitente, String inscricaoEstadualEmitente, String cepEmitente, String codigoMunicipioEmitente, String nomeMunicipioEmitente, String bairroEmitente, String telefoneEmitente, String logradouroEmitente, String numeroEnderecoEmitente, String ufEmitente, String razaoSocialDestinatario, String documentoDestinatario, String inscricaoEstadualDestinatario, String cepDestinatario, String codigoMunicipioDestinatario, String nomeMunicipioDestinatario, String bairroDestinatario, String telefoneDestinatario, String logradouroDestinatario, String numeroEnderecoDestinatario, String ufDestinatario, BigDecimal valorBaseCalculo, BigDecimal valorIcms, BigDecimal valorIcmsDesonerado, BigDecimal valorFcp, BigDecimal valorBaseCalculoSt, BigDecimal valorSt, BigDecimal valorFcpSt, BigDecimal valorFcpStRetido, BigDecimal valorProdutos, BigDecimal valorFrete, BigDecimal valorSeguro, BigDecimal valorDesconto, BigDecimal valorIi, BigDecimal valorIpi, BigDecimal valorIpiDevolucao, BigDecimal valorPis, BigDecimal valorCofins, BigDecimal valorOutros, BigDecimal valorTotal, String modalidadeFrete, String cnpjTransportador, String nomeTransportador, String enderecoTransportador, String municipioTransportador, String numeroFatura, BigDecimal valorOriginalFatura, BigDecimal valorDescontoFatura, BigDecimal valorLiquidoFatura, String informacaoAdicionalFisco, String informacaoAdicionalContribuinte, String chave, String cstat, String numeroProtocolo, String dataHoraProtocolo, String motivoProtocolo, Date dataImportacao) {
        this.id = id;
        this.xml = xml;
        this.codigoUf = codigoUf;
        this.codigoNf = codigoNf;
        this.naturezaOperacao = naturezaOperacao;
        this.modelo = modelo;
        this.serie = serie;
        this.numero = numero;
        this.emissao = emissao;
        this.dhSaidaEntrada = dhSaidaEntrada;
        this.tipo = tipo;
        this.indicadorPresenca = indicadorPresenca;
        this.razaoSocialEmitente = razaoSocialEmitente;
        this.nomeFantasiaEmitente = nomeFantasiaEmitente;
        this.documentoEmitente = documentoEmitente;
        this.inscricaoEstadualEmitente = inscricaoEstadualEmitente;
        this.cepEmitente = cepEmitente;
        this.codigoMunicipioEmitente = codigoMunicipioEmitente;
        this.nomeMunicipioEmitente = nomeMunicipioEmitente;
        this.bairroEmitente = bairroEmitente;
        this.telefoneEmitente = telefoneEmitente;
        this.logradouroEmitente = logradouroEmitente;
        this.numeroEnderecoEmitente = numeroEnderecoEmitente;
        this.ufEmitente = ufEmitente;
        this.razaoSocialDestinatario = razaoSocialDestinatario;
        this.documentoDestinatario = documentoDestinatario;
        this.inscricaoEstadualDestinatario = inscricaoEstadualDestinatario;
        this.cepDestinatario = cepDestinatario;
        this.codigoMunicipioDestinatario = codigoMunicipioDestinatario;
        this.nomeMunicipioDestinatario = nomeMunicipioDestinatario;
        this.bairroDestinatario = bairroDestinatario;
        this.telefoneDestinatario = telefoneDestinatario;
        this.logradouroDestinatario = logradouroDestinatario;
        this.numeroEnderecoDestinatario = numeroEnderecoDestinatario;
        this.ufDestinatario = ufDestinatario;
        this.valorBaseCalculo = valorBaseCalculo;
        this.valorIcms = valorIcms;
        this.valorIcmsDesonerado = valorIcmsDesonerado;
        this.valorFcp = valorFcp;
        this.valorBaseCalculoSt = valorBaseCalculoSt;
        this.valorSt = valorSt;
        this.valorFcpSt = valorFcpSt;
        this.valorFcpStRetido = valorFcpStRetido;
        this.valorProdutos = valorProdutos;
        this.valorFrete = valorFrete;
        this.valorSeguro = valorSeguro;
        this.valorDesconto = valorDesconto;
        this.valorIi = valorIi;
        this.valorIpi = valorIpi;
        this.valorIpiDevolucao = valorIpiDevolucao;
        this.valorPis = valorPis;
        this.valorCofins = valorCofins;
        this.valorOutros = valorOutros;
        this.valorTotal = valorTotal;
        this.modalidadeFrete = modalidadeFrete;
        this.cnpjTransportador = cnpjTransportador;
        this.nomeTransportador = nomeTransportador;
        this.enderecoTransportador = enderecoTransportador;
        this.municipioTransportador = municipioTransportador;
        this.numeroFatura = numeroFatura;
        this.valorOriginalFatura = valorOriginalFatura;
        this.valorDescontoFatura = valorDescontoFatura;
        this.valorLiquidoFatura = valorLiquidoFatura;
        this.informacaoAdicionalFisco = informacaoAdicionalFisco;
        this.informacaoAdicionalContribuinte = informacaoAdicionalContribuinte;
        this.chave = chave;
        this.cstat = cstat;
        this.numeroProtocolo = numeroProtocolo;
        this.dataHoraProtocolo = dataHoraProtocolo;
        this.motivoProtocolo = motivoProtocolo;
        this.dataImportacao = dataImportacao;
    }

    public ImportarXml(ImportarXmlDto obj) {
        this.id = obj.getId();
        this.xml = obj.getXml();
        this.codigoUf = obj.getCodigoUf();
        this.codigoNf = obj.getCodigoNf();
        this.naturezaOperacao = obj.getNaturezaOperacao();
        this.modelo = obj.getModelo();
        this.serie = obj.getSerie();
        this.numero = obj.getNumero();
        this.emissao = obj.getEmissao();
        this.dhSaidaEntrada = obj.getDhSaidaEntrada();
        this.tipo = obj.getTipo();
        this.indicadorPresenca = obj.getIndicadorPresenca();
        this.razaoSocialEmitente = obj.getRazaoSocialEmitente();
        this.nomeFantasiaEmitente = obj.getNomeFantasiaEmitente();
        this.documentoEmitente = obj.getDocumentoEmitente();
        this.inscricaoEstadualEmitente = obj.getInscricaoEstadualEmitente();
        this.cepEmitente = obj.getCepEmitente();
        this.codigoMunicipioEmitente = obj.getCodigoMunicipioEmitente();
        this.nomeMunicipioEmitente = obj.getNomeMunicipioEmitente();
        this.bairroEmitente = obj.getBairroEmitente();
        this.telefoneEmitente = obj.getTelefoneEmitente();
        this.logradouroEmitente = obj.getLogradouroEmitente();
        this.numeroEnderecoEmitente = obj.getNumeroEnderecoEmitente();
        this.ufEmitente = obj.getUfEmitente();
        this.razaoSocialDestinatario = obj.getRazaoSocialDestinatario();
        this.documentoDestinatario = obj.getDocumentoDestinatario();
        this.inscricaoEstadualDestinatario = obj.getInscricaoEstadualDestinatario();
        this.cepDestinatario = obj.getCepDestinatario();
        this.codigoMunicipioDestinatario = obj.getCodigoMunicipioDestinatario();
        this.nomeMunicipioDestinatario = obj.getNomeMunicipioDestinatario();
        this.bairroDestinatario = obj.getBairroDestinatario();
        this.telefoneDestinatario = obj.getTelefoneDestinatario();
        this.logradouroDestinatario = obj.getLogradouroDestinatario();
        this.numeroEnderecoDestinatario = obj.getNumeroEnderecoDestinatario();
        this.ufDestinatario = obj.getUfDestinatario();
        this.valorBaseCalculo = obj.getValorBaseCalculo();
        this.valorIcms = obj.getValorIcms();
        this.valorIcmsDesonerado = obj.getValorIcmsDesonerado();
        this.valorFcp = obj.getValorFcp();
        this.valorBaseCalculoSt = obj.getValorBaseCalculoSt();
        this.valorSt = obj.getValorSt();
        this.valorFcpSt = obj.getValorFcpSt();
        this.valorFcpStRetido = obj.getValorFcpStRetido();
        this.valorProdutos = obj.getValorProdutos();
        this.valorFrete = obj.getValorFrete();
        this.valorSeguro = obj.getValorSeguro();
        this.valorDesconto = obj.getValorDesconto();
        this.valorIi = obj.getValorIi();
        this.valorIpi = obj.getValorIpi();
        this.valorIpiDevolucao = obj.getValorIpiDevolucao();
        this.valorPis = obj.getValorPis();
        this.valorCofins = obj.getValorCofins();
        this.valorOutros = obj.getValorOutros();
        this.valorTotal = obj.getValorTotal();
        this.modalidadeFrete = obj.getModalidadeFrete();
        this.cnpjTransportador = obj.getCnpjTransportador();
        this.nomeTransportador = obj.getNomeTransportador();
        this.enderecoTransportador = obj.getEnderecoTransportador();
        this.municipioTransportador = obj.getMunicipioTransportador();
        this.numeroFatura = obj.getNumeroFatura();
        this.valorOriginalFatura = obj.getValorOriginalFatura();
        this.valorDescontoFatura = obj.getValorDescontoFatura();
        this.valorLiquidoFatura = obj.getValorLiquidoFatura();
        this.informacaoAdicionalFisco = obj.getInformacaoAdicionalFisco();
        this.informacaoAdicionalContribuinte = obj.getInformacaoAdicionalContribuinte();
        this.chave = obj.getChave();
        this.cstat = obj.getCstat();
        this.numeroProtocolo = obj.getNumeroProtocolo();
        this.dataHoraProtocolo = obj.getDataHoraProtocolo();
        this.motivoProtocolo = obj.getMotivoProtocolo();
        this.dataImportacao = obj.getDataImportacao();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImportarXml that = (ImportarXml) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
