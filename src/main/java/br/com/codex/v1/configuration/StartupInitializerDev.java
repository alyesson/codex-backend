package br.com.codex.v1.configuration;

import br.com.codex.v1.domain.repository.EmpresaRepository;
import br.com.codex.v1.domain.repository.UsuarioRepository;
import br.com.codex.v1.service.DBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Profile("desenvolvimento")
public class StartupInitializerDev {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private DBService dbService;

    @PostConstruct
    public void init() {
        try {
            // Verifica se a tabela pessoa (usuários) está vazia
            if (usuarioRepository.count() == 0) {
                System.out.println("🟢 Banco 'codex' está vazio. Populando com dados iniciais...");

                dbService.criaBaseCodex();

                System.out.println("✅ Dados iniciais inseridos com sucesso.");
            } else {
                System.out.println("ℹ️ Banco já contém dados. Nenhuma ação necessária.");
            }
        } catch (Exception e) {
            System.err.println("❌ Erro ao inicializar base codex: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
