package br.com.codex.v1.domain.cadastros;

import br.com.codex.v1.domain.dto.EmpresaDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Empresa implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 25)
    private String cnpj;
    @Column(length = 20)
    private String inscricaoEstadual;
    @Column(length = 20)
    private String inscricaoMunicipal;
    @Column(length = 20)
    private String inscricaoEstadualSt;
    @Column(length = 20)
    private String suframa;
    private String cnae;
    @Column(length = 150)
    private String nomeFantasia;
    @Column(length = 150)
    private String razaoSocial;
    @Column(length = 200)
    private String endereco;
    @Column(length = 6)
    private String numero;
    private String complemento;
    @Column(length = 60)
    private String bairro;
    @Column(length = 60)
    private String cidade;
    private String cep;
    @Column(length = 3)
    private String uf;
    @Column(length = 70)
    private String regimeTributario;
    @Column(length = 25)
    private String telefone;
    @Column(length = 25)
    private String celular;
    @Email
    @Column(length = 40)
    private String emailContato;
    @Column(length = 10)
    private String situacao;
    @Column(length = 10)
    private String classificacaoFinanceira;
    private String jdbcUrl;// ex: jdbc:mysql://host:3306/empresa_012345
    @Column(length = 40)
    private String tipoEmpresa;
    private boolean possuiBase; //por padrão é inicializada como false;
    @Column(length = 9)
    private String codigoCidade;

    public Empresa() {
        super();
    }

    public Empresa(Long id, String cnpj, String inscricaoEstadual, String inscricaoMunicipal, String inscricaoEstadualSt, String suframa, String cnae, String nomeFantasia, String razaoSocial, String endereco, String numero, String complemento, String bairro, String cidade, String cep, String uf, String regimeTributario, String telefone, String celular, String emailContato, String situacao, String classificacaoFinanceira, String jdbcUrl, String tipoEmpresa, Boolean possuiBase, String codigoCidade) {
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
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.cep = cep;
        this.uf = uf;
        this.regimeTributario = regimeTributario;
        this.telefone = telefone;
        this.celular = celular;
        this.emailContato = emailContato;
        this.situacao = situacao;
        this.classificacaoFinanceira = classificacaoFinanceira;
        this.jdbcUrl = jdbcUrl;
        this.tipoEmpresa = tipoEmpresa;
        this.possuiBase = possuiBase;
        this.codigoCidade = codigoCidade;
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
        this.numero = obj.getNumero();
        this.complemento = obj.getComplemento();
        this.bairro = obj.getBairro();
        this.cidade = obj.getCidade();
        this.cep = obj.getCep();
        this.uf = obj.getUf();
        this.regimeTributario = obj.getRegimeTributario();
        this.telefone = obj.getTelefone();
        this.celular = obj.getCelular();
        this.emailContato = obj.getEmailContato();
        this.situacao = obj.getSituacao();
        this.classificacaoFinanceira = obj.getClassificacaoFinanceira();
        this.jdbcUrl = obj.getJdbcUrl();
        this.tipoEmpresa = obj.getTipoEmpresa();
        this.possuiBase = obj.isPossuiBase();
        this.codigoCidade = obj.getCodigoCidade();
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
