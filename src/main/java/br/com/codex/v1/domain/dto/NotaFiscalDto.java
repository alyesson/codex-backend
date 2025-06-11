package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.contabilidade.NotaFiscal;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class NotaFiscalDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    // ------------ GRUPO IDE (Identificação) ------------
    private String naturezaOperacao;
    private Integer serie;
    private Integer numero;
    private LocalDateTime dataEmissao;
    private LocalDateTime dataEntradaSaida;
    private Integer tipoDocumento; // 0-Entrada, 1-Saída
    private Integer localDestino; // 1-Interna, 2-Interestadual, 3-Exterior
    private Integer municipio;
    private Integer finalidadeEmissao; // 1-Normal, 2-Complementar, etc.
    private Integer consumidorFinal; // 0-Normal, 1-Consumidor final
    private Integer presencaComprador; // 0-Não se aplica, 1-Presencial, etc.
    private Integer indicadorIntermediario; // 0-Sem intermediador, 1-Com intermediador

    // Emitente
    private String cnpjEmitente;
    private String cpfEmitente;
    private String nomeEmitente;
    private String nomeFantasiaEmitente;
    private String logradouroEmitente;
    private String numeroEmitente;
    private String complementoEmitente;
    private String bairroEmitente;
    private String codigoMunicipioEmitente;
    private String municipioEmitente;
    private String ufEmitente;
    private String cepEmitente;
    private String telefoneEmitente;
    private String inscricaoEstadualEmitente;
    private String inscricaoEstadualStEmitente;
    private String inscricaoMunicipalEmitente;
    private String cnaeFiscalEmitente;
    private String regimeTributarioEmitente;

    // Destinatário
    private String cnpjDestinatario;
    private String cpfDestinatario;
    private String nomeDestinatario;
    private String nomeFantasiaDestinatario;
    private String logradouroDestinatario;
    private String numeroDestinatario;
    private String complementoDestinatario;
    private String bairroDestinatario;
    private String codigoMunicipioDestinatario;
    private String municipioDestinatario;
    private String ufDestinatario;
    private String cepDestinatario;
    private String codigoPaisDestinatario;
    private String paisDestinatario;
    private String telefoneDestinatario;
    private Integer indicadorInscricaoEstadualDestinatario;
    private String inscricaoEstadualDestinatario;
    private String inscricaoSuframaDestinatario;
    private String inscricaoMunicipalDestinatario;
    private String emailDestinatario;

    // Totais
    private BigDecimal valorProdutos;
    private BigDecimal valorFrete;
    private BigDecimal valorSeguro;
    private BigDecimal valorDesconto;
    private BigDecimal valorTotalII;
    private BigDecimal valorIPI;
    private BigDecimal valorPIS;
    private BigDecimal valorCOFINS;
    private BigDecimal valorOutrasDespesas;
    private BigDecimal valorTotal;
    private BigDecimal valorTotalTributos;

    // Transporte
    private Integer modalidadeFrete; // 0-por conta do remetente, 1-por conta destinatário, etc.
    private String cnpjTransportador;
    private String placaVeiculo;
    private String ufVeiculo;

    // Informações adicionais
    private String informacoesAdicionaisFisco;
    private String informacoesAdicionaisContribuinte;

    /* ------------ RELACIONAMENTO ------------ */
    private List<NotaFiscalItemDto> itens;
    private List<NotaFiscalDuplicatasDto> duplicatas;

    public NotaFiscalDto() {
        super();
    }

    public NotaFiscalDto(NotaFiscal obj) {
        this.id = obj.getId();
        this.naturezaOperacao = obj.getNaturezaOperacao();
        this.serie = obj.getSerie();
        this.numero = obj.getNumero();
        this.dataEmissao = obj.getDataEmissao();
        this.dataEntradaSaida = obj.getDataEntradaSaida();
        this.tipoDocumento = obj.getTipoDocumento();
        this.localDestino = obj.getLocalDestino();
        this.municipio = obj.getMunicipio();
        this.finalidadeEmissao = obj.getFinalidadeEmissao();
        this.consumidorFinal = obj.getConsumidorFinal();
        this.presencaComprador = obj.getPresencaComprador();
        this.indicadorIntermediario = obj.getIndicadorIntermediario();
        this.cnpjEmitente = obj.getCnpjEmitente();
        this.cpfEmitente = obj.getCpfEmitente();
        this.nomeEmitente = obj.getNomeEmitente();
        this.nomeFantasiaEmitente = obj.getNomeFantasiaEmitente();
        this.logradouroEmitente = obj.getLogradouroEmitente();
        this.numeroEmitente = obj.getNumeroEmitente();
        this.complementoEmitente = obj.getComplementoEmitente();
        this.bairroEmitente = obj.getBairroEmitente();
        this.codigoMunicipioEmitente = obj.getCodigoMunicipioEmitente();
        this.municipioEmitente = obj.getMunicipioEmitente();
        this.ufEmitente = obj.getUfEmitente();
        this.cepEmitente = obj.getCepEmitente();
        this.telefoneEmitente = obj.getTelefoneEmitente();
        this.inscricaoEstadualEmitente = obj.getInscricaoEstadualEmitente();
        this.inscricaoEstadualStEmitente = obj.getInscricaoEstadualStEmitente();
        this.inscricaoMunicipalEmitente = obj.getInscricaoMunicipalEmitente();
        this.cnaeFiscalEmitente = obj.getCnaeFiscalEmitente();
        this.regimeTributarioEmitente = obj.getRegimeTributarioEmitente();
        this.cnpjDestinatario = obj.getCnpjDestinatario();
        this.cpfDestinatario = obj.getCpfDestinatario();
        this.nomeDestinatario = obj.getNomeDestinatario();
        this.nomeFantasiaDestinatario = obj.getNomeFantasiaDestinatario();
        this.logradouroDestinatario = obj.getLogradouroDestinatario();
        this.numeroDestinatario = obj.getNumeroDestinatario();
        this.complementoDestinatario = obj.getComplementoDestinatario();
        this.bairroDestinatario = obj.getBairroDestinatario();
        this.codigoMunicipioDestinatario = obj.getCodigoMunicipioDestinatario();
        this.municipioDestinatario = obj.getMunicipioDestinatario();
        this.ufDestinatario = obj.getUfDestinatario();
        this.cepDestinatario = obj.getCepDestinatario();
        this.codigoPaisDestinatario = obj.getCodigoPaisDestinatario();
        this.paisDestinatario = obj.getPaisDestinatario();
        this.telefoneDestinatario = obj.getTelefoneDestinatario();
        this.indicadorInscricaoEstadualDestinatario = obj.getIndicadorInscricaoEstadualDestinatario();
        this.inscricaoEstadualDestinatario = obj.getInscricaoEstadualDestinatario();
        this.inscricaoSuframaDestinatario = obj.getInscricaoSuframaDestinatario();
        this.inscricaoMunicipalDestinatario = obj.getInscricaoMunicipalDestinatario();
        this.emailDestinatario = obj.getEmailDestinatario();
        this.valorProdutos = obj.getValorProdutos();
        this.valorFrete = obj.getValorFrete();
        this.valorSeguro = obj.getValorSeguro();
        this.valorDesconto = obj.getValorDesconto();
        this.valorTotalII = obj.getValorTotalII();
        this.valorIPI = obj.getValorIPI();
        this.valorPIS = obj.getValorPIS();
        this.valorCOFINS = obj.getValorCOFINS();
        this.valorOutrasDespesas = obj.getValorOutrasDespesas();
        this.valorTotal = obj.getValorTotal();
        this.valorTotalTributos = obj.getValorTotalTributos();
        this.modalidadeFrete = obj.getModalidadeFrete();
        this.cnpjTransportador = obj.getCnpjTransportador();
        this.placaVeiculo = obj.getPlacaVeiculo();
        this.ufVeiculo = obj.getUfVeiculo();
        this.informacoesAdicionaisFisco = obj.getInformacoesAdicionaisFisco();
        this.informacoesAdicionaisContribuinte = obj.getInformacoesAdicionaisContribuinte();

        // Convertendo itens da nota fiscal para DTO
        this.itens = obj.getItens() != null ? obj.getItens().stream().map(NotaFiscalItemDto::new).toList() : null;

        // Convertendo duplicatas da nota fiscal para DTO
        this.duplicatas = obj.getDuplicatas() != null ? obj.getDuplicatas().stream().map(NotaFiscalDuplicatasDto::new).toList() : null;
    }
}