package br.com.codex.v1.domain.fiscal;

import br.com.codex.v1.domain.dto.NotaFiscalServicoDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class NotaFiscalServico implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_emissao", length = 15)
    private LocalDateTime dataEmissao;

    @Column(name = "competencia", length = 10)
    private LocalDate competencia;

    @Column(name = "codigo_verificacao", length = 20)
    private String codigoVerificacao;

    @Column(name = "numero_nfse", length = 15)
    private Integer numeroNfse;

    @Column(name = "numero_rps", length = 50)
    private Integer numeroRps;

    @Column(name = "numero_nfse_substituida", length = 50)
    private Integer numeroNfseSubstituida;

    @Column(name = "local_prestacao", length = 100)
    private String localPrestacao;

    // Dados do Prestador
    @Column(name = "prestador_razao_social", length = 150)
    private String prestadorRazaoSocial;

    @Column(name = "prestador_nome_fantasia", length = 100)
    private String prestadorNomeFantasia;

    @Column(name = "prestador_cnpj_cpf", length = 18)
    private String prestadorCnpjCpf;

    @Column(name = "prestador_inscricao_municipal", length = 20)
    private String prestadorInscricaoMunicipal;

    @Column(name = "prestador_municipio", length = 100)
    private String prestadorMunicipio;

    @Column(name = "prestador_endereco", length = 200)
    private String prestadorEndereco;

    @Column(name = "prestador_complemento", length = 100)
    private String prestadorComplemento;

    @Column(name = "prestador_telefone", length = 20)
    private String prestadorTelefone;

    @Column(name = "prestador_email", length = 100)
    private String prestadorEmail;

    // Dados do Tomador
    @Column(name = "tomador_razao_social", length = 150)
    private String tomadorRazaoSocial;

    @Column(name = "tomador_cnpj_cpf", length = 18)
    private String tomadorCnpjCpf;

    @Column(name = "tomador_inscricao_municipal", length = 20)
    private String tomadorInscricaoMunicipal;

    @Column(name = "tomador_municipio", length = 100)
    private String tomadorMunicipio;

    @Column(name = "tomador_endereco", length = 200)
    private String tomadorEndereco;

    @Column(name = "tomador_complemento", length = 100)
    private String tomadorComplemento;

    @Column(name = "tomador_telefone", length = 20)
    private String tomadorTelefone;

    @Column(name = "tomador_email", length = 100)
    private String tomadorEmail;

    // Dados do Serviço
    @Column(name = "discriminacao_servicos", columnDefinition = "TEXT")
    private String discriminacaoServicos;

    @Column(name = "codigo_servico", length = 10)
    private String codigoServico;

    @Column(name = "descricao_servico", length = 200)
    private String descricaoServico;

    @Column(name = "codigo_obra", length = 20)
    private String codigoObra;

    @Column(name = "codigo_art", length = 20)
    private String codigoArt;

    // Valores e Tributos
    @Column(name = "valor_pis", precision = 15, scale = 2)
    private BigDecimal valorPis;

    @Column(name = "valor_cofins", precision = 15, scale = 2)
    private BigDecimal valorCofins;

    @Column(name = "valor_ir", precision = 15, scale = 2)
    private BigDecimal valorIr;

    @Column(name = "valor_inss", precision = 15, scale = 2)
    private BigDecimal valorInss;

    @Column(name = "valor_csll", precision = 15, scale = 2)
    private BigDecimal valorCsll;

    @Column(name = "valor_servicos", precision = 15, scale = 2)
    private BigDecimal valorServicos;

    @Column(name = "desconto_incondicionado", precision = 15, scale = 2)
    private BigDecimal descontoIncondicionado;

    @Column(name = "desconto_condicionado", precision = 15, scale = 2)
    private BigDecimal descontoCondicionado;

    @Column(name = "retencoes_federais", precision = 15, scale = 2)
    private BigDecimal retencoesFederais;

    @Column(name = "outras_retencoes", precision = 15, scale = 2)
    private BigDecimal outrasRetencoes;

    @Column(name = "iss_retido", precision = 15, scale = 2)
    private BigDecimal issRetido;

    @Column(name = "valor_liquido", precision = 15, scale = 2)
    private BigDecimal valorLiquido;

    @Column(name = "natureza_operacao")
    private Integer naturezaOperacao;

    @Column(name = "regime_especial_tributacao")
    private Integer regimeEspecialTributacao;

    @Column(name = "opcao_simples_nacional")
    private Integer opcaoSimplesNacional;

    @Column(name = "incentivador_cultural")
    private Integer incentivadorCultural;

    @Column(name = "deducoes_permitidas", precision = 15, scale = 2)
    private BigDecimal deducoesPermitidas;

    @Column(name = "base_calculo", precision = 15, scale = 2)
    private BigDecimal baseCalculo;

    @Column(name = "aliquota", precision = 5, scale = 4)
    private BigDecimal aliquota;

    @Column(name = "valor_iss", precision = 15, scale = 2)
    private BigDecimal valorIss;

    @Column(name = "iss_reter")
    private Boolean issReter;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "situacao", length = 15)
    private String situacao;

    @Column(name = "justificativa_cancelamento")
    private String justificativaCancelamento;

    @Column(name = "data_cancelamento")
    private LocalDate dataCancelamento;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }

    public NotaFiscalServico() {
        super();
    }

    public NotaFiscalServico(Long id, LocalDateTime dataEmissao, LocalDate competencia, String codigoVerificacao,
                             Integer numeroNfse, Integer numeroRps, Integer numeroNfseSubstituida, String localPrestacao,
                             String prestadorRazaoSocial, String prestadorNomeFantasia, String prestadorCnpjCpf,
                             String prestadorInscricaoMunicipal, String prestadorMunicipio, String prestadorEndereco,
                             String prestadorComplemento, String prestadorTelefone, String prestadorEmail,
                             String tomadorRazaoSocial, String tomadorCnpjCpf, String tomadorInscricaoMunicipal,
                             String tomadorMunicipio, String tomadorEndereco, String tomadorComplemento,
                             String tomadorTelefone, String tomadorEmail, String discriminacaoServicos,
                             String codigoServico, String descricaoServico, String codigoObra, String codigoArt,
                             BigDecimal valorPis, BigDecimal valorCofins, BigDecimal valorIr, BigDecimal valorInss, BigDecimal valorCsll,
                             BigDecimal valorServicos, BigDecimal descontoIncondicionado, BigDecimal descontoCondicionado,
                             BigDecimal retencoesFederais, BigDecimal outrasRetencoes, BigDecimal issRetido,
                             BigDecimal valorLiquido, Integer naturezaOperacao, Integer regimeEspecialTributacao,
                             Integer opcaoSimplesNacional, Integer incentivadorCultural, BigDecimal deducoesPermitidas,
                             BigDecimal baseCalculo, BigDecimal aliquota, BigDecimal valorIss, Boolean issReter,
                             LocalDateTime dataCriacao, LocalDateTime dataAtualizacao, String situacao, String justificativaCancelamento, LocalDate dataCancelamento) {
        this.id = id;
        this.dataEmissao = dataEmissao;
        this.competencia = competencia;
        this.codigoVerificacao = codigoVerificacao;
        this.numeroNfse = numeroNfse;
        this.numeroRps = numeroRps;
        this.numeroNfseSubstituida = numeroNfseSubstituida;
        this.localPrestacao = localPrestacao;
        this.prestadorRazaoSocial = prestadorRazaoSocial;
        this.prestadorNomeFantasia = prestadorNomeFantasia;
        this.prestadorCnpjCpf = prestadorCnpjCpf;
        this.prestadorInscricaoMunicipal = prestadorInscricaoMunicipal;
        this.prestadorMunicipio = prestadorMunicipio;
        this.prestadorEndereco = prestadorEndereco;
        this.prestadorComplemento = prestadorComplemento;
        this.prestadorTelefone = prestadorTelefone;
        this.prestadorEmail = prestadorEmail;
        this.tomadorRazaoSocial = tomadorRazaoSocial;
        this.tomadorCnpjCpf = tomadorCnpjCpf;
        this.tomadorInscricaoMunicipal = tomadorInscricaoMunicipal;
        this.tomadorMunicipio = tomadorMunicipio;
        this.tomadorEndereco = tomadorEndereco;
        this.tomadorComplemento = tomadorComplemento;
        this.tomadorTelefone = tomadorTelefone;
        this.tomadorEmail = tomadorEmail;
        this.discriminacaoServicos = discriminacaoServicos;
        this.codigoServico = codigoServico;
        this.descricaoServico = descricaoServico;
        this.codigoObra = codigoObra;
        this.codigoArt = codigoArt;
        this.valorPis = valorPis;
        this.valorCofins = valorCofins;
        this.valorIr = valorIr;
        this.valorInss = valorInss;
        this.valorCsll = valorCsll;
        this.valorServicos = valorServicos;
        this.descontoIncondicionado = descontoIncondicionado;
        this.descontoCondicionado = descontoCondicionado;
        this.retencoesFederais = retencoesFederais;
        this.outrasRetencoes = outrasRetencoes;
        this.issRetido = issRetido;
        this.valorLiquido = valorLiquido;
        this.naturezaOperacao = naturezaOperacao;
        this.regimeEspecialTributacao = regimeEspecialTributacao;
        this.opcaoSimplesNacional = opcaoSimplesNacional;
        this.incentivadorCultural = incentivadorCultural;
        this.deducoesPermitidas = deducoesPermitidas;
        this.baseCalculo = baseCalculo;
        this.aliquota = aliquota;
        this.valorIss = valorIss;
        this.issReter = issReter;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.situacao = situacao;
        this.justificativaCancelamento = justificativaCancelamento;
        this.dataCancelamento = dataCancelamento;
    }

    public NotaFiscalServico(NotaFiscalServicoDto obj) {
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
        this.situacao = obj.getSituacao();
        this.justificativaCancelamento = obj.getJustificativaCancelamento();
        this.dataCancelamento = obj.getDataCancelamento();
    }
}
