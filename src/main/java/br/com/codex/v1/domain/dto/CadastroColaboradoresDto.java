package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.rh.CadastroColaboradores;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

public class CadastroColaboradoresDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Integer id;
    @NotNull(message = "Nome do colaborador não pode ficar em branco")
    protected String nomeColaborador;
    @NotNull(message = "Departamento não pode ficar em branco")
    protected String nomeDepartamento;
    @NotNull(message = "Estado civil não pode ficar em branco")
    protected String estadoCivil;
    @NotNull(message = "Cargo não pode ficar em branco")
    protected String cargo;
    @NotNull(message = "Matrícula não pode ficar em branco")
    protected int numeroMatricula;
    @NotNull(message = "Data da contratação não pode ficar em branco")
    protected Date dataContratacao;
    protected Date dataDemissao;
    @NotNull(message = "Campo situação atual não pode ficar em branco")
    protected String situacaoAtual;
    @NotNull(message = "Data de nascimento não pode ficar em branco")
    protected Date nascimento;
    @NotNull(message = "Número de celular não pode ficar em branco")
    protected String numeroCelular;
    protected String email;
    @NotNull(message = "Unidade contratante não pode ficar em branco")
    protected String unidadeContratante;
    @NotNull(message = "Código do cbo não pode ficar em branco")
    protected String codCbo;
    @NotNull(message = "Descrição do Cbo não pode ficar em branco")
    protected String descricaoCbo;
    @NotNull(message = "Logradouro não pode ficar em branco")
    protected String logradouro;
    @NotNull(message = "Bairro não pode ficar em branco")
    protected String bairro;
    @NotNull(message = "Cidade não pode ficar em branco")
    protected String cidade;
    @NotNull(message = "Cep não pode ficar em branco")
    protected String cep;
    @NotNull(message = "Unidade federativa não pode ficar em branco")
    protected String uf;
    protected String complemento;
    @NotNull(message = "Campo sexo não pode ficar em branco")
    protected String sexo;
    protected String carteiraSus;
    protected String deficiencia;
    protected String qualDeficiencia;
    protected String conjugeNome;
    protected int filhos;
    protected String habilitacao;
    @NotNull(message = "Título de eleitor não pode ficar em branco")
    protected String tituloEleitor;
    @NotNull(message = "Zona eleitoral não pode ficar em branco não pode ficar em branco")
    protected String zonaEleitor;
    @NotNull(message = "Seção eleitoral não pode ficar em branco não pode ficar em branco")
    protected String secaoEleitor;
    protected String numeroReservista;
    @NotNull(message = "Número da carteira de trabalho não pode ficar em branco não pode ficar em branco")
    protected String numeroCarteiraTrabalho;
    protected String serieCarteiraTrabalho;
    protected String ufCtps;
    protected String expedicaoCtps;
    @NotNull(message = "Número Rg não pode ficar em branco não pode ficar em branco")
    protected String rg;
    protected String ufRg;
    protected Date expedicaoRg;
    @NotNull(message = "Órgão emissor do rg não pode ficar em branco não pode ficar em branco")
    protected String orgaoEmissorRg;
    @NotNull(message = "Cpf não pode ficar em branco não pode ficar em branco")
    protected String cpf;
    @NotNull(message = "Centro de custo não pode ficar em branco não pode ficar em branco")
    protected String centroCusto;
    @NotNull(message = "Número do PIS não pode ficar em branco não pode ficar em branco")
    protected String numeroPis;
    protected BigDecimal ultimoSalario;
    @NotNull(message = "Nome do banco não pode ficar em branco não pode ficar em branco")
    protected String nomeBanco;
    @NotNull(message = "Agência bancária não pode ficar em branco não pode ficar em branco")
    protected String agenciaBanco;
    @NotNull(message = "Conta bancária não pode ficar em branco não pode ficar em branco")
    protected String contaBanco;
    @NotNull(message = "Salário do colaborador não pode ficar em branco não pode ficar em branco")
    protected BigDecimal salarioColaborador;
    @NotNull(message = "Jornada de trabalho não pode ficar em branco não pode ficar em branco")
    protected String jornada;
    protected String nomePai;
    protected String nomeMae;
    @NotNull(message = "Escolaridade não pode ficar em branco não pode ficar em branco")
    protected String escolaridade;
    @NotNull(message = "Tipo de certidão não pode ficar em branco não pode ficar em branco")
    protected String tipoCertidao;
    protected String emissaoCertidao;
    protected String livroCertidao;
    protected String cartorioCertidao;
    protected String folhaCertidao;
    protected String termoCertidao;
    protected String ufCertidao;
    protected String municipioCertidao;
    @NotNull(message = "Tipo de contrato não pode ficar em branco não pode ficar em branco")
    protected String tipoContrato;
    @NotNull(message = "Modalidade do contrato não pode ficar em branco não pode ficar em branco")
    protected String modalidadeContrato;
    @NotNull(message = "Número do contrato não pode ficar em branco não pode ficar em branco")
    protected int numeroContrato;
    protected String numeroCartaoPonto;
    @NotNull(message = "Conta do FGTS não pode ficar em branco não pode ficar em branco")
    protected String contaFgts;
    @NotNull(message = "Dias de experiência não pode ficar em branco não pode ficar em branco")
    protected int diasExperiencia;
    protected Date terminoContrato;
    @NotNull(message = "Tipo de admissão não pode ficar em branco não pode ficar em branco")
    protected String tipoAdmissao;
    protected String nomeSindicato;
    protected String contribuicaoSindical;
    @NotNull(message = "Tipo de folha não pode ficar em branco não pode ficar em branco")
    protected String tipoFolha;
    @NotNull(message = "Tipo de salário não pode ficar em branco não pode ficar em branco")
    protected String tipoSalario;
    @NotNull(message = "Forma de pagamento não pode ficar em branco não pode ficar em branco")
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
        this.nomeColaborador = capitalizarPalavras(nomeColaborador);
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
        this.cargo = capitalizarPalavras(cargo);
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
        this.descricaoCbo = capitalizarPalavras(descricaoCbo);
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = capitalizarPalavras(logradouro);
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = capitalizarPalavras(bairro);
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = capitalizarPalavras(cidade);
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
        this.complemento = capitalizarPalavras(complemento);
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
        this.qualDeficiencia = capitalizarPalavras(qualDeficiencia);
    }

    public String getConjugeNome() {
        return conjugeNome;
    }

    public void setConjugeNome(String conjugeNome) {
        this.conjugeNome = capitalizarPalavras(conjugeNome);
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
        this.municipioCertidao = capitalizarPalavras(municipioCertidao);
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
}
