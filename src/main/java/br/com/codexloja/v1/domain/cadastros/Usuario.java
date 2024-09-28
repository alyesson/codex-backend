package br.com.codexloja.v1.domain.cadastros;

import br.com.codexloja.v1.domain.dto.UsuarioDto;
import br.com.codexloja.v1.domain.enums.Perfil;
import javax.persistence.Entity;

import java.io.Serial;
import java.sql.Date;
import java.util.stream.Collectors;

@Entity
public class Usuario extends Pessoa {
    @Serial
    private static final long serialVersionUID = 1L;

    public Usuario() {
        super();
    }

    public Usuario(Integer id, String nome, String cpf, Date nascimento, String sexo, String telefone, String endereco, String bairro, String cidade, String uf, String cep, String email, String senha, String departamento, String centroCusto) {
        super(id, nome, cpf, nascimento, sexo, telefone, endereco, bairro, cidade, uf, cep, email, senha, departamento, centroCusto);
    }

    public Usuario(UsuarioDto obj) {
        this.id = obj.getId();
        this.nome = obj.getNome();
        this.cpf = obj.getCpf();
        this.nascimento = obj.getNascimento();
        this.sexo = obj.getSexo();
        this.telefone = obj.getTelefone();
        this.endereco = obj.getEndereco();
        this.bairro = obj.getBairro();
        this.cidade = obj.getCidade();
        this.uf = obj.getUf();
        this.cep = obj.getCep();
        this.email = obj.getEmail();
        this.senha = obj.getSenha();
        this.perfis = obj.getPerfis().stream().map(Perfil::getCodigo).collect(Collectors.toSet());
        this.departamento = obj.getDepartamento();
        this.centroCusto = obj.getCentroCusto();
    }
}
