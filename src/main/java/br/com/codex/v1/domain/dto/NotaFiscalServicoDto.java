package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.fiscal.NotaFiscalServico;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class NotaFiscalServicoDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private LocalDateTime dataEmissao;
    private LocalDate competencia;
    private String codigoVerificacao;
    private Integer numeroNfse;
    private Integer numeroRps;
    private Integer numeroNfseSubstituida;
    private String localPrestacao;

    // Dados do Prestador
    private String prestadorRazaoSocial;
    private String prestadorNomeFantasia;
    private String prestadorCnpjCpf;
    private String prestadorInscricaoMunicipal;
    private String prestadorMunicipio;
    private String prestadorEndereco;
    private String prestadorComplemento;
    private String prestadorTelefone;
    private String prestadorEmail;

    // Dados do Tomador
    private String tomadorRazaoSocial;
    private String tomadorCnpjCpf;
    private String tomadorInscricaoMunicipal;
    private String tomadorMunicipio;
    private String tomadorEndereco;
    private String tomadorComplemento;
    private String tomadorTelefone;
    private String tomadorEmail;

    // Dados do Serviço
    private String discriminacaoServicos;
    private String codigoServico;
    private String descricaoServico;
    private String codigoObra;
    private String codigoArt;

    // Valores e Tributos
    private BigDecimal valorPis;
    private BigDecimal valorCofins;
    private BigDecimal valorIr;
    private BigDecimal valorInss;
    private BigDecimal valorCsll;
    private BigDecimal valorServicos;
    private BigDecimal descontoIncondicionado;
    private BigDecimal descontoCondicionado;
    private BigDecimal retencoesFederais;
    private BigDecimal outrasRetencoes;
    private BigDecimal issRetido;
    private BigDecimal valorLiquido;
    private Integer naturezaOperacao;
    private Integer regimeEspecialTributacao;
    private Integer opcaoSimplesNacional;
    private Integer incentivadorCultural;
    private BigDecimal deducoesPermitidas;
    private BigDecimal baseCalculo;
    private BigDecimal aliquota;
    private BigDecimal valorIss;
    private Boolean issReter;

    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public NotaFiscalServicoDto() {
    }

    public NotaFiscalServicoDto(NotaFiscalServico obj) {
        this.id = obj.getId();
        this.dataEmissao = obj.getDataEmissao();
        this.competencia = obj.getCompetencia();
        this.codigoVerificacao = obj.getCodigoVerificacao();
        this.numeroNfse = obj.getNumeroNfse();
        this.numeroRps = obj.getNumeroRps();
        this.numeroNfseSubstituida = obj.getNumeroNfseSubstituida();
        this.localPrestacao = obj.getLocalPrestacao();

        this.prestadorRazaoSocial = obj.getPrestadorRazaoSocial();
        this.prestadorNomeFantasia = obj.getPrestadorNomeFantasia();
        this.prestadorCnpjCpf = obj.getPrestadorCnpjCpf();
        this.prestadorInscricaoMunicipal = obj.getPrestadorInscricaoMunicipal();
        this.prestadorMunicipio = obj.getPrestadorMunicipio();
        this.prestadorEndereco = obj.getPrestadorEndereco();
        this.prestadorComplemento = obj.getPrestadorComplemento();
        this.prestadorTelefone = obj.getPrestadorTelefone();
        this.prestadorEmail = obj.getPrestadorEmail();

        this.tomadorRazaoSocial = obj.getTomadorRazaoSocial();
        this.tomadorCnpjCpf = obj.getTomadorCnpjCpf();
        this.tomadorInscricaoMunicipal = obj.getTomadorInscricaoMunicipal();
        this.tomadorMunicipio = obj.getTomadorMunicipio();
        this.tomadorEndereco = obj.getTomadorEndereco();
        this.tomadorComplemento = obj.getTomadorComplemento();
        this.tomadorTelefone = obj.getTomadorTelefone();
        this.tomadorEmail = obj.getTomadorEmail();

        this.discriminacaoServicos = obj.getDiscriminacaoServicos();
        this.codigoServico = obj.getCodigoServico();
        this.descricaoServico = obj.getDescricaoServico();
        this.codigoObra = obj.getCodigoObra();
        this.codigoArt = obj.getCodigoArt();

        this.valorPis = obj.getValorPis();
        this.valorCofins = obj.getValorCofins();
        this.valorIr = obj.getValorIr();
        this.valorInss = obj.getValorInss();
        this.valorCsll = obj.getValorCsll();
        this.valorServicos = obj.getValorServicos();
        this.descontoIncondicionado = obj.getDescontoIncondicionado();
        this.descontoCondicionado = obj.getDescontoCondicionado();
        this.retencoesFederais = obj.getRetencoesFederais();
        this.outrasRetencoes = obj.getOutrasRetencoes();
        this.issRetido = obj.getIssRetido();
        this.valorLiquido = obj.getValorLiquido();
        this.naturezaOperacao = obj.getNaturezaOperacao();
        this.regimeEspecialTributacao = obj.getRegimeEspecialTributacao();
        this.opcaoSimplesNacional = obj.getOpcaoSimplesNacional();
        this.incentivadorCultural = obj.getIncentivadorCultural();
        this.deducoesPermitidas = obj.getDeducoesPermitidas();
        this.baseCalculo = obj.getBaseCalculo();
        this.aliquota = obj.getAliquota();
        this.valorIss = obj.getValorIss();
        this.issReter = obj.getIssReter();

        this.dataCriacao = obj.getDataCriacao();
        this.dataAtualizacao = obj.getDataAtualizacao();
    }
}
