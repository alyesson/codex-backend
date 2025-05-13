package br.com.codex.v1.domain.cadastros;

import br.com.codex.v1.domain.dto.EmpresaDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class Empresa implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String cnpj;
    private String inscricaoEstadual;
    private String inscricaoMunicipal;
    private String inscricaoEstadualSt;
    private String suframa;
    private String cnae;
    private String nomeFantasia;
    private String razaoSocial;
    private String endereco;
    private String complemento;
    private String bairro;
    private String cidade;
    private String cep;
    private String uf;
    private String regimeTributario;
    private String telelefone;
    private String celular;
    private String emailContato;
    private String situacao;
    private String classificacaoFinanceira;
    private String jdbcUrl;// ex: jdbc:mysql://host:3306/empresa_012345
    private String tipoEmpresa;

    public Empresa() {
        super();
    }

    public Empresa(Integer id, String cnpj, String inscricaoEstadual, String inscricaoMunicipal, String inscricaoEstadualSt, String suframa, String cnae, String nomeFantasia, String razaoSocial, String endereco, String complemento, String bairro, String cidade, String cep, String uf, String regimeTributario, String telelefone, String celular, String emailContato, String situacao, String classificacaoFinanceira, String jdbcUrl, String tipoEmpresa) {
        this.id = id;
        this.cnpj = cnpj;
        this.inscricaoEstadual = inscricaoEstadual;
        this.inscricaoMunicipal = inscricaoMunicipal;
        this.inscricaoEstadualSt = inscricaoEstadualSt;
        this.suframa = suframa;
        this.cnae = cnae;
        this.nomeFantasia = nomeFantasia;
        this.razaoSocial = razaoSocial;
        this.endereco = endereco;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.cep = cep;
        this.uf = uf;
        this.regimeTributario = regimeTributario;
        this.telelefone = telelefone;
        this.celular = celular;
        this.emailContato = emailContato;
        this.situacao = situacao;
        this.classificacaoFinanceira = classificacaoFinanceira;
        this.jdbcUrl = jdbcUrl;
        this.tipoEmpresa = tipoEmpresa;
    }

    public Empresa(EmpresaDto obj) {
        this.id = obj.getId();
        this.cnpj = obj.getCnpj();
        this.inscricaoEstadual = obj.getInscricaoEstadual();
        this.inscricaoMunicipal = obj.getInscricaoMunicipal();
        this.inscricaoEstadualSt = obj.getInscricaoEstadualSt();
        this.suframa = obj.getSuframa();
        this.cnae = obj.getCnae();
        this.nomeFantasia = obj.getNomeFantasia();
        this.razaoSocial = obj.getRazaoSocial();
        this.endereco = obj.getEndereco();
        this.complemento = obj.getComplemento();
        this.bairro = obj.getBairro();
        this.cidade = obj.getCidade();
        this.cep = obj.getCep();
        this.uf = obj.getUf();
        this.regimeTributario = obj.getRegimeTributario();
        this.telelefone = obj.getTelelefone();
        this.celular = obj.getCelular();
        this.emailContato = obj.getEmailContato();
        this.situacao = obj.getSituacao();
        this.classificacaoFinanceira = obj.getClassificacaoFinanceira();
        this.jdbcUrl = obj.getJdbcUrl();
        this.tipoEmpresa = obj.getTipoEmpresa();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public String getInscricaoMunicipal() {
        return inscricaoMunicipal;
    }

    public void setInscricaoMunicipal(String inscricaoMunicipal) {
        this.inscricaoMunicipal = inscricaoMunicipal;
    }

    public String getInscricaoEstadualSt() {
        return inscricaoEstadualSt;
    }

    public void setInscricaoEstadualSt(String inscricaoEstadualSt) {
        this.inscricaoEstadualSt = inscricaoEstadualSt;
    }

    public String getSuframa() {
        return suframa;
    }

    public void setSuframa(String suframa) {
        this.suframa = suframa;
    }

    public String getCnae() {
        return cnae;
    }

    public void setCnae(String cnae) {
        this.cnae = cnae;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
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

    public String getRegimeTributario() {
        return regimeTributario;
    }

    public void setRegimeTributario(String regimeTributario) {
        this.regimeTributario = regimeTributario;
    }

    public String getTelelefone() {
        return telelefone;
    }

    public void setTelelefone(String telelefone) {
        this.telelefone = telelefone;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmailContato() {
        return emailContato;
    }

    public void setEmailContato(String emailContato) {
        this.emailContato = emailContato;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getClassificacaoFinanceira() {
        return classificacaoFinanceira;
    }

    public void setClassificacaoFinanceira(String classificacaoFinanceira) {
        this.classificacaoFinanceira = classificacaoFinanceira;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getTipoEmpresa() {
        return tipoEmpresa;
    }

    public void setTipoEmpresa(String tipoEmpresa) {
        this.tipoEmpresa = tipoEmpresa;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Empresa empresa = (Empresa) o;
        return Objects.equals(id, empresa.id) && Objects.equals(cnpj, empresa.cnpj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cnpj);
    }
}
