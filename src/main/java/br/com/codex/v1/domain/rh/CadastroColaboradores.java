package br.com.codex.v1.domain.rh;

import br.com.codex.v1.domain.dto.CadastroColaboradoresDto;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

@Entity
public class CadastroColaboradores implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    @Column(length = 100)
    protected String nomeColaborador;
    @Column(length = 25)
    protected String nomeDepartamento;
    @Column(length = 25)
    protected String estadoCivil;
    @Column(length = 70)
    protected String cargo;
    @Column(length = 6)
    protected int numeroMatricula;
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
    @Column(length = 11)
    protected String rg;
    @Column(length = 2)
    protected String ufRg;
    @Column(length = 11)
    protected Date expedicaoRg;
    @Column(length = 3)
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
    @Column(length = 35)
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

    public CadastroColaboradores(Integer id, String nomeColaborador, String nomeDepartamento, String estadoCivil, String cargo, int numeroMatricula, Date dataContratacao, Date dataDemissao, String situacaoAtual, Date nascimento, String numeroCelular, String email, String unidadeContratante, String codCbo, String descricaoCbo, String logradouro, String bairro, String cidade, String cep, String uf, String complemento, String sexo, String carteiraSus, String deficiencia, String qualDeficiencia, String conjugeNome, int filhos, String habilitacao, String tituloEleitor, String zonaEleitor, String secaoEleitor, String numeroReservista, String numeroCarteiraTrabalho, String serieCarteiraTrabalho, String ufCtps, String expedicaoCtps, String rg, String ufRg, Date expedicaoRg, String orgaoEmissorRg, String cpf, String centroCusto, String numeroPis, BigDecimal ultimoSalario, String nomeBanco, String agenciaBanco, String contaBanco, BigDecimal salarioColaborador, String jornada, String nomePai, String nomeMae, String escolaridade, String tipoCertidao, String emissaoCertidao, String livroCertidao, String cartorioCertidao, String folhaCertidao, String termoCertidao, String ufCertidao, String municipioCertidao, String tipoContrato, String modalidadeContrato, int numeroContrato, String numeroCartaoPonto, String contaFgts, int diasExperiencia, Date terminoContrato, String tipoAdmissao, String nomeSindicato, String contribuicaoSindical, String tipoFolha, String tipoSalario, String formaPagamento, BigDecimal insalubridade, BigDecimal periculosidade, BigDecimal horaMes, BigDecimal horaSemana, BigDecimal salarioHora, BigDecimal valeTransporteCusto, BigDecimal pensaoAlimenticia, BigDecimal salarioFamilia) {
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeColaborador() {
        return nomeColaborador;
    }

    public void setNomeColaborador(String nomeColaborador) {
        this.nomeColaborador = nomeColaborador;
    }

    public String getNomeDepartamento() {
        return nomeDepartamento;
    }

    public void setNomeDepartamento(String nomeDepartamento) {
        this.nomeDepartamento = nomeDepartamento;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public int getNumeroMatricula() {
        return numeroMatricula;
    }

    public void setNumeroMatricula(int numeroMatricula) {
        this.numeroMatricula = numeroMatricula;
    }

    public Date getDataContratacao() {
        return dataContratacao;
    }

    public void setDataContratacao(Date dataContratacao) {
        this.dataContratacao = dataContratacao;
    }

    public Date getDataDemissao() {
        return dataDemissao;
    }

    public void setDataDemissao(Date dataDemissao) {
        this.dataDemissao = dataDemissao;
    }

    public String getSituacaoAtual() {
        return situacaoAtual;
    }

    public void setSituacaoAtual(String situacaoAtual) {
        this.situacaoAtual = situacaoAtual;
    }

    public Date getNascimento() {
        return nascimento;
    }

    public void setNascimento(Date nascimento) {
        this.nascimento = nascimento;
    }

    public String getNumeroCelular() {
        return numeroCelular;
    }

    public void setNumeroCelular(String numeroCelular) {
        this.numeroCelular = numeroCelular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUnidadeContratante() {
        return unidadeContratante;
    }

    public void setUnidadeContratante(String unidadeContratante) {
        this.unidadeContratante = unidadeContratante;
    }

    public String getCodCbo() {
        return codCbo;
    }

    public void setCodCbo(String codCbo) {
        this.codCbo = codCbo;
    }

    public String getDescricaoCbo() {
        return descricaoCbo;
    }

    public void setDescricaoCbo(String descricaoCbo) {
        this.descricaoCbo = descricaoCbo;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getCarteiraSus() {
        return carteiraSus;
    }

    public void setCarteiraSus(String carteiraSus) {
        this.carteiraSus = carteiraSus;
    }

    public String getDeficiencia() {
        return deficiencia;
    }

    public void setDeficiencia(String deficiencia) {
        this.deficiencia = deficiencia;
    }

    public String getQualDeficiencia() {
        return qualDeficiencia;
    }

    public void setQualDeficiencia(String qualDeficiencia) {
        this.qualDeficiencia = qualDeficiencia;
    }

    public String getConjugeNome() {
        return conjugeNome;
    }

    public void setConjugeNome(String conjugeNome) {
        this.conjugeNome = conjugeNome;
    }

    public int getFilhos() {
        return filhos;
    }

    public void setFilhos(int filhos) {
        this.filhos = filhos;
    }

    public String getHabilitacao() {
        return habilitacao;
    }

    public void setHabilitacao(String habilitacao) {
        this.habilitacao = habilitacao;
    }

    public String getTituloEleitor() {
        return tituloEleitor;
    }

    public void setTituloEleitor(String tituloEleitor) {
        this.tituloEleitor = tituloEleitor;
    }

    public String getZonaEleitor() {
        return zonaEleitor;
    }

    public void setZonaEleitor(String zonaEleitor) {
        this.zonaEleitor = zonaEleitor;
    }

    public String getSecaoEleitor() {
        return secaoEleitor;
    }

    public void setSecaoEleitor(String secaoEleitor) {
        this.secaoEleitor = secaoEleitor;
    }

    public String getNumeroReservista() {
        return numeroReservista;
    }

    public void setNumeroReservista(String numeroReservista) {
        this.numeroReservista = numeroReservista;
    }

    public String getNumeroCarteiraTrabalho() {
        return numeroCarteiraTrabalho;
    }

    public void setNumeroCarteiraTrabalho(String numeroCarteiraTrabalho) {
        this.numeroCarteiraTrabalho = numeroCarteiraTrabalho;
    }

    public String getSerieCarteiraTrabalho() {
        return serieCarteiraTrabalho;
    }

    public void setSerieCarteiraTrabalho(String serieCarteiraTrabalho) {
        this.serieCarteiraTrabalho = serieCarteiraTrabalho;
    }

    public String getUfCtps() {
        return ufCtps;
    }

    public void setUfCtps(String ufCtps) {
        this.ufCtps = ufCtps;
    }

    public String getExpedicaoCtps() {
        return expedicaoCtps;
    }

    public void setExpedicaoCtps(String expedicaoCtps) {
        this.expedicaoCtps = expedicaoCtps;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getUfRg() {
        return ufRg;
    }

    public void setUfRg(String ufRg) {
        this.ufRg = ufRg;
    }

    public Date getExpedicaoRg() {
        return expedicaoRg;
    }

    public void setExpedicaoRg(Date expedicaoRg) {
        this.expedicaoRg = expedicaoRg;
    }

    public String getOrgaoEmissorRg() {
        return orgaoEmissorRg;
    }

    public void setOrgaoEmissorRg(String orgaoEmissorRg) {
        this.orgaoEmissorRg = orgaoEmissorRg;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCentroCusto() {
        return centroCusto;
    }

    public void setCentroCusto(String centroCusto) {
        this.centroCusto = centroCusto;
    }

    public String getNumeroPis() {
        return numeroPis;
    }

    public void setNumeroPis(String numeroPis) {
        this.numeroPis = numeroPis;
    }

    public BigDecimal getUltimoSalario() {
        return ultimoSalario;
    }

    public void setUltimoSalario(BigDecimal ultimoSalario) {
        this.ultimoSalario = ultimoSalario;
    }

    public String getNomeBanco() {
        return nomeBanco;
    }

    public void setNomeBanco(String nomeBanco) {
        this.nomeBanco = nomeBanco;
    }

    public String getAgenciaBanco() {
        return agenciaBanco;
    }

    public void setAgenciaBanco(String agenciaBanco) {
        this.agenciaBanco = agenciaBanco;
    }

    public String getContaBanco() {
        return contaBanco;
    }

    public void setContaBanco(String contaBanco) {
        this.contaBanco = contaBanco;
    }

    public BigDecimal getSalarioColaborador() {
        return salarioColaborador;
    }

    public void setSalarioColaborador(BigDecimal salarioColaborador) {
        this.salarioColaborador = salarioColaborador;
    }

    public String getJornada() {
        return jornada;
    }

    public void setJornada(String jornada) {
        this.jornada = jornada;
    }

    public String getNomePai() {
        return nomePai;
    }

    public void setNomePai(String nomePai) {
        this.nomePai = nomePai;
    }

    public String getNomeMae() {
        return nomeMae;
    }

    public void setNomeMae(String nomeMae) {
        this.nomeMae = nomeMae;
    }

    public String getEscolaridade() {
        return escolaridade;
    }

    public void setEscolaridade(String escolaridade) {
        this.escolaridade = escolaridade;
    }

    public String getTipoCertidao() {
        return tipoCertidao;
    }

    public void setTipoCertidao(String tipoCertidao) {
        this.tipoCertidao = tipoCertidao;
    }

    public String getEmissaoCertidao() {
        return emissaoCertidao;
    }

    public void setEmissaoCertidao(String emissaoCertidao) {
        this.emissaoCertidao = emissaoCertidao;
    }

    public String getLivroCertidao() {
        return livroCertidao;
    }

    public void setLivroCertidao(String livroCertidao) {
        this.livroCertidao = livroCertidao;
    }

    public String getCartorioCertidao() {
        return cartorioCertidao;
    }

    public void setCartorioCertidao(String cartorioCertidao) {
        this.cartorioCertidao = cartorioCertidao;
    }

    public String getFolhaCertidao() {
        return folhaCertidao;
    }

    public void setFolhaCertidao(String folhaCertidao) {
        this.folhaCertidao = folhaCertidao;
    }

    public String getTermoCertidao() {
        return termoCertidao;
    }

    public void setTermoCertidao(String termoCertidao) {
        this.termoCertidao = termoCertidao;
    }

    public String getUfCertidao() {
        return ufCertidao;
    }

    public void setUfCertidao(String ufCertidao) {
        this.ufCertidao = ufCertidao;
    }

    public String getMunicipioCertidao() {
        return municipioCertidao;
    }

    public void setMunicipioCertidao(String municipioCertidao) {
        this.municipioCertidao = municipioCertidao;
    }

    public String getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public String getModalidadeContrato() {
        return modalidadeContrato;
    }

    public void setModalidadeContrato(String modalidadeContrato) {
        this.modalidadeContrato = modalidadeContrato;
    }

    public int getNumeroContrato() {
        return numeroContrato;
    }

    public void setNumeroContrato(int numeroContrato) {
        this.numeroContrato = numeroContrato;
    }

    public String getNumeroCartaoPonto() {
        return numeroCartaoPonto;
    }

    public void setNumeroCartaoPonto(String numeroCartaoPonto) {
        this.numeroCartaoPonto = numeroCartaoPonto;
    }

    public String getContaFgts() {
        return contaFgts;
    }

    public void setContaFgts(String contaFgts) {
        this.contaFgts = contaFgts;
    }

    public int getDiasExperiencia() {
        return diasExperiencia;
    }

    public void setDiasExperiencia(int diasExperiencia) {
        this.diasExperiencia = diasExperiencia;
    }

    public Date getTerminoContrato() {
        return terminoContrato;
    }

    public void setTerminoContrato(Date terminoContrato) {
        this.terminoContrato = terminoContrato;
    }

    public String getTipoAdmissao() {
        return tipoAdmissao;
    }

    public void setTipoAdmissao(String tipoAdmissao) {
        this.tipoAdmissao = tipoAdmissao;
    }

    public String getNomeSindicato() {
        return nomeSindicato;
    }

    public void setNomeSindicato(String nomeSindicato) {
        this.nomeSindicato = nomeSindicato;
    }

    public String getContribuicaoSindical() {
        return contribuicaoSindical;
    }

    public void setContribuicaoSindical(String contribuicaoSindical) {
        this.contribuicaoSindical = contribuicaoSindical;
    }

    public String getTipoFolha() {
        return tipoFolha;
    }

    public void setTipoFolha(String tipoFolha) {
        this.tipoFolha = tipoFolha;
    }

    public String getTipoSalario() {
        return tipoSalario;
    }

    public void setTipoSalario(String tipoSalario) {
        this.tipoSalario = tipoSalario;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public BigDecimal getInsalubridade() {
        return insalubridade;
    }

    public void setInsalubridade(BigDecimal insalubridade) {
        this.insalubridade = insalubridade;
    }

    public BigDecimal getPericulosidade() {
        return periculosidade;
    }

    public void setPericulosidade(BigDecimal periculosidade) {
        this.periculosidade = periculosidade;
    }

    public BigDecimal getHoraMes() {
        return horaMes;
    }

    public void setHoraMes(BigDecimal horaMes) {
        this.horaMes = horaMes;
    }

    public BigDecimal getHoraSemana() {
        return horaSemana;
    }

    public void setHoraSemana(BigDecimal horaSemana) {
        this.horaSemana = horaSemana;
    }

    public BigDecimal getSalarioHora() {
        return salarioHora;
    }

    public void setSalarioHora(BigDecimal salarioHora) {
        this.salarioHora = salarioHora;
    }

    public BigDecimal getValeTransporteCusto() {
        return valeTransporteCusto;
    }

    public void setValeTransporteCusto(BigDecimal valeTransporteCusto) {
        this.valeTransporteCusto = valeTransporteCusto;
    }

    public BigDecimal getPensaoAlimenticia() {
        return pensaoAlimenticia;
    }

    public void setPensaoAlimenticia(BigDecimal pensaoAlimenticia) {
        this.pensaoAlimenticia = pensaoAlimenticia;
    }

    public BigDecimal getSalarioFamilia() {
        return salarioFamilia;
    }

    public void setSalarioFamilia(BigDecimal salarioFamilia) {
        this.salarioFamilia = salarioFamilia;
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
