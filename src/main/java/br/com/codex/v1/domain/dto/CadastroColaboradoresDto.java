package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.rh.CadastroColaboradores;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

@Getter
@Setter
public class CadastroColaboradoresDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Long id;
    @NotBlank(message = "Nome do colaborador não pode ficar em branco")
    protected String nomeColaborador;
    @NotBlank(message = "Departamento não pode ficar em branco")
    protected String nomeDepartamento;
    @NotBlank(message = "Estado civil não pode ficar em branco")
    protected String estadoCivil;
    @NotBlank(message = "Cargo não pode ficar em branco")
    protected String cargo;
    @NotNull(message = "Matrícula não pode ficar em branco")
    protected int numeroMatricula;
    @NotNull(message = "Data da contratação não pode ficar em branco")
    protected Date dataContratacao;
    protected Date dataDemissao;
    @NotBlank(message = "Campo situação atual não pode ficar em branco")
    protected String situacaoAtual;
    @NotNull(message = "Data de nascimento não pode ficar em branco")
    protected Date nascimento;
    @NotBlank(message = "Número de celular não pode ficar em branco")
    protected String numeroCelular;
    protected String email;
    @NotBlank(message = "Unidade contratante não pode ficar em branco")
    protected String unidadeContratante;
    @NotBlank(message = "Código do cbo não pode ficar em branco")
    protected String codCbo;
    @NotBlank(message = "Descrição do Cbo não pode ficar em branco")
    protected String descricaoCbo;
    @NotBlank(message = "Logradouro não pode ficar em branco")
    protected String logradouro;
    @NotBlank(message = "Bairro não pode ficar em branco")
    protected String bairro;
    @NotBlank(message = "Cidade não pode ficar em branco")
    protected String cidade;
    @NotBlank(message = "Cep não pode ficar em branco")
    protected String cep;
    @NotBlank(message = "Unidade federativa não pode ficar em branco")
    protected String uf;
    protected String complemento;
    @NotBlank(message = "Campo sexo não pode ficar em branco")
    protected String sexo;
    protected String carteiraSus;
    protected String deficiencia;
    protected String qualDeficiencia;
    protected String conjugeNome;
    protected int filhos;
    protected String habilitacao;
    @NotBlank(message = "Título de eleitor não pode ficar em branco")
    protected String tituloEleitor;
    @NotBlank(message = "Zona eleitoral não pode ficar em branco não pode ficar em branco")
    protected String zonaEleitor;
    @NotBlank(message = "Seção eleitoral não pode ficar em branco não pode ficar em branco")
    protected String secaoEleitor;
    protected String numeroReservista;
    protected String numeroCarteiraTrabalho;
    protected String serieCarteiraTrabalho;
    protected String ufCtps;
    protected String expedicaoCtps;
    @NotBlank(message = "Número Rg não pode ficar em branco não pode ficar em branco")
    protected String rg;
    protected String ufRg;
    protected Date expedicaoRg;
    @NotBlank(message = "Órgão emissor do rg não pode ficar em branco não pode ficar em branco")
    protected String orgaoEmissorRg;
    @NotBlank(message = "Cpf não pode ficar em branco não pode ficar em branco")
    protected String cpf;
    @NotBlank(message = "Centro de custo não pode ficar em branco não pode ficar em branco")
    protected String centroCusto;
    protected String numeroPis;
    protected BigDecimal ultimoSalario;
    @NotBlank(message = "Nome do banco não pode ficar em branco não pode ficar em branco")
    protected String nomeBanco;
    @NotBlank(message = "Agência bancária não pode ficar em branco não pode ficar em branco")
    protected String agenciaBanco;
    @NotBlank(message = "Conta bancária não pode ficar em branco não pode ficar em branco")
    protected String contaBanco;
    @NotNull(message = "Salário do colaborador não pode ficar em branco não pode ficar em branco")
    protected BigDecimal salarioColaborador;
    @NotNull(message = "Jornada de trabalho não pode ficar em branco não pode ficar em branco")
    protected String jornada;
    protected String nomePai;
    protected String nomeMae;
    @NotBlank(message = "Escolaridade não pode ficar em branco não pode ficar em branco")
    protected String escolaridade;
    @NotBlank(message = "Tipo de certidão não pode ficar em branco não pode ficar em branco")
    protected String tipoCertidao;
    protected String emissaoCertidao;
    protected String livroCertidao;
    protected String cartorioCertidao;
    protected String folhaCertidao;
    protected String termoCertidao;
    protected String ufCertidao;
    protected String municipioCertidao;
    @NotBlank(message = "Tipo de contrato não pode ficar em branco não pode ficar em branco")
    protected String tipoContrato;
    @NotBlank(message = "Modalidade do contrato não pode ficar em branco não pode ficar em branco")
    protected String modalidadeContrato;
    @NotNull(message = "Número do contrato não pode ficar em branco não pode ficar em branco")
    protected int numeroContrato;
    protected String numeroCartaoPonto;
    @NotBlank(message = "Conta do FGTS não pode ficar em branco não pode ficar em branco")
    protected String contaFgts;
    protected int diasExperiencia;
    protected Date terminoContrato;
    @NotBlank(message = "Tipo de admissão não pode ficar em branco não pode ficar em branco")
    protected String tipoAdmissao;
    protected String nomeSindicato;
    protected String contribuicaoSindical;
    @NotBlank(message = "Tipo de folha não pode ficar em branco não pode ficar em branco")
    protected String tipoFolha;
    @NotBlank(message = "Tipo de salário não pode ficar em branco não pode ficar em branco")
    protected String tipoSalario;
    @NotBlank(message = "Forma de pagamento não pode ficar em branco não pode ficar em branco")
    protected String formaPagamento;
    protected BigDecimal insalubridade;
    protected BigDecimal periculosidade;
    @NotNull(message = "Horas por mês não pode ficar em branco não pode ficar em branco")
    protected BigDecimal horaMes;
    @NotNull(message = "Horas por semana não pode ficar em branco não pode ficar em branco")
    protected BigDecimal horaSemana;
    @NotNull(message = "Salário hora não pode ficar em branco não pode ficar em branco")
    protected BigDecimal salarioHora;
    @NotNull(message = "Vale transporte não pode ficar em branco não pode ficar em branco")
    protected BigDecimal valeTransporteCusto;
    protected BigDecimal pensaoAlimenticia;
    protected BigDecimal salarioFamilia;


    public CadastroColaboradoresDto() {
        super();
    }

    public CadastroColaboradoresDto(CadastroColaboradores obj) {
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

    public void setNomeColaborador(String nomeColaborador) {
        this.nomeColaborador = capitalizarPalavras(nomeColaborador);
    }

    public void setCargo(String cargo) {
        this.cargo = capitalizarPalavras(cargo);
    }

    public void setDescricaoCbo(String descricaoCbo) {
        this.descricaoCbo = capitalizarPalavras(descricaoCbo);
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = capitalizarPalavras(logradouro);
    }

    public void setBairro(String bairro) {
        this.bairro = capitalizarPalavras(bairro);
    }

    public void setCidade(String cidade) {
        this.cidade = capitalizarPalavras(cidade);
    }

    public void setComplemento(String complemento) {
        this.complemento = capitalizarPalavras(complemento);
    }

    public void setQualDeficiencia(String qualDeficiencia) {
        this.qualDeficiencia = capitalizarPalavras(qualDeficiencia);
    }

    public void setConjugeNome(String conjugeNome) {
        this.conjugeNome = capitalizarPalavras(conjugeNome);
    }

    public void setCartorioCertidao(String cartorioCertidao) {
        this.cartorioCertidao = capitalizarPalavras(cartorioCertidao);
    }

    public void setMunicipioCertidao(String municipioCertidao) {
        this.municipioCertidao = capitalizarPalavras(municipioCertidao);}
}
