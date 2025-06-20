package br.com.codex.v1.service;

import br.com.codex.v1.configuration.PersistenceUnitInfoAdapter;
import br.com.codex.v1.domain.cadastros.AmbienteNotaFiscal;
import br.com.codex.v1.domain.cadastros.Empresa;
import br.com.codex.v1.domain.cadastros.TabelaCfop;
import br.com.codex.v1.domain.cadastros.Usuario;
import br.com.codex.v1.domain.enums.Perfil;
import br.com.codex.v1.domain.repository.AmbienteNotaFiscalRepository;
import br.com.codex.v1.domain.repository.EmpresaRepository;
import br.com.codex.v1.domain.repository.TabelaCfopRepository;
import br.com.codex.v1.domain.repository.UsuarioRepository;
import br.com.codex.v1.utilitario.ImportaTabelaCfop;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DBService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private AmbienteNotaFiscalRepository ambienteNotaFiscalRepository;

    @Autowired
    private ImportaTabelaCfop importaTabelaCfop;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public void criaOutrasBases(DataSource dataSource) {
        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.hbm2ddl.auto", "update");  // cria ou atualiza tabelas
        props.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");

        // Use a naming strategy do Spring Boot que gera nomes snake_case:
        props.put("hibernate.physical_naming_strategy", "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy");

        // Datasource não transacional
        props.put("javax.persistence.nonJtaDataSource", dataSource);
        PersistenceUnitInfoAdapter info = new PersistenceUnitInfoAdapter("dynamicUnit", dataSource);
        EntityManagerFactory emf = new HibernatePersistenceProvider().createContainerEntityManagerFactory(info, props);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            Usuario pessoa = new Usuario(null, "Administrador", "80374841063", Date.valueOf("2024-01-07"), "Neutro", "19974061119",
                    "Rua Indefinida 07", "Indefinido", "Hortolândia", "SP", "13185-421", "suporte@codexsolucoes.com.br",
                    encoder.encode("Admin@2024!"), "Sistema", "00000");
            pessoa.addPerfil(Perfil.ADMINISTRADOR);

            // Importa CFOPs
            importaTabelaCfop.importarCfops();

            em.persist(pessoa);

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Erro ao popular dados", e);
        } finally {
            em.close();
            emf.close();
        }
    }

    public void criaBaseCodex() {
            Usuario pessoa = new Usuario(null, "Administrador", "80374841063",
                    Date.valueOf("2024-01-07"), "Neutro", "19974061119",
                    "Rua Indefinida 07", "Indefinido", "Hortolândia", "SP",
                    "13185-421", "suporte@codexsolucoes.com.br",
                    encoder.encode("Admin@2024!"), "Sistema", "00000");
            pessoa.addPerfil(Perfil.ADMINISTRADOR);
            usuarioRepository.save(pessoa);

            Empresa empresa = new Empresa(null, "37025579000157", "", "", "", "", "",
                    "Codex Soluções Em TI", "", "", "", "", "", "", "", "", "", "", "",
                    "Ativo", "Ótimo", "codex", "----", true, "");
            empresaRepository.save(empresa);

            AmbienteNotaFiscal ambienteNotaFiscal = new AmbienteNotaFiscal(null, 2);
            ambienteNotaFiscalRepository.save(ambienteNotaFiscal);

            // Importa CFOPs
            importaTabelaCfop.importarCfops();
    }
}