package br.com.codex.v1.domain.rh;

import br.com.codex.v1.domain.dto.CadastroColaboradoresDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
public class CadastroColaboradores implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @Column(length = 100)
    protected String nomeColaborador;
    @Column(length = 25)
    protected String nomeDepartamento;
    @Column(length = 25)
    protected String estadoCivil;
    @Column(length = 70)
    protected String cargo;
    @Column(length = 10)
    protected String numeroMatricula;
    @Column(length = 12)
    protected Date dataContratacao;
    @Column(length = 10)
    protected Date dataDemissao;
    @Column(length = 35)
    protected String situacaoAtual;
    @Column(length = 10)
    protected Date nascimento;
    @Column(length = 15)
    protected String numeroCelular;
    @Column(length = 40)
    protected String email;
    @Column(length = 100)
    protected String unidadeContratante;
    @Column(length = 8)
    protected String codCbo;
    @Column(length = 100)
    protected String descricaoCbo;
    @Column(length = 150)
    protected String logradouro;
    @Column(length = 60)
    protected String bairro;
    @Column(length = 60)
    protected String cidade;
    @Column(length = 10)
    protected String cep;
    @Column(length = 2)
    protected String uf;
    @Column(length = 50)
    protected String complemento;
    @Column(length = 10)
    protected String sexo;
    @Column(length = 15)
    protected String carteiraSus;
    @Column(length = 5)
    protected String deficiencia;
    @Column(length = 20)
    protected String qualDeficiencia;
    @Column(length = 50)
    protected String conjugeNome;
    @Column(length = 2)
    protected int filhos;
    @Column(length = 10)
    protected String habilitacao;
    @Column(length = 20)
    protected String tituloEleitor;
    @Column(length = 6)
    protected String zonaEleitor;
    @Column(length = 6)
    protected String secaoEleitor;
    @Column(length = 20)
    protected String numeroReservista;
    @Column(length = 6)
    protected String numeroCarteiraTrabalho;
    @Column(length = 6)
    protected String serieCarteiraTrabalho;
    @Column(length = 2)
    protected String ufCtps;
    @Column(length = 10)
    protected String expedicaoCtps;
    @Column(length = 15)
    protected String rg;
    @Column(length = 2)
    protected String ufRg;
    @Column(length = 11)
    protected Date expedicaoRg;
    @Column(length = 20)
    protected String orgaoEmissorRg;
    @Column(length = 12)
    protected String cpf;
    @Column(length = 25)
    protected String centroCusto;
    @Column(length = 25)
    protected String numeroPis;
    @Column(length = 10)
    protected BigDecimal ultimoSalario;
    @Column(length = 25)
    protected String nomeBanco;
    @Column(length = 6)
    protected String agenciaBanco;
    @Column(length = 10)
    protected String contaBanco;
    @Column(length = 10)
    protected BigDecimal salarioColaborador;
    @Column(length = 75)
    protected String jornada;
    @Column(length = 60)
    protected String nomePai;
    @Column(length = 60)
    protected String nomeMae;
    @Column(length = 30)
    protected String escolaridade;
    @Column(length = 15)
    protected String tipoCertidao;
    @Column(length = 10)
    protected String emissaoCertidao;
    @Column(length = 10)
    protected String livroCertidao;
    @Column(length = 50)
    protected String cartorioCertidao;
    @Column(length = 5)
    protected String folhaCertidao;
    @Column(length = 5)
    protected String termoCertidao;
    @Column(length = 2)
    protected String ufCertidao;
    @Column(length = 50)
    protected String municipioCertidao;
    @Column(length = 30)
    protected String tipoContrato;
    @Column(length = 50)
    protected String modalidadeContrato;
    @Column(length = 5)
    protected int numeroContrato;
    @Column(length = 5)
    protected String numeroCartaoPonto;
    @Column(length = 25)
    protected String contaFgts;
    @Column(length = 3)
    protected int diasExperiencia;
    @Column(length = 15)
    protected Date terminoContrato;
    @Column(length = 250)
    protected String tipoAdmissao;
    @Column(length = 70)
    protected String nomeSindicato;
    @Column(length = 18)
    protected String contribuicaoSindical;
    @Column(length = 10)
    protected String tipoFolha;
    @Column(length = 10)
    protected String tipoSalario;
    @Column(length = 30)
    protected String formaPagamento;
    @Column(length = 2)
    protected BigDecimal insalubridade;
    @Column(length = 2)
    protected BigDecimal periculosidade;
    protected BigDecimal horaMes;
    @Column(length = 2)
    protected BigDecimal horaSemana;
    @Column(length = 6)
    protected BigDecimal salarioHora;
    @Column(length = 6)
    protected BigDecimal valeTransporteCusto;
    @Column(length = 10)
    protected BigDecimal pensaoAlimenticia;
    @Column(length = 10)
    protected BigDecimal salarioFamilia;

    public CadastroColaboradores() {
        super();
    }

    public CadastroColaboradores(Long id, String nomeColaborador, String nomeDepartamento, String estadoCivil, String cargo,
                                 String numeroMatricula, Date dataContratacao, Date dataDemissao, String situacaoAtual,
                                 Date nascimento, String numeroCelular, String email, String unidadeContratante,
                                 String codCbo, String descricaoCbo, String logradouro, String bairro, String cidade,
                                 String cep, String uf, String complemento, String sexo, String carteiraSus,
                                 String deficiencia, String qualDeficiencia, String conjugeNome, int filhos,
                                 String habilitacao, String tituloEleitor, String zonaEleitor, String secaoEleitor,
                                 String numeroReservista, String numeroCarteiraTrabalho, String serieCarteiraTrabalho,
                                 String ufCtps, String expedicaoCtps, String rg, String ufRg, Date expedicaoRg,
                                 String orgaoEmissorRg, String cpf, String centroCusto, String numeroPis, BigDecimal ultimoSalario,
                                 String nomeBanco, String agenciaBanco, String contaBanco, BigDecimal salarioColaborador,
                                 String jornada, String nomePai, String nomeMae, String escolaridade, String tipoCertidao,
                                 String emissaoCertidao, String livroCertidao, String cartorioCertidao, String folhaCertidao,
                                 String termoCertidao, String ufCertidao, String municipioCertidao, String tipoContrato,
                                 String modalidadeContrato, int numeroContrato, String numeroCartaoPonto, String contaFgts,
                                 int diasExperiencia, Date terminoContrato, String tipoAdmissao, String nomeSindicato,
                                 String contribuicaoSindical, String tipoFolha, String tipoSalario, String formaPagamento,
                                 BigDecimal insalubridade, BigDecimal periculosidade, BigDecimal horaMes, BigDecimal horaSemana,
                                 BigDecimal salarioHora, BigDecimal valeTransporteCusto, BigDecimal pensaoAlimenticia,
                                 BigDecimal salarioFamilia) {
        this.id = id;
        this.nomeColaborador = nomeColaborador;
        this.nomeDepartamento = nomeDepartamento;
        this.estadoCivil = estadoCivil;
        this.cargo = cargo;
        this.numeroMatricula = numeroMatricula;
        this.dataContratacao = dataContratacao;
        this.dataDemissao = dataDemissao;
        this.situacaoAtual = situacaoAtual;
        this.nascimento = nascimento;
        this.numeroCelular = numeroCelular;
        this.email = email;
        this.unidadeContratante = unidadeContratante;
        this.codCbo = codCbo;
        this.descricaoCbo = descricaoCbo;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.cidade = cidade;
        this.cep = cep;
        this.uf = uf;
        this.complemento = complemento;
        this.sexo = sexo;
        this.carteiraSus = carteiraSus;
        this.deficiencia = deficiencia;
        this.qualDeficiencia = qualDeficiencia;
        this.conjugeNome = conjugeNome;
        this.filhos = filhos;
        this.habilitacao = habilitacao;
        this.tituloEleitor = tituloEleitor;
        this.zonaEleitor = zonaEleitor;
        this.secaoEleitor = secaoEleitor;
        this.numeroReservista = numeroReservista;
        this.numeroCarteiraTrabalho = numeroCarteiraTrabalho;
        this.serieCarteiraTrabalho = serieCarteiraTrabalho;
        this.ufCtps = ufCtps;
        this.expedicaoCtps = expedicaoCtps;
        this.rg = rg;
        this.ufRg = ufRg;
        this.expedicaoRg = expedicaoRg;
        this.orgaoEmissorRg = orgaoEmissorRg;
        this.cpf = cpf;
        this.centroCusto = centroCusto;
        this.numeroPis = numeroPis;
        this.ultimoSalario = ultimoSalario;
        this.nomeBanco = nomeBanco;
        this.agenciaBanco = agenciaBanco;
        this.contaBanco = contaBanco;
        this.salarioColaborador = salarioColaborador;
        this.jornada = jornada;
        this.nomePai = nomePai;
        this.nomeMae = nomeMae;
        this.escolaridade = escolaridade;
        this.tipoCertidao = tipoCertidao;
        this.emissaoCertidao = emissaoCertidao;
        this.livroCertidao = livroCertidao;
        this.cartorioCertidao = cartorioCertidao;
        this.folhaCertidao = folhaCertidao;
        this.termoCertidao = termoCertidao;
        this.ufCertidao = ufCertidao;
        this.municipioCertidao = municipioCertidao;
        this.tipoContrato = tipoContrato;
        this.modalidadeContrato = modalidadeContrato;
        this.numeroContrato = numeroContrato;
        this.numeroCartaoPonto = numeroCartaoPonto;
        this.contaFgts = contaFgts;
        this.diasExperiencia = diasExperiencia;
        this.terminoContrato = terminoContrato;
        this.tipoAdmissao = tipoAdmissao;
        this.nomeSindicato = nomeSindicato;
        this.contribuicaoSindical = contribuicaoSindical;
        this.tipoFolha = tipoFolha;
        this.tipoSalario = tipoSalario;
        this.formaPagamento = formaPagamento;
        this.insalubridade = insalubridade;
        this.periculosidade = periculosidade;
        this.horaMes = horaMes;
        this.horaSemana = horaSemana;
        this.salarioHora = salarioHora;
        this.valeTransporteCusto = valeTransporteCusto;
        this.pensaoAlimenticia = pensaoAlimenticia;
        this.salarioFamilia = salarioFamilia;
    }

    public CadastroColaboradores(CadastroColaboradoresDto obj) {
        this.id = obj.getId();
        this.nomeColaborador = obj.getNomeColaborador();
        this.nomeDepartamento = obj.getNomeDepartamento();
        this.estadoCivil = obj.getEstadoCivil();
        this.cargo = obj.getCargo();
        this.numeroMatricula = obj.getNumeroMatricula();
        this.dataContratacao = obj.getDataContratacao();
        this.dataDemissao = obj.getDataDemissao();
        this.situacaoAtual = obj.getSituacaoAtual();
        this.nascimento = obj.getNascimento();
        this.numeroCelular = obj.getNumeroCelular();
        this.email = obj.getEmail();
        this.unidadeContratante = obj.getUnidadeContratante();
        this.codCbo = obj.getCodCbo();
        this.descricaoCbo = obj.getDescricaoCbo();
        this.logradouro = obj.getLogradouro();
        this.bairro = obj.getBairro();
        this.cidade = obj.getCidade();
        this.cep = obj.getCep();
        this.uf = obj.getUf();
        this.complemento = obj.getComplemento();
        this.sexo = obj.getSexo();
        this.carteiraSus = obj.getCarteiraSus();
        this.deficiencia = obj.getDeficiencia();
        this.qualDeficiencia = obj.getQualDeficiencia();
        this.conjugeNome = obj.getConjugeNome();
        this.filhos = obj.getFilhos();
        this.habilitacao = obj.getHabilitacao();
        this.tituloEleitor = obj.getTituloEleitor();
        this.zonaEleitor = obj.getZonaEleitor();
        this.secaoEleitor = obj.getSecaoEleitor();
        this.numeroReservista = obj.getNumeroReservista();
        this.numeroCarteiraTrabalho = obj.getNumeroCarteiraTrabalho();
        this.serieCarteiraTrabalho = obj.getSerieCarteiraTrabalho();
        this.ufCtps = obj.getUfCtps();
        this.expedicaoCtps = obj.getExpedicaoCtps();
        this.rg = obj.getRg();
        this.ufRg = obj.getUfRg();
        this.expedicaoRg = obj.getExpedicaoRg();
        this.orgaoEmissorRg = obj.getOrgaoEmissorRg();
        this.cpf = obj.getCpf();
        this.centroCusto = obj.getCentroCusto();
        this.numeroPis = obj.getNumeroPis();
        this.ultimoSalario = obj.getUltimoSalario();
        this.nomeBanco = obj.getNomeBanco();
        this.agenciaBanco = obj.getAgenciaBanco();
        this.contaBanco = obj.getContaBanco();
        this.salarioColaborador = obj.getSalarioColaborador();
        this.jornada = obj.getJornada();
        this.nomePai = obj.getNomePai();
        this.nomeMae = obj.getNomeMae();
        this.escolaridade = obj.getEscolaridade();
        this.tipoCertidao = obj.getTipoCertidao();
        this.emissaoCertidao = obj.getEmissaoCertidao();
        this.livroCertidao = obj.getLivroCertidao();
        this.cartorioCertidao = obj.getCartorioCertidao();
        this.folhaCertidao = obj.getFolhaCertidao();
        this.termoCertidao = obj.getTermoCertidao();
        this.ufCertidao = obj.getUfCertidao();
        this.municipioCertidao = obj.getMunicipioCertidao();
        this.tipoContrato = obj.getTipoContrato();
        this.modalidadeContrato = obj.getModalidadeContrato();
        this.numeroContrato = obj.getNumeroContrato();
        this.numeroCartaoPonto = obj.getNumeroCartaoPonto();
        this.contaFgts = obj.getContaFgts();
        this.diasExperiencia = obj.getDiasExperiencia();
        this.terminoContrato = obj.getTerminoContrato();
        this.tipoAdmissao = obj.getTipoAdmissao();
        this.nomeSindicato = obj.getNomeSindicato();
        this.contribuicaoSindical = obj.getContribuicaoSindical();
        this.tipoFolha = obj.getTipoFolha();
        this.tipoSalario = obj.getTipoSalario();
        this.formaPagamento = obj.getFormaPagamento();
        this.insalubridade = obj.getInsalubridade();
        this.periculosidade = obj.getPericulosidade();
        this.horaMes = obj.getHoraMes();
        this.horaSemana = obj.getHoraSemana();
        this.salarioHora = obj.getSalarioHora();
        this.valeTransporteCusto = obj.getValeTransporteCusto();
        this.pensaoAlimenticia = obj.getPensaoAlimenticia();
        this.salarioFamilia = obj.getSalarioFamilia();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CadastroColaboradores that = (CadastroColaboradores) o;
        return Objects.equals(id, that.id) && Objects.equals(cpf, that.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cpf);
    }
}
