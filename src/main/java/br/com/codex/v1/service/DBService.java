package br.com.codex.v1.service;

import br.com.codex.v1.domain.cadastros.Empresa;
import br.com.codex.v1.domain.cadastros.Usuario;
import br.com.codex.v1.domain.enums.Perfil;
import br.com.codex.v1.domain.repository.EmpresaRepository;
import br.com.codex.v1.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.sql.Date;
import java.util.Map;


@Service
public class DBService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Transactional // Garante transação no contexto atual
    public void instanciaDB() {
        if (usuarioRepository.count() == 0) {
            Usuario pessoa = new Usuario(null, "Administrador", "80374841063", Date.valueOf("2024-01-07"), "Neutro", "19974061119",
                    "Rua Indefinida 07", "Indefinido", "Hortolândia", "SP", "13185-421", "suporte@codexsolucoes.com.br",
                    encoder.encode("Admin@2024!"), "Sistema", "00000");
            pessoa.addPerfil(Perfil.ADMINISTRADOR);
            usuarioRepository.save(pessoa);

            Empresa empresa = new Empresa(null, "37025579000157", "", "", "", "", "",
                    "Codex Soluções Em TI", "", "", "", "", "", "", "", "", "", "", "",
                    "Ativo", "Ótimo", "codex", "----", true);
            empresaRepository.save(empresa);
        }
    }
}

/*@Service
public class DBService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public void instanciaDB() {
        // Verifica se já existem dados antes de inserir
        if (usuarioRepository.count() == 0) {
            Usuario pessoa = new Usuario(null, "Administrador", "80374841063",
                    Date.valueOf("2024-01-07"), "Neutro", "19974061119",
                    "Rua Indefinida 07", "Indefinido", "Hortolândia", "SP",
                    "13185-421", "suporte@codexsolucoes.com.br",
                    encoder.encode("Admin@2024!"), "Sistema", "00000");
            pessoa.addPerfil(Perfil.ADMINISTRADOR);
            usuarioRepository.save(pessoa);

            Empresa empresa = new Empresa(null, "37025579000157", "", "", "", "", "",
                    "Codex Soluções Em TI", "", "", "", "", "", "", "", "", "", "", "",
                    "Ativo", "Ótimo", "codex", "----", true);
            empresaRepository.save(empresa);
        }
    }
}*/