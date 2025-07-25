package br.com.codex.v1.domain.fiscal;

import br.com.codex.v1.domain.dto.NotaFiscalDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
public class NotaFiscal implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ------------ GRUPO IDE (Identificação) ------------

    @Column(length = 15)
    private String cepEmitente;

    @Column(length = 8)
    private String codigoUf;

    @Column(length = 8)
    private String codigoNf;

    @Column(length = 175)
    private String cnaeEmitente;

    @Column(length = 8)
    private String codigoMunicipioEmitente;

    @Column(length = 45)
    private String dhSaidaEntrada;

    @Column(length = 45)
    private String documentoEmitente;

    @Column(length = 15)
    private LocalDate emissao;

    @Column(length = 4)
    private Integer finalidadeEmissao;

    @Column(length = 4)
    private Integer consumidorFinal;

    @Column(length = 4)
    private String presencaComprador;

    @Column(length = 2)
    private String localDestino;

    @Column(length = 45)
    private String modelo;

    @Column (length = 200)
    private String naturezaOperacao;

    @Column(length = 10)
    private String numero;

    @Column(length = 5)
    private String serie;

    @Column(length = 5)
    private String tipo;

    @Column(length = 5)
    private String formaPagamento;

    @Column(length = 5)
    private String tipoAmbiente;

    @Column(length = 45)
    private String indicadorPresenca;

    @Column(length = 2)
    private Integer indicadorFinal;

    @Column(length = 2)
    private String indicadorIntermediario;

    @Column(length = 45)
    private String inscricaoEstadualEmitente;

    @Column(length = 15)
    private Integer inscricaoEstadualStEmitente;

    @Column(length = 15)
    private Integer inscricaoMunicipalEmitente;

    @Column(length = 150)
    private String razaoSocialEmitente;

    @Column(length = 150)
    private String nomeFantasiaEmitente;

    @Column(length = 4)
    private Integer regimeTributarioEmitente;

    @Column(length = 45)
    private String nomeMunicipioEmitente;

    @Column(length = 150)
    private String bairroEmitente;

    @Column(length = 45)
    private String telefoneEmitente;

    @Column (length = 200)
    private String logradouroEmitente;

    @Column(length = 15)
    private String numeroEnderecoEmitente;

    @Column(length = 8)
    private String ufEmitente;

    @Column(length = 150)
    private String razaoSocialDestinatario;

    @Column(length = 45)
    private String documentoDestinatario;

    @Column(length = 5)
    private Integer indicadorInscricaoEstadualDestinatario;

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

    @Column(length = 65)
    private String emailDestinatario;

    @Column(length = 4)
    private Integer codigoPaisDestinatario;

    @Column(length = 55)
    private String paisDestinatario;
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
    private BigDecimal valorPagamento;
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
    private String pesoLiquido;
    private String pesoBruto;
    private Integer quantidadeVolumes;

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

    /* ------------ RELACIONAMENTO ------------ */
    @OneToMany(mappedBy = "notaFiscal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotaFiscalItem> itens;

    @OneToMany(mappedBy = "notaFiscal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotaFiscalDuplicatas> duplicatas;

    public NotaFiscal() {
        super();
    }

    public NotaFiscal(Long id, String codigoUf, String codigoNf, String naturezaOperacao, String localDestino, String modelo, String serie, String numero, LocalDate emissao,
                      Integer finalidadeEmissao, Integer consumidorFinal, String presencaComprador, String dhSaidaEntrada, String tipo, String formaPagamento, String tipoAmbiente, String indicadorPresenca, Integer indicadorFinal,
                      String indicadorIntermediario, String razaoSocialEmitente, String nomeFantasiaEmitente, String documentoEmitente, String inscricaoEstadualEmitente, Integer inscricaoEstadualStEmitente, Integer inscricaoMunicipalEmitente,
                      String cnaeEmitente, Integer regimeTributarioEmitente, String cepEmitente, String codigoMunicipioEmitente, String nomeMunicipioEmitente, String bairroEmitente,
                      String telefoneEmitente, String logradouroEmitente, String numeroEnderecoEmitente, String ufEmitente, String razaoSocialDestinatario, String documentoDestinatario,
                      Integer indicadorInscricaoEstadualDestinatario, String inscricaoEstadualDestinatario, String cepDestinatario, String codigoMunicipioDestinatario,
                      String nomeMunicipioDestinatario, String bairroDestinatario, String telefoneDestinatario, String logradouroDestinatario, String numeroEnderecoDestinatario,
                      String ufDestinatario, String emailDestinatario, Integer codigoPaisDestinatario , String paisDestinatario, BigDecimal valorBaseCalculo, BigDecimal valorIcms,
                      BigDecimal valorIcmsDesonerado, BigDecimal valorFcp, BigDecimal valorBaseCalculoSt, BigDecimal valorSt, BigDecimal valorFcpSt, BigDecimal valorFcpStRetido,
                      BigDecimal valorProdutos, BigDecimal valorFrete, BigDecimal valorSeguro, BigDecimal valorDesconto, BigDecimal valorIi, BigDecimal valorIpi, BigDecimal valorPagamento,
                      BigDecimal valorIpiDevolucao, BigDecimal valorPis, BigDecimal valorCofins, BigDecimal valorOutros, BigDecimal valorTotal, String modalidadeFrete,
                      String cnpjTransportador, String nomeTransportador, String enderecoTransportador, String municipioTransportador, String pesoLiquido, String pesoBruto, Integer quantidadeVolumes, String numeroFatura, BigDecimal valorOriginalFatura,
                      BigDecimal valorDescontoFatura, BigDecimal valorLiquidoFatura, String informacaoAdicionalFisco, String informacaoAdicionalContribuinte, String chave, String cstat,
                      String numeroProtocolo, String dataHoraProtocolo, String motivoProtocolo) {
        this.id = id;
        this.codigoUf = codigoUf;
        this.codigoNf = codigoNf;
        this.naturezaOperacao = naturezaOperacao;
        this.localDestino = localDestino;
        this.modelo = modelo;
        this.serie = serie;
        this.numero = numero;
        this.emissao = emissao;
        this.finalidadeEmissao = finalidadeEmissao;
        this.consumidorFinal = consumidorFinal;
        this.presencaComprador = presencaComprador;
        this.dhSaidaEntrada = dhSaidaEntrada;
        this.tipo = tipo;
        this.formaPagamento = formaPagamento;
        this.tipoAmbiente = tipoAmbiente;
        this.indicadorPresenca = indicadorPresenca;
        this.indicadorFinal = indicadorFinal;
        this.indicadorIntermediario = indicadorIntermediario;
        this.razaoSocialEmitente = razaoSocialEmitente;
        this.nomeFantasiaEmitente = nomeFantasiaEmitente;
        this.documentoEmitente = documentoEmitente;
        this.inscricaoEstadualEmitente = inscricaoEstadualEmitente;
        this.inscricaoEstadualStEmitente = inscricaoEstadualStEmitente;
        this.inscricaoMunicipalEmitente = inscricaoMunicipalEmitente;
        this.cnaeEmitente = cnaeEmitente;
        this.regimeTributarioEmitente = regimeTributarioEmitente;
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
        this.indicadorInscricaoEstadualDestinatario = indicadorInscricaoEstadualDestinatario;
        this.inscricaoEstadualDestinatario = inscricaoEstadualDestinatario;
        this.cepDestinatario = cepDestinatario;
        this.codigoMunicipioDestinatario = codigoMunicipioDestinatario;
        this.nomeMunicipioDestinatario = nomeMunicipioDestinatario;
        this.bairroDestinatario = bairroDestinatario;
        this.telefoneDestinatario = telefoneDestinatario;
        this.logradouroDestinatario = logradouroDestinatario;
        this.numeroEnderecoDestinatario = numeroEnderecoDestinatario;
        this.ufDestinatario = ufDestinatario;
        this.emailDestinatario = emailDestinatario;
        this.codigoPaisDestinatario = codigoPaisDestinatario;
        this.paisDestinatario = paisDestinatario;
        this.pesoLiquido = pesoLiquido;
        this.pesoBruto = pesoBruto;
        this.quantidadeVolumes = quantidadeVolumes;
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
        this.valorPagamento = valorPagamento;
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
    }

    public NotaFiscal(NotaFiscalDto obj) {
        this.id = obj.getId();
        this.codigoUf = obj.getCodigoUf();
        this.codigoNf = obj.getCodigoNf();
        this.naturezaOperacao = obj.getNaturezaOperacao();
        this.localDestino = obj.getLocalDestino();
        this.modelo = obj.getModelo();
        this.serie = obj.getSerie();
        this.numero = obj.getNumero();
        this.emissao = obj.getEmissao();
        this.finalidadeEmissao = obj.getFinalidadeEmissao();
        this.consumidorFinal = obj.getConsumidorFinal();
        this.presencaComprador = obj.getPresencaComprador();
        this.dhSaidaEntrada = obj.getDhSaidaEntrada();
        this.tipo = obj.getTipo();
        this.formaPagamento = obj.getFormaPagamento();
        this.tipoAmbiente = obj.getTipoAmbiente();
        this.indicadorPresenca = obj.getIndicadorPresenca();
        this.indicadorFinal = obj.getIndicadorFinal();
        this.indicadorIntermediario = obj.getIndicadorIntermediario();
        this.razaoSocialEmitente = obj.getRazaoSocialEmitente();
        this.nomeFantasiaEmitente = obj.getNomeFantasiaEmitente();
        this.documentoEmitente = obj.getDocumentoEmitente();
        this.inscricaoEstadualEmitente = obj.getInscricaoEstadualEmitente();
        this.inscricaoEstadualStEmitente = obj.getInscricaoEstadualStEmitente();
        this.inscricaoMunicipalEmitente = obj.getInscricaoMunicipalEmitente();
        this.cnaeEmitente = obj.getCnaeEmitente();
        this.regimeTributarioEmitente = obj.getRegimeTributarioEmitente();
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
        this.indicadorInscricaoEstadualDestinatario = obj.getIndicadorInscricaoEstadualDestinatario();
        this.inscricaoEstadualDestinatario = obj.getInscricaoEstadualDestinatario();
        this.cepDestinatario = obj.getCepDestinatario();
        this.codigoMunicipioDestinatario = obj.getCodigoMunicipioDestinatario();
        this.nomeMunicipioDestinatario = obj.getNomeMunicipioDestinatario();
        this.bairroDestinatario = obj.getBairroDestinatario();
        this.telefoneDestinatario = obj.getTelefoneDestinatario();
        this.logradouroDestinatario = obj.getLogradouroDestinatario();
        this.numeroEnderecoDestinatario = obj.getNumeroEnderecoDestinatario();
        this.ufDestinatario = obj.getUfDestinatario();
        this.emailDestinatario = obj.getEmailDestinatario();
        this.codigoPaisDestinatario = obj.getCodigoPaisDestinatario() != null ? Integer.valueOf(obj.getCodigoPaisDestinatario()) : null;
        this.paisDestinatario = obj.getPaisDestinatario();
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
        this.valorPagamento = obj.getValorPagamento();
        this.valorCofins = obj.getValorCofins();
        this.valorOutros = obj.getValorOutros();
        this.valorTotal = obj.getValorTotal();
        this.modalidadeFrete = obj.getModalidadeFrete();
        this.cnpjTransportador = obj.getCnpjTransportador();
        this.nomeTransportador = obj.getNomeTransportador();
        this.enderecoTransportador = obj.getEnderecoTransportador();
        this.municipioTransportador = obj.getMunicipioTransportador();
        this.pesoLiquido = obj.getPesoLiquido();
        this.pesoBruto = obj.getPesoBruto();
        this.quantidadeVolumes = obj.getQuantidadeVolumes();
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
        this.itens = obj.getItens() != null ? obj.getItens().stream().map(itemDto -> {
                    NotaFiscalItem item = new NotaFiscalItem(itemDto);
                    item.setNotaFiscal(this);
                    return item;
                }).toList() : null;

        this.duplicatas = obj.getDuplicatas() != null ? obj.getDuplicatas().stream().map(dupDto -> {
                    NotaFiscalDuplicatas dup = new NotaFiscalDuplicatas(dupDto);
                    dup.setNotaFiscal(this);
                    return dup;
                }).toList() : null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        NotaFiscal that = (NotaFiscal) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}