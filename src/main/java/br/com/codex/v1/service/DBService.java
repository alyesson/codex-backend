package br.com.codex.v1.service;

import br.com.codex.v1.configuration.PersistenceUnitInfoAdapter;
import br.com.codex.v1.domain.cadastros.AmbienteNotaFiscal;
import br.com.codex.v1.domain.cadastros.Empresa;
import br.com.codex.v1.domain.cadastros.TabelaCfop;
import br.com.codex.v1.domain.cadastros.Usuario;
import br.com.codex.v1.domain.enums.Perfil;
import br.com.codex.v1.domain.estoque.MotivoAcerto;
import br.com.codex.v1.domain.repository.*;
import br.com.codex.v1.domain.rh.EventosFolha;
import br.com.codex.v1.utilitario.ImportaEventosFolha;
import br.com.codex.v1.utilitario.ImportaMotivosAcerto;
import br.com.codex.v1.utilitario.ImportaTabelaCfop;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.sql.Date;
import java.util.*;

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
    private ImportaMotivosAcerto importaMotivosAcerto;

    @Autowired
    private ImportaEventosFolha importaEventosFolha;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public void criaOutrasBases(DataSource dataSource) {
        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.hbm2ddl.auto", "update");  // cria ou atualiza tabelas
        props.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        props.put("hibernate.physical_naming_strategy", "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy");// Use a naming strategy do Spring Boot que gera nomes snake_case:
        props.put("javax.persistence.nonJtaDataSource", dataSource);// Datasource não transacional
        PersistenceUnitInfoAdapter info = new PersistenceUnitInfoAdapter("dynamicUnit", dataSource);
        EntityManagerFactory emf = new HibernatePersistenceProvider().createContainerEntityManagerFactory(info, props);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        try {
            //Adiciona o usuário administrador padrão
            List<Usuario> usuariosPadrao = Arrays.asList(

                    new Usuario(null, "Administrador", "80374841063", Date.valueOf("2024-01-07"), "Neutro", "19974061119",
                    "Rua Indefinida 07", "Indefinido", "Hortolândia", "SP", "13185-421", "suporte@codexsolucoes.com.br",
                    encoder.encode("Admin@2025!"), "Sistema", "00000"),

                    new Usuario(null, "Sistema", "13784744141", Date.valueOf("2024-01-07"), "Neutro", "19999999999",
                            "Rua Indefinida 05", "Indefinido", "Hortolândia", "SP",
                            "13185-421", "sistema@sistema.com.br",
                            encoder.encode("Sistema@2026!"), "Sistema", "00001"));

                    usuariosPadrao.get(0).addPerfil(Perfil.ADMINISTRADOR);
                    usuariosPadrao.get(1).addPerfil(Perfil.SISTEMA);

                    for (Usuario usuario : usuariosPadrao) {
                        em.persist(usuario);
                    }

            //Adiciona o ambiente de nota fiscal padrão
            AmbienteNotaFiscal ambienteNotaFiscal = new AmbienteNotaFiscal(null, 2);
            em.persist(ambienteNotaFiscal);

            //Cria alguns motivos de acerto
            List<MotivoAcerto> listaMotivos = criaListaMotivoAcerto();
            for(MotivoAcerto motivos : listaMotivos){
                em.persist(motivos);
            }

            //Cria a lista dos eventos da folha
            List<EventosFolha> listaEventosFolha = criaListaEventosFolha();
            for(EventosFolha eventos : listaEventosFolha){
                em.persist(eventos);
            }

            // Métudo que retorna todos CFOPs
            List<TabelaCfop> cfops = criarListaCfopCompleta();
            for (TabelaCfop cfop : cfops) {
                em.persist(cfop);
            }

            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao popular dados", e);
        } finally {
            em.close();
            emf.close();
        }
    }

    public void criaBaseCodex() {

        // Cria usuários iniciais
        List<Usuario> usuariosPadrao = Arrays.asList(
                new Usuario(null, "Administrador", "80374841063",
                        Date.valueOf("2024-01-07"), "Neutro", "19974061119",
                        "Rua Indefinida", "Indefinido", "Hortolândia", "SP",
                        "13185-421", "suporte@codexsolucoes.com.br",
                        encoder.encode("Admin@2026!"), "Sistema", "00000"),

                new Usuario(null, "Sistema", "13784744141",
                        Date.valueOf("2024-01-07"), "Neutro", "19999999999",
                        "Rua Indefinida 05", "Indefinido", "Hortolândia", "SP",
                        "13185-421", "sistema@sistema.com.br",
                        encoder.encode("Sistema@2026!"), "Sistema", "00001")
        );

        // Atribui perfis
        usuariosPadrao.get(0).addPerfil(Perfil.ADMINISTRADOR);
        usuariosPadrao.get(1).addPerfil(Perfil.SISTEMA);

        // Salva todos os usuários
        usuarioRepository.saveAll(usuariosPadrao);

            Empresa empresa = new Empresa(null, "37025579000157", "", "", "", "", "",
                    "Codex Soluções Em TI", "", "", "", "","", "", "", "", "", "", "", "",
                    "Ativo", "Ótimo", "codex", "----", true, "");
            empresaRepository.save(empresa);

            AmbienteNotaFiscal ambienteNotaFiscal = new AmbienteNotaFiscal(null, 2);
            ambienteNotaFiscalRepository.save(ambienteNotaFiscal);

            //Importa Motivos Acerto
            importaMotivosAcerto.importarMotivosAcerto();

            //Importa Eventos Folha
            importaEventosFolha.importaEventosFolha();

            // Importa CFOPs
            importaTabelaCfop.importarCfops();
    }

    public void criaBaseCodexDev() {

        // Cria usuários iniciais
        List<Usuario> usuariosPadrao = Arrays.asList(
                new Usuario(null, "Administrador", "80374841063",
                        Date.valueOf("2024-01-07"), "Neutro", "19974061119",
                        "Rua Indefinida", "Indefinido", "Hortolândia", "SP",
                        "13185-421", "suporte@codexsolucoes.com.br",
                        encoder.encode("Admin@2026!"), "Sistema", "00000"),

                new Usuario(null, "Sistema", "13784744141",
                        Date.valueOf("2024-01-07"), "Neutro", "19999999999",
                        "Rua Indefinida 05", "Indefinido", "Hortolândia", "SP",
                        "13185-421", "sistema@sistema.com.br",
                        encoder.encode("Sistema@2026!"), "Sistema", "00001")
        );

        // Atribui perfis
        usuariosPadrao.get(0).addPerfil(Perfil.ADMINISTRADOR);
        usuariosPadrao.get(1).addPerfil(Perfil.SISTEMA);

        // Salva todos os usuários
        usuarioRepository.saveAll(usuariosPadrao);

        Empresa empresa = new Empresa(null, "37025579000157", "", "", "", "", "",
                "Codex Soluções Em TI", "", "", "", "","", "", "", "", "", "", "", "",
                "Ativo", "Ótimo", "codex-dev", "----", true, "");
        empresaRepository.save(empresa);

        AmbienteNotaFiscal ambienteNotaFiscal = new AmbienteNotaFiscal(null, 2);
        ambienteNotaFiscalRepository.save(ambienteNotaFiscal);

        //Importa Motivos Acerto
        importaMotivosAcerto.importarMotivosAcerto();

        //Importa Eventos Folha
        importaEventosFolha.importaEventosFolha();

        // Importa CFOPs
        importaTabelaCfop.importarCfops();
    }

    private List<MotivoAcerto> criaListaMotivoAcerto(){
        List<MotivoAcerto> listaMotivoAcerto = new ArrayList<>();
        listaMotivoAcerto.add(new MotivoAcerto(null, "1000", "Entrada De Material Com Nota Fiscal"));
        listaMotivoAcerto.add(new MotivoAcerto(null, "1001", "Entrada De Material Sem Nota Fiscal"));
        listaMotivoAcerto.add(new MotivoAcerto(null, "1002", "Devolução"));
        listaMotivoAcerto.add(new MotivoAcerto(null, "1003", "Material Danificado"));
        listaMotivoAcerto.add(new MotivoAcerto(null, "1004", "Material Extraviado"));

        return listaMotivoAcerto;
    }

    private List<EventosFolha> criaListaEventosFolha(){
        List<EventosFolha> listaEventosFolha = new ArrayList<>();
        listaEventosFolha.add(new EventosFolha(null, "1", "Normais Diurnas"));
        listaEventosFolha.add(new EventosFolha(null, "2", "Adiantamento Salário"));
        listaEventosFolha.add(new EventosFolha(null, "5", "Horas Repouso Remunerado Diruno"));
        listaEventosFolha.add(new EventosFolha(null, "6", "Desconto Dias de Faltas"));
        listaEventosFolha.add(new EventosFolha(null, "7", "Desconto Dias de Descanso Semanal Remunerado (DSR)"));
        listaEventosFolha.add(new EventosFolha(null, "8", "Horas de Atestado Médico"));
        listaEventosFolha.add(new EventosFolha(null, "9", "Dias de atestado médico"));
        listaEventosFolha.add(new EventosFolha(null, "12", "Horas Normais Noturnas"));
        listaEventosFolha.add(new EventosFolha(null, "13", "Redução Horário Noturno"));
        listaEventosFolha.add(new EventosFolha(null, "14", "% Adicional Noturno"));
        listaEventosFolha.add(new EventosFolha(null, "15", "Desconto Horas Atrasos e/ou Saídas"));
        listaEventosFolha.add(new EventosFolha(null, "16", "Salário Simbólico p/ Cálculo FGTS"));
        listaEventosFolha.add(new EventosFolha(null, "17", "Pró-labore"));
        listaEventosFolha.add(new EventosFolha(null, "18", "Desconto Dias de Faltas - Feriado"));
        listaEventosFolha.add(new EventosFolha(null, "19", "Bolsa Auxílio"));
        listaEventosFolha.add(new EventosFolha(null, "23", "Suspensão Disciplinar"));
        listaEventosFolha.add(new EventosFolha(null, "25", "Horas Repouso Remunerado Noturno"));
        listaEventosFolha.add(new EventosFolha(null, "26", "DSR Sobre Hora Extra 50%"));
        listaEventosFolha.add(new EventosFolha(null, "27", "DSR Sobre Hora Extra 75%"));
        listaEventosFolha.add(new EventosFolha(null, "28", "DSR Sobre Hora Extra 100%"));
        listaEventosFolha.add(new EventosFolha(null, "44", "Desconto Adiantamento Salário"));
        listaEventosFolha.add(new EventosFolha(null, "45", "Diferença Salário Mês Anterior"));
        listaEventosFolha.add(new EventosFolha(null, "46", "Adicional Insalubridade"));
        listaEventosFolha.add(new EventosFolha(null, "47", "Adicional Periculosidade"));
        listaEventosFolha.add(new EventosFolha(null, "48", "Diferença Férias"));
        listaEventosFolha.add(new EventosFolha(null, "49", "Diferença Dissídio"));
        listaEventosFolha.add(new EventosFolha(null, "51", "% Comissões"));
        listaEventosFolha.add(new EventosFolha(null, "52", "Integração Comissões s/ Repouso"));
        listaEventosFolha.add(new EventosFolha(null, "53", "Gratificações"));
        listaEventosFolha.add(new EventosFolha(null, "54", "% Quebra de Caixa"));
        listaEventosFolha.add(new EventosFolha(null, "55", "% Estimativa Gorjeta"));
        listaEventosFolha.add(new EventosFolha(null, "56", "% Prêmio Frequência"));
        listaEventosFolha.add(new EventosFolha(null, "98", "Horas Extras 50%"));
        listaEventosFolha.add(new EventosFolha(null, "99", "Horas Extras 70%"));
        listaEventosFolha.add(new EventosFolha(null, "100", "Horas Extras 100%"));
        listaEventosFolha.add(new EventosFolha(null, "101", "Salário Maternidade"));
        listaEventosFolha.add(new EventosFolha(null, "102", "Horas Extras 50% Sobre Salário Maternidade"));
        listaEventosFolha.add(new EventosFolha(null, "103", "Horas Extras 70% Sobre Salário Maternidade"));
        listaEventosFolha.add(new EventosFolha(null, "104", "Horas Extras 100% Sobre Salário Maternidade"));
        listaEventosFolha.add(new EventosFolha(null, "105", "DSR Diurno Sobre Salário Maternidade"));
        listaEventosFolha.add(new EventosFolha(null, "106", "DSR Noturno Sobre Salário Maternidade"));
        listaEventosFolha.add(new EventosFolha(null, "107", "Adicional Noturno Sobre Salário Maternidade"));
        listaEventosFolha.add(new EventosFolha(null, "116", "Integração Horas Extras e Variáveis Repouso"));
        listaEventosFolha.add(new EventosFolha(null, "117", "Integração de Horas Noturnas"));
        listaEventosFolha.add(new EventosFolha(null, "130", "Ajuda de Custo"));
        listaEventosFolha.add(new EventosFolha(null, "131", "Desconto Salário Maternidade"));
        listaEventosFolha.add(new EventosFolha(null, "132", "Auxílio Natalidade"));
        listaEventosFolha.add(new EventosFolha(null, "133", "Salário Família"));
        listaEventosFolha.add(new EventosFolha(null, "134", "Salário Paternidade"));
        listaEventosFolha.add(new EventosFolha(null, "140", "30 Dias de Férias Gozadas - Férias"));
        listaEventosFolha.add(new EventosFolha(null, "141", "20 Dias de Férias Gozadas - Férias"));
        listaEventosFolha.add(new EventosFolha(null, "142", "15 Dias de Férias Gozadas - Férias"));
        listaEventosFolha.add(new EventosFolha(null, "143", "10 Dias de Férias Gozadas - Férias"));
        listaEventosFolha.add(new EventosFolha(null, "144", "Dias de Férias Gozadas - Férias"));
        listaEventosFolha.add(new EventosFolha(null, "145", "1/3 De Férias"));
        listaEventosFolha.add(new EventosFolha(null, "146", "Abono Pecuniário - Férias"));
        listaEventosFolha.add(new EventosFolha(null, "147", "1/3 Abono Pecuniário - Férias"));
        listaEventosFolha.add(new EventosFolha(null, "148", "Média Horas Extras 50% - Férias"));
        listaEventosFolha.add(new EventosFolha(null, "149", "Média Horas Extras 100% - Férias"));
        listaEventosFolha.add(new EventosFolha(null, "150", "Média Horas Extras 70% - Férias"));
        listaEventosFolha.add(new EventosFolha(null, "151", "Adicional Insalubridade - Férias"));
        listaEventosFolha.add(new EventosFolha(null, "152", "Adicional Periculosidade - Férias"));
        listaEventosFolha.add(new EventosFolha(null, "153", "Média Comissões - Férias"));
        listaEventosFolha.add(new EventosFolha(null, "154", "Média Adicional Noturno - Férias"));
        listaEventosFolha.add(new EventosFolha(null, "155", "Desconto Dias Redução p/ Faltas - Férias"));
        listaEventosFolha.add(new EventosFolha(null, "156", "Férias em Dobro - Férias"));
        listaEventosFolha.add(new EventosFolha(null, "157", "1/3 Constitucional Férias em Dobro - Férias"));
        listaEventosFolha.add(new EventosFolha(null, "158", "% INSS Sobre Férias"));
        listaEventosFolha.add(new EventosFolha(null, "159", "% IRF Sobre Férias"));
        listaEventosFolha.add(new EventosFolha(null, "167", "Adiantamento 1° Parcela 13º Salário"));
        listaEventosFolha.add(new EventosFolha(null, "168", "Adiantamento 13º Salário Horas Extras 50%"));
        listaEventosFolha.add(new EventosFolha(null, "169", "Adiantamento 13º Salário Horas Extras 70%"));
        listaEventosFolha.add(new EventosFolha(null, "170", "Adiantamento 13º Salário Horas Extras 100%"));
        listaEventosFolha.add(new EventosFolha(null, "171", "Adiantamento 13º Salário"));
        listaEventosFolha.add(new EventosFolha(null, "172", "Adiantamento 13º Salário - Desconto"));
        listaEventosFolha.add(new EventosFolha(null, "176", "DSR Diurno Sobre 1° Parcela 13°"));
        listaEventosFolha.add(new EventosFolha(null, "177", "DSR Noturno Sobre 1° Parcela 13°"));
        listaEventosFolha.add(new EventosFolha(null, "178", "Insalubridade Sobre 1° Parcela 13°"));
        listaEventosFolha.add(new EventosFolha(null, "179", "Periculosidade Sobre 1° Parcela 13°"));
        listaEventosFolha.add(new EventosFolha(null, "182", "13º Salário Final Horas Extras 50%"));
        listaEventosFolha.add(new EventosFolha(null, "183", "13º Salário Final Horas Extras 70%"));
        listaEventosFolha.add(new EventosFolha(null, "184", "13º Salário Final Horas Extras 100%"));
        listaEventosFolha.add(new EventosFolha(null, "185", "DSR Diurno Sobre 2° Parcela 13°"));
        listaEventosFolha.add(new EventosFolha(null, "186", "DSR Noturno Sobre 2° Parcela 13°"));
        listaEventosFolha.add(new EventosFolha(null, "187", "Insalubridade Sobre 2° Parcela 13°"));
        listaEventosFolha.add(new EventosFolha(null, "188", "Periculosidade Sobre 2° Parcela 13°"));
        listaEventosFolha.add(new EventosFolha(null, "189", "2° Parcela 13º Salário"));
        listaEventosFolha.add(new EventosFolha(null, "190", "Desconto Adiantamento 13º Salário"));
        listaEventosFolha.add(new EventosFolha(null, "194", "13º Salário Final Horas Extras e Adicional s/ Repouso"));
        listaEventosFolha.add(new EventosFolha(null, "195", "13º Salário Final Média Comissões"));
        listaEventosFolha.add(new EventosFolha(null, "200", "13º Salário Final Comissão s/ Repouso"));
        listaEventosFolha.add(new EventosFolha(null, "201", "Complemento Média Hora Extra 50% do 13°"));
        listaEventosFolha.add(new EventosFolha(null, "202", "Complemento Média Hora Extra 70% do 13°"));
        listaEventosFolha.add(new EventosFolha(null, "203", "Complemento Média Hora Extra 100% do 13°"));
        listaEventosFolha.add(new EventosFolha(null, "204", "Complemento Média Ref. DSR Diurno"));
        listaEventosFolha.add(new EventosFolha(null, "205", "Complemento Média Ref. DSR Noturno"));
        listaEventosFolha.add(new EventosFolha(null, "206", "13º Salário Média Adicional Noturno"));
        listaEventosFolha.add(new EventosFolha(null, "229", "Empréstimo Consignado"));
        listaEventosFolha.add(new EventosFolha(null, "230", "Desconto Empréstimo - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "231", "Vale Alimentação"));
        listaEventosFolha.add(new EventosFolha(null, "232", "Vale Farmácia"));
        listaEventosFolha.add(new EventosFolha(null, "233", "Vale Refeição"));
        listaEventosFolha.add(new EventosFolha(null, "235", "Convênio Assistência Médica"));
        listaEventosFolha.add(new EventosFolha(null, "236", "Seguro de Vida"));
        listaEventosFolha.add(new EventosFolha(null, "238", "Desconto Seguro Vida"));
        listaEventosFolha.add(new EventosFolha(null, "239", "Vale Transporte"));
        listaEventosFolha.add(new EventosFolha(null, "241", "6% Desconto Vale Transporte"));
        listaEventosFolha.add(new EventosFolha(null, "242", "Convênio Assistência Odontológica"));
        listaEventosFolha.add(new EventosFolha(null, "243", "Coparticipação Assistência Médica"));
        listaEventosFolha.add(new EventosFolha(null, "244", "% Desconto INSS"));
        listaEventosFolha.add(new EventosFolha(null, "245", "% INSS Sobre 13º Salário"));
        listaEventosFolha.add(new EventosFolha(null, "246", "% Desconto IRF"));
        listaEventosFolha.add(new EventosFolha(null, "247", "% IRF Sobre 13º Salário"));
        listaEventosFolha.add(new EventosFolha(null, "248", "% Pensão Alimentícia"));
        listaEventosFolha.add(new EventosFolha(null, "256", "Coparticipação Assistência Odontológica"));
        listaEventosFolha.add(new EventosFolha(null, "259", "Vale Creche"));
        listaEventosFolha.add(new EventosFolha(null, "260", "Dia Desconto Contribuição Sindical"));
        listaEventosFolha.add(new EventosFolha(null, "302", "Dias Saldo Salário - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "303", "Dias Aviso Prévio Trabalhados - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "304", "Dias Aviso Prévio Indenizados - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "305", "Multa do FGTS - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "306", "Férias Proporcionais - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "307", "Férias Vencidas - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "308", "Média Horas Extras Férias Proporcionais - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "309", "Insalubridade Férias Proporcionais - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "310", "Periculosidade Férias Proporcionais - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "311", "Média Adicional Noturno Férias Proporcionais - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "312", "Salário Família - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "313", "% INSS s/ Aviso Prévio - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "314", "% IRF Participação nos Lucros - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "315", "Média Comissões Férias Proporcionais - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "316", "Média Horas Extras Férias - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "317", "Adiantamento 13º Salário - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "318", "Média Triênio Férias Proporcionais - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "319", "Média Quinquênio Férias Proporcionais - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "324", "1/3 Férias Constitucional - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "325", "13º Salário Proporcional - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "326", "Insalubridade 13º Salário - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "327", "Periculosidade 13º Salário - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "328", "Média Horas Extras 13º Salário Proporcional - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "329", "Adicional Noturno 13º Salário - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "330", "Comissões 13º Salário Proporcional - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "331", "Multa Art. 476-A §5º CLT"));
        listaEventosFolha.add(new EventosFolha(null, "332", "Média Adicional Noturno - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "333", "Insalubridade - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "335", "Insalubridade 13º Salário - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "336", "Periculosidade - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "338", "Hora Extra 50% - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "339", "Hora Extra 70% - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "340", "Hora Extra 100% - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "342", "Desconto Vale Transporte - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "343", "Desconto Vale Refeição - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "347", "Faltas Calculadas nas Férias - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "349", "Faltas - Rescisão"));
        listaEventosFolha.add(new EventosFolha(null, "350", "1/12 Avos 13º Salário s/ Aviso Prévio"));
        listaEventosFolha.add(new EventosFolha(null, "351", "1/12 Avos Férias s/ Aviso Prévio. Inden."));
        listaEventosFolha.add(new EventosFolha(null, "352", "1/12 Avos 13º Salário C/ Aviso Prévio"));
        listaEventosFolha.add(new EventosFolha(null, "353", "1/12 Avos Férias C/ Aviso Prévio. Inden."));
        listaEventosFolha.add(new EventosFolha(null, "402", "FGTS Normal Deposito"));
        listaEventosFolha.add(new EventosFolha(null, "403", "FGTS 13º Salário Deposito"));
        listaEventosFolha.add(new EventosFolha(null, "404", "Dedução Dependentes IRF"));
        listaEventosFolha.add(new EventosFolha(null, "405", "Dedução Dependentes IRF s/ Férias"));
        listaEventosFolha.add(new EventosFolha(null, "407", "Dedução Dependentes IRF s/ 13º Salário"));
        listaEventosFolha.add(new EventosFolha(null, "408", "Prévia IRF"));
        listaEventosFolha.add(new EventosFolha(null, "409", "Participação nos Lucros e Resultados"));
        listaEventosFolha.add(new EventosFolha(null, "410", "Abono Salarial"));
        listaEventosFolha.add(new EventosFolha(null, "412", "Reembolso creche"));
        listaEventosFolha.add(new EventosFolha(null, "414", "Gratificação Semestral"));
        listaEventosFolha.add(new EventosFolha(null, "416", "Viagens"));
        listaEventosFolha.add(new EventosFolha(null, "417", "1° Parcela P.L.R"));
        listaEventosFolha.add(new EventosFolha(null, "418", "2° Parcela P.L.R"));
        listaEventosFolha.add(new EventosFolha(null, "420", "1° Parcela Abono Salarial"));
        listaEventosFolha.add(new EventosFolha(null, "421", "2° Parcela Abono Salarial"));
        listaEventosFolha.add(new EventosFolha(null, "422", "Arredondamento Anterior"));
        listaEventosFolha.add(new EventosFolha(null, "423", "Arredondamento Atual"));
        listaEventosFolha.add(new EventosFolha(null, "424", "Desconto Complementar Insuficiência Saldo"));
        listaEventosFolha.add(new EventosFolha(null, "425", "Complemento p/ Insuficiência de Saldo"));
        listaEventosFolha.add(new EventosFolha(null, "426", "Retenção INSS Empregador"));
        listaEventosFolha.add(new EventosFolha(null, "427", "Pensão Alimentícia s/ Líquido Férias"));
        listaEventosFolha.add(new EventosFolha(null, "428", "Prévia IRF s/ Férias"));
        listaEventosFolha.add(new EventosFolha(null, "429", "Prévia IRF s/ 13º Salário"));
        listaEventosFolha.add(new EventosFolha(null, "430", "Prévia Pensão Alimentícia"));
        listaEventosFolha.add(new EventosFolha(null, "431", "Prévia Pensão Alimentícia s/ Férias"));
        listaEventosFolha.add(new EventosFolha(null, "432", "Prévia Pensão Alimentícia s/ 13º Salário"));
        listaEventosFolha.add(new EventosFolha(null, "433", "Desconto Contribuição Assistencial"));
        
        return listaEventosFolha;
    }

    private List<TabelaCfop> criarListaCfopCompleta() {
        List<TabelaCfop> listaCfops = new ArrayList<>();
        listaCfops.add(new TabelaCfop(null, 11,"Compra para industrialização, produção rural, comercialização ou prestação de serviços", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1101,"Compra para industrialização ou produção rural", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1102,"Compra para comercialização", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1111,"Compra para industrialização de mercadoria recebida anteriormente em consignação industrial", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1113,"Compra para comercialização, de mercadoria recebida anteriormente em consignação mercantil", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1116,"Compra para industrialização ou produção rural originada de encomenda para recebimento futuro", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1117,"Compra para comercialização originada de encomenda para recebimento futuro", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1118,"Compra de mercadoria para comercialização pelo adquirente originário, entregue pelo vendedor remetente ao destinatário, em venda à ordem", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 112,"Compra para industrialização, em venda à ordem, já recebida do vendedor remetente", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1121,"Compra para comercialização, em venda à ordem, já recebida do vendedor remetente", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1122,"Compra para industrialização em que a mercadoria foi remetida pelo fornecedor ao industrializador sem transitar pelo estabelecimento adquirente", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1124,"Industrialização efetuada por outra empresa", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1125,"Industrialização efetuada por outra empresa quando a mercadoria remetida para utilização no processo de industrialização não transitou pelo estabelecimento adquirente da mercadoria", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1126,"Compra para utilização na prestação de serviço sujeita ao ICMS - Classificam-se neste código as entradas de mercadorias a serem utilizadas nas prestações de serviços sujeitas ao ICMS. ( Efeitos a partir de 1° de janeiro de 2011 )", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1128,"Compra para utilização na prestação de serviço sujeita ao ISSQN - Classificam-se neste código as entradas de mercadorias a serem utilizadas nas prestações de serviços sujeitas ao ISSQN. ( Efeitos a partir de 1° de janeiro de 2011 )", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 115,"Transferências para industrialização, produção rural, comercialização ou prestação de serviços", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1151,"Transferência para industrialização ou produção rural", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1152,"Transferência para comercialização", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1153,"Transferência de energia elétrica para distribuição", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1154,"Transferência para utilização na prestação de serviço de Transporte e de Comunicação", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 12,"DEVOLUÇÕES DE VENDAS DE PRODUÇÃO PRÓPRIA, DE TERCEIROS OU ANULAÇÕES DE VALORES", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1201,"Devolução de venda de produção do estabelecimento", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1202,"Devolução de venda de mercadoria adquirida ou recebida de terceiros", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1203,"Devolução de venda de produção do estabelecimento, destinada à Zona Franca de Manaus ou Áreas de Livre Comércio", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1204,"Devolução de venda de mercadoria adquirida ou recebida de terceiros, destinada à Zona Franca de Manaus ou Áreas de Livre Comércio", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1205,"Anulação de valor relativo à prestação de serviço de comunicação", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1206,"Anulação de valor relativo à prestação de serviço de transporte", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1207,"Anulação de valor relativo à venda de energia elétrica", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1208,"Devolução de produção do estabelecimento, remetida em transferência", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1209,"Devolução de mercadoria adquirida ou recebida de terceiros remetida em transferência", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 125,"COMPRAS DE ENERGIA ELÉTRICA", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1251,"Compra de energia elétrica para distribuição ou comercialização", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1252,"Compra de energia elétrica por estabelecimento industrial", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1253,"Compra de energia elétrica por estabelecimento comercial", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1254,"Compra de energia elétrica por estabelecimento prestador de serviço de transporte", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1255,"Compra de energia elétrica por estabelecimento prestador de serviço de comunicação", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1256,"Compra de energia elétrica por estabelecimento de produtor rural", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1257,"Compra de energia elétrica para consumo por demanda contratada", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 13,"AQUISIÇÕES DE SERVIÇOS DE COMUNICAÇÃO", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1301,"Aquisição de serviço de comunicação para execução de serviço da mesma natureza", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1302,"Aquisição de serviço de comunicação por estabelecimento industrial", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1303,"Aquisição de serviço de comunicação por estabelecimento comercial", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1304,"Aquisição de serviço de comunicação por estabelecimento de prestador de serviço de transporte", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1305,"Aquisição de serviço de comunicação por estabelecimento de geradora ou de distribuidora de energia elétrica", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1306,"Aquisição de serviço de comunicação por estabelecimento de produtor rural", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 135,"AQUISIÇÕES DE SERVIÇOS DE TRANSPORTE", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1351,"Aquisição de serviço de transporte para execução de serviço da mesma natureza", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1352,"Aquisição de serviço de transporte por estabelecimento industrial", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1353,"Aquisição de serviço de transporte por estabelecimento comercial", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1354,"Aquisição de serviço de transporte por estabelecimento de prestador de serviço de comunicação", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1355,"Aquisição de serviço de transporte por estabelecimento de geradora ou de distribuidora de energia elétrica", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1356,"Aquisição de serviço de transporte por estabelecimento de produtor rural", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 136,"Aquisição de serviço de transporte por contribuinte substituto em relação ao serviço de transporte", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 14,"Entrada DE MERCADORIAS SUJEITAS AO", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1401,"Compra para industrialização ou produção rural em operação com mercadoria sujeita ao regime de substituição tributária", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1403,"Compra para comercialização em operação com mercadoria sujeita ao regime de substituição tributária", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1406,"Compra de bem para o ativo imobilizado cuja mercadoria está sujeita ao regime de substituição tributária", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1407,"Compra de mercadoria para uso ou consumo cuja mercadoria está sujeita ao regime de substituição tributária", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1408,"Transferência para industrialização ou produção rural em operação com mercadoria sujeita ao regime de substituição tributária", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1409,"Transferência para comercialização em operação com mercadoria sujeita ao regime de substituição tributária", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 141,"Devolução de venda de produção do estabelecimento em operação com produto sujeito ao regime de substituição tributária", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1411,"Devolução de venda de mercadoria adquirida ou recebida de terceiros em operação com mercadoria sujeita ao regime de substituição tributária", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1414,"Retorno de produção do estabelecimento, remetida para venda fora do estabelecimento em operação com produto sujeito ao regime de substituição tributária", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1415,"Retorno de mercadoria adquirida ou recebida de terceiros, remetida para venda fora do estabelecimento em operação com mercadoria sujeita ao regime de substituição tributária", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 145,"SISTEMAS DE INTEGRAÇÃO", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1451,"Retorno de animal do estabelecimento produtor", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1452,"Retorno de insumo não utilizado na produção", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 15,"Entrada de mercadorias  remetidas  para formação de lote ou com fim específico de exportação e eventuais devoluções", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1501,"Entrada de mercadoria recebida com fim específico de exportação", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1503,"Entrada decorrente de devolução de produto remetido com fim específico de exportação, de produção do estabelecimento", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1504,"Entrada decorrente de devolução de mercadoria remetida com fim específico de exportação, adquirida ou recebida de terceiros", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1505,"Entrada decorrente de devolução simbólica de mercadorias remetidas para formação de lote de exportação, de produtos industrializados ou produzidos pelo próprio estabelecimento (efeitos a partir de 01.07.2006)", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1506,"Entrada decorrente de devolução simbólica de mercadorias, adquiridas ou recebidas de terceiros, remetidas para formação de lote de exportação (efeitos a partir de 01.07.2006)", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 155,"OPERAÇÕES COM BENS DE ATIVO IMOBILIZADO E MATERIAIS PARA USO OU CONSUMO", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1551,"Compra de bem para o ativo imobilizado", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1552,"Transferência de bem do ativo imobilizado", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1553,"Devolução de venda de bem do ativo imobilizado", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1554,"Retorno de bem do ativo imobilizado remetido para uso fora do estabelecimento", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1555,"Entrada de bem do ativo imobilizado de terceiro, remetido para uso no estabelecimento", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1556,"Compra de material para uso ou consumo", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1557,"Transferência de material para uso ou consumo", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 16,"Créditos E Ressarcimento DE ICMS", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1601,"Recebimento, por transferência, de crédito de ICMS", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1602,"Recebimento, por transferência, de saldo credor de ICMS de outro estabelecimento da mesma empresa, para compensação de saldo devedor de ICMS", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1603,"Ressarcimento de ICMS retido por substituição tributária", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1604,"Lançamento do crédito relativo à compra de bem para o ativo imobilizado", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1605,"Recebimento, por transferência, de saldo devedor de ICMS de outro estabelecimento da mesma empresa", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 165,"Entrada de combustíveis, derivados ou não de petróleo e lubrificantes", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1651,"Compra de combustível ou lubrificante para industrialização subsequente", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1652,"Compra de combustível ou lubrificante para comercialização", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1653,"Compra de combustível ou lubrificante por consumidor ou usuário final", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1658,"Transferência de combustível e lubrificante para industrialização", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1659,"Transferência de combustível e lubrificante para comercialização", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 166,"Devolução de venda de combustível ou lubrificante destinado à industrialização subsequente", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1661,"Devolução de venda de combustível ou lubrificante destinado à comercialização", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1662,"Devolução de venda de combustível ou lubrificante destinado a consumidor ou usuário final", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1663,"Entrada de combustível ou lubrificante para armazenagem", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1664,"Retorno de combustível ou lubrificante remetido para armazenagem", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 19,"Outras entradas de MERCADORIAS OU AQUISIÇÕES DE SERVIÇOS", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1901,"Entrada para industrialização por encomenda", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1902,"Retorno de mercadoria remetida para industrialização por encomenda", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1903,"Entrada de mercadoria remetida para industrialização e não aplicada no referido processo", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1904,"Retorno de remessa para venda fora do estabelecimento", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1905,"Entrada de mercadoria recebida para depósito em depósito fechado ou armazém-geral", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1906,"Retorno de mercadoria remetida para depósito fechado ou armazém-geral", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1907,"Retorno simbólico de mercadoria remetida para depósito fechado ou armazém-geral", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1908,"Entrada de bem por conta de contrato de comodato", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1909,"Retorno de bem remetido por conta de contrato de comodato", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 191,"Entrada de bonificação, doação ou brinde", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1911,"Entrada de amostra grátis", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1912,"Entrada de mercadoria ou bem recebido para demonstração", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1913,"Retorno de mercadoria ou bem remetido para demonstração", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1914,"Retorno de mercadoria ou bem remetido para exposição ou feira", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1915,"Entrada de mercadoria ou bem recebido para conserto ou reparo", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1916,"Retorno de mercadoria ou bem remetido para conserto ou reparo", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1917,"Entrada de mercadoria recebida em consignação mercantil ou industrial", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1918,"Devolução de mercadoria remetida em consignação mercantil ou industrial", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1919,"Devolução simbólica de mercadoria vendida ou utilizada em processo industrial, remetida anteriormente em consignação mercantil ou industrial", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 192,"Entrada de vasilhame ou sacaria", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1921,"Retorno de vasilhame ou sacaria", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1922,"Lançamento efetuado a título de simples faturamento decorrente de compra para recebimento futuro", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1923,"Entrada de mercadoria recebida do vendedor remetente, em venda à ordem", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1924,"Entrada para industrialização por conta e ordem do adquirente da mercadoria, quando esta não transitar pelo estabelecimento do adquirente", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1925,"Retorno de mercadoria remetida para industrialização por conta e ordem do adquirente da mercadoria, quando esta não transitar pelo estabelecimento do adquirente", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1926,"Lançamento efetuado a título de reclassificação de mercadoria decorrente de formação de kit ou de sua desagregação", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1931,"Lançamento efetuado pelo tomador do serviço de transporte quando a responsabilidade de retenção do imposto for atribuída ao remetente ou alienante da mercadoria, pelo serviço de transporte realizado por transportador autônomo ou por transportador não inscrito na unidade da Federação onde iniciado o serviço", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1932,"Aquisição de serviço de transporte iniciado em unidade da Federação diversa daquela onde inscrito o prestador", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1933,"Aquisição de serviço tributado pelo ISSQN", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1934,"Entrada simbólica de mercadoria recebida para depósito fechado ou armazém geral", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 1949,"Outra Entrada de mercadoria ou prestação de serviço não especificada", "Interno", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 21,"COMPRA PARA INDUSTRIALIZAÇÃO, PRODUÇÃO RURAL, COMERCIALIZAÇÃO OU PRESTAÇÃO DE SERVIÇO", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2101,"Compra para industrialização ou produção rural", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2102,"Compra para comercialização", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2111,"Compra para industrialização de mercadoria recebida anteriormente em consignação industrial", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2113,"Compra para comercialização, de mercadoria recebida anteriormente em consignação mercantil", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2116,"Compra para industrialização ou produção rural originada de encomenda para recebimento futuro", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2117,"Compra para comercialização originada de encomenda para recebimento futuro", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2118,"Compra de mercadoria para comercialização pelo adquirente originário, entregue pelo vendedor remetente ao destinatário, em venda à ordem", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 212,"Compra para industrialização, em venda à ordem, já recebida do vendedor remetente", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2121,"Compra para comercialização, em venda à ordem, já recebida do vendedor remetente", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2122,"Compra para industrialização em que a mercadoria foi remetida pelo fornecedor ao industrializador sem transitar pelo estabelecimento adquirente", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2124,"Industrialização efetuada por outra empresa", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2125,"Industrialização efetuada por outra empresa quando a mercadoria remetida para utilização no processo de industrialização não transitou pelo estabelecimento adquirente da mercadoria", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2126,"Compra para utilização na prestação de serviço sujeita ao ICMS - Classificam-se neste código as Entradas de mercadorias a serem utilizadas nas prestações de serviços sujeitas ao ICMS.( Efeitos a partir de 1° de janeiro de 2011 )", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2128,"Compra para utilização na prestação de serviço sujeita ao ISSQN - Classificam-se neste código as entradas de mercadorias a serem utilizadas nas prestações de serviços sujeitas ao ISSQN.( Efeitos a partir de 1° de janeiro de 2011 )", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 215,"TRANSFERÊNCIAS PARA INDUSTRIALIZAÇÃO, PRODUÇÃO RURAL, COMERCIALIZAÇÃO OU PRESTAÇÃO DE SERVIÇOS", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2151,"Transferência para industrialização ou produção rural", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2152,"Transferência para comercialização", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2153,"Transferência de energia elétrica para distribuição", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2154,"Transferência para utilização na prestação de serviço de Transporte e de Comunicação", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 22,"DEVOLUÇÕES DE VENDAS DE PRODUÇÃO PRÓPRIA, DE TERCEIROS OU ANULAÇÕES DE VALORES", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2201,"Devolução de venda de produção do estabelecimento", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2202,"Devolução de venda de mercadoria adquirida ou recebida de terceiros", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2203,"Devolução de venda de produção do estabelecimento, destinada à Zona Franca de Manaus ou Áreas de Livre Comércio", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2204,"Devolução de venda de mercadoria adquirida ou recebida de terceiros, destinada à Zona Franca de Manaus ou Áreas de Livre Comércio", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2205,"Anulação de valor relativo à prestação de serviço de comunicação", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2206,"Anulação de valor relativo à prestação de serviço de transporte", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2207,"Anulação de valor relativo à venda de energia elétrica", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2208,"Devolução de produção do estabelecimento, remetida em transferência", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2209,"Devolução de mercadoria adquirida ou recebida de terceiros remetida em transferência", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 225,"COMPRAS DE ENERGIA ELÉTRICA", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2251,"Compra de energia elétrica para distribuição ou comercialização", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2252,"Compra de energia elétrica por estabelecimento industrial", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2253,"Compra de energia elétrica por estabelecimento comercial", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2254,"Compra de energia elétrica por estabelecimento prestador de serviço de transporte", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2255,"Compra de energia elétrica por estabelecimento prestador de serviço de comunicação", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2256,"Compra de energia elétrica por estabelecimento de produtor rural", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2257,"Compra de energia elétrica para consumo por demanda contratada", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 23,"AQUISIÇÕES DE SERVIÇOS DE COMUNICAÇÃO", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2301,"Aquisição de serviço de comunicação para execução de serviço da mesma natureza", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2302,"Aquisição de serviço de comunicação por estabelecimento industrial", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2303,"Aquisição de serviço de comunicação por estabelecimento comercial", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2304,"Aquisição de serviço de comunicação por estabelecimento de prestador de serviço de transporte", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2305,"Aquisição de serviço de comunicação por estabelecimento de geradora ou de distribuidora de energia elétrica", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2306,"Aquisição de serviço de comunicação por estabelecimento de produtor rural", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 235,"AQUISIÇÕES DE SERVIÇOS DE TRANSPORTE", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2351,"Aquisição de serviço de transporte para execução de serviço da mesma natureza", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2352,"Aquisição de serviço de transporte por estabelecimento industrial", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2353,"Aquisição de serviço de transporte por estabelecimento comercial", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2354,"Aquisição de serviço de transporte por estabelecimento de prestador de serviço de comunicação", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2355,"Aquisição de serviço de transporte por estabelecimento de geradora ou de distribuidora de energia elétrica", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2356,"Aquisição de serviço de transporte por estabelecimento de produtor rural", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 24,"Entrada DE MERCADORIAS SUJEITAS AO", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2401,"Compra para industrialização ou produção rural em operação com mercadoria sujeita ao regime de substituição tributária", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2403,"Compra para comercialização em operação com mercadoria sujeita ao regime de substituição tributária", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2406,"Compra de bem para o ativo imobilizado cuja mercadoria está sujeita ao regime de substituição tributária", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2407,"Compra de mercadoria para uso ou consumo cuja mercadoria está sujeita ao regime de substituição tributária", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2408,"Transferência para industrialização ou produção rural em operação com mercadoria sujeita ao regime de substituição tributária", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2409,"Transferência para comercialização em operação com mercadoria sujeita ao regime de substituição tributária", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 241,"Devolução de venda de produção do estabelecimento em operação com produto sujeito ao regime de substituição tributária", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2411,"Devolução de venda de mercadoria adquirida ou recebida de terceiros em operação com mercadoria sujeita ao regime de substituição tributária", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2414,"Retorno de produção do estabelecimento, remetida para venda fora do estabelecimento em operação com produto sujeito ao regime de substituição tributária", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2415,"Retorno de mercadoria adquirida ou recebida de terceiros, remetida para venda fora do estabelecimento em operação com mercadoria sujeita ao regime de substituição tributária", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 25,"Entrada  DE  MERCADORIAS  REMETIDAS  PARA FORMAÇÃO DE LOTE OU COM FIM ESPECÍFICO DE EXPORTAÇÃO E EVENTUAIS DEVOLUÇÕES", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2501,"Entrada de mercadoria recebida com fim específico de exportação", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2503,"Entrada decorrente de devolução de produto remetido com fim específico de exportação, de produção do estabelecimento", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2504,"Entrada decorrente de devolução de mercadoria remetida com fim específico de exportação, adquirida ou recebida de terceiros", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2505,"Entrada decorrente de devolução simbólica de mercadorias remetidas para formação de lote de exportação, de produtos industrializados ou produzidos pelo próprio estabelecimento (efeitos a partir de 01.07.2006)", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2506,"Entrada decorrente de devolução simbólica de mercadorias, adquiridas ou recebidas de terceiros, remetidas para formação de lote de exportação (efeitos a partir de 01.07.2006)", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 255,"OPERAÇÕES COM BENS DE ATIVO IMOBILIZADO E MATERIAIS PARA USO OU CONSUMO", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2551,"Compra de bem para o ativo imobilizado", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2552,"Transferência de bem do ativo imobilizado", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2553,"Devolução de venda de bem do ativo imobilizado", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2554,"Retorno de bem do ativo imobilizado remetido para uso fora do estabelecimento", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2555,"Entrada de bem do ativo imobilizado de terceiro, remetido para uso no estabelecimento", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2556,"Compra de material para uso ou consumo", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2557,"Transferência de material para uso ou consumo", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 26,"CRÉDITOS E RESSARCIMENTOS DE ICMS", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2603,"Ressarcimento de ICMS retido por substituição tribuária", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 265,"Entrada DE COMBUSTÍVEIS, DERIVADOS OU NÃO DE PETRÓLEO E LUBRIFICANTES", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2651,"Compra de combustível ou lubrificante para industrialização subsequente", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2652,"Compra de combustível ou lubrificante para comercialização", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2653,"Compra de combustível ou lubrificante por consumidor ou usuário final", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2658,"Transferência de combustível e lubrificante para industrialização", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2659,"Transferência de combustível e lubrificante para comercialização", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 266,"Devolução de venda de combustível ou lubrificante destinado à industrialização subsequente", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2661,"Devolução de venda de combustível ou lubrificante destinado à comercialização", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2662,"Devolução de venda de combustível ou lubrificante destinado a consumidor ou usuário final", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2663,"Entrada de combustível ou lubrificante para armazenagem", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2664,"Retorno de combustível ou lubrificante remetido para armazenagem", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 29,"Outras entradas de MERCADORIAS OU AQUISIÇÕES DE SERVIÇOS", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2901,"Entrada para industrialização por encomenda", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2902,"Retorno de mercadoria remetida para industrialização por encomenda", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2903,"Entrada de mercadoria remetida para industrialização e não aplicada no referido processo", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2904,"Retorno de remessa para venda fora do estabelecimento", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2905,"Entrada de mercadoria recebida para depósito em depósito fechado ou armazém-geral", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2906,"Retorno de mercadoria remetida para depósito fechado ou armazém-geral", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2907,"Retorno simbólico de mercadoria remetida para depósito fechado ou armazém-geral", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2908,"Entrada de bem por conta de contrato de comodato", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2909,"Retorno de bem remetido por conta de contrato de comodato", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 291,"Entrada de bonificação, doação ou brinde", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2911,"Entrada de amostra grátis", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2912,"Entrada de mercadoria ou bem recebido para demonstração", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2913,"Retorno de mercadoria ou bem remetido para demonstração", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2914,"Retorno de mercadoria ou bem remetido para exposição ou feira", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2915,"Entrada de mercadoria ou bem recebido para conserto ou reparo", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2916,"Retorno de mercadoria ou bem remetido para conserto ou reparo", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2917,"Entrada de mercadoria recebida em consignação mercantil ou industrial", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2918,"Devolução de mercadoria remetida em consignação mercantil ou industrial", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2919,"Devolução simbólica de mercadoria vendida ou utilizada em processo industrial, remetida anteriormente em consignação mercantil ou industrial", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 292,"Entrada de vasilhame ou sacaria", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2921,"Retorno de vasilhame ou sacaria", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2922,"Lançamento efetuado a título de simples faturamento decorrente de compra para recebimento futuro", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2923,"Entrada de mercadoria recebida do vendedor remetente, em venda à ordem", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2924,"Entrada para industrialização por conta e ordem do adquirente da mercadoria, quando esta não transitar pelo estabelecimento do adquirente", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2925,"Retorno de mercadoria remetida para industrialização por conta e ordem do adquirente da mercadoria, quando esta não transitar pelo estabelecimento do adquirente", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2931,"Lançamento efetuado pelo tomador do serviço de transporte quando a responsabilidade de retenção do imposto for atribuída ao remetente ou alienante da mercadoria, pelo serviço de transporte realizado por transportador autônomo ou por transportador não inscrito na unidade da Federação onde iniciado o serviço", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2932,"Aquisição de serviço de transporte iniciado em unidade da Federação diversa daquela onde inscrito o prestador", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2933,"Aquisição de serviço tributado pelo ISSQN", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2934,"Entrada simbólica de mercadoria recebida para depósito fechado ou armazém geral", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 2949,"Outra Entrada de mercadoria ou prestação de serviço não especificada", "Interestadual", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 31,"COMPRA PARA INDUSTRIALIZAÇÃO, PRODUÇÃO RURAL, COMERCIALIZAÇÃO OU PRESTAÇÃO DE SERVIÇO", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 3101,"Compra para industrialização ou produção rural", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 3102,"Compra para comercialização", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 3126,"Compra para utilização na prestação de serviço sujeita ao ICMS - Classificam-se neste código as entradas de mercadorias a serem utilizadas nas prestações de serviços sujeitas ao ICMS. ( Efeitos a partir de 1° de janeiro de 2011 )", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 3128,"Compra para utilização na prestação de serviço sujeita ao ISSQN - Classificam-se neste código as entradas de mercadorias a serem utilizadas nas prestações de serviços sujeitas ao ISSQN. ( Efeitos a partir de 1° de janeiro de 2011 )", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 3127,"Compra para industrialização sob o regime de drawback", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 32,"DEVOLUÇÕES DE VENDAS DE PRODUÇÃO PRÓPRIA, DE TERCEIROS OU ANULAÇÕES DE VALORES", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 3201,"Devolução de venda de produção do estabelecimento", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 3202,"Devolução de venda de mercadoria adquirida ou recebida de terceiros", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 3205,"Anulação de valor relativo à prestação de serviço de comunicação", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 3206,"Anulação de valor relativo à prestação de serviço de transporte", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 3207,"Anulação de valor relativo à venda de energia elétrica", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 3211,"Devolução de venda de produção do estabelecimento sob o regime de drawback", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 325,"COMPRAS DE ENERGIA ELÉTRICA", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 3251,"Compra de energia elétrica para distribuição ou comercialização", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 33,"AQUISIÇÕES DE SERVIÇOS DE COMUNICAÇÃO", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 3301,"Aquisição de serviço de comunicação para execução de serviço da mesma natureza", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 335,"AQUISIÇÕES DE SERVIÇOS DE TRANSPORTE", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 3351,"Aquisição de serviço de transporte para execução de serviço da mesma natureza", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 3352,"Aquisição de serviço de transporte por estabelecimento industrial", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 3353,"Aquisição de serviço de transporte por estabelecimento comercial", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 3354,"Aquisição de serviço de transporte por estabelecimento de prestador de serviço de comunicação", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 3355,"Aquisição de serviço de transporte por estabelecimento de geradora ou de distribuidora de energia elétrica", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 3356,"Aquisição de serviço de transporte por estabelecimento de produtor rural", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 35,"Entrada  DE  MERCADORIAS  REMETIDAS  PARA FORMAÇÃO DE LOTE OU COM FIM ESPECÍFICO DE EXPORTAÇÃO E EVENTUAIS DEVOLUÇÕES", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 3503,"Devolução de mercadoria exportada que tenha sido recebida com fim específico de exportação", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 355,"OPERAÇÕES COM BENS DE ATIVO IMOBILIZADO E MATERIAIS PARA USO OU CONSUMO", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 3551,"Compra de bem para o ativo imobilizado", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 3553,"Devolução de venda de bem do ativo imobilizado", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 3556,"Compra de material para uso ou consumo", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 365,"Entrada DE COMBUSTÍVEIS, DERIVADOS OU NÃO DE PETRÓLEO E LUBRIFICANTES", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 3651,"Compra de combustível ou lubrificante para industrialização subsequente", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 3652,"Compra de combustível ou lubrificante para comercialização", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 3653,"Compra de combustível ou lubrificante por consumidor ou usuário final", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 39,"OUTRAS Entradaa DE MERCADORIAS OU AQUISIÇÕES DE SERVIÇOS", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 393,"Lançamento efetuado a título de Entrada de bem sob amparo de regime especial aduaneiro de admissão temporária", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 3949,"Outra Entrada de mercadoria ou prestação de serviço não especificada", "Externior", "Entrada"));
        listaCfops.add(new TabelaCfop(null, 51,"VENDA DE PRODUÇÃO PRÓPRIA OU DE TERCEIROS", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5101,"Venda de produção do estabelecimento", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5102,"Venda de mercadoria adquirida ou recebida de terceiros", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5103,"Venda de produção do estabelecimento, efetuada fora do estabelecimento", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5104,"Venda de mercadoria adquirida ou recebida de terceiros, efetuada fora do estabelecimento", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5105,"Venda de produção do estabelecimento que não deva por ele transitar", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5106,"Venda de mercadoria adquirida ou recebida de terceiros, que não deva por ele transitar", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5109,"Venda de produção do estabelecimento, destinada à Zona Franca de Manaus ou Áreas de Livre Comércio", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 511,"Venda de mercadoria adquirida ou recebida de terceiros, destinada à Zona Franca de Manaus ou Áreas de Livre Comércio", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5111,"Venda de produção do estabelecimento remetida anteriormente em consignação industrial", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5112,"Venda de mercadoria adquirida ou recebida de terceiros remetida anteriormente em consignação industrial", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5113,"Venda de produção do estabelecimento remetida anteriormente em consignação mercantil", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5114,"Venda de mercadoria adquirida ou recebida de terceiros remetida anteriormente em consignação mercantil", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5115,"Venda de mercadoria adquirida ou recebida de terceiros, recebida anteriormente em consignação mercantil", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5116,"Venda de produção do estabelecimento originada de encomenda para entrega futura", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5117,"Venda de mercadoria adquirida ou recebida de terceiros, originada de encomenda para entrega futura", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5118,"Venda de produção do estabelecimento entregue ao destinatário por conta e ordem do adquirente originário, em venda à ordem", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5119,"Venda de mercadoria adquirida ou recebida de terceiros entregue ao destinatário por conta e ordem do adquirente originário, em venda à ordem", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 512,"Venda de mercadoria adquirida ou recebida de terceiros entregue ao destinatário pelo vendedor remetente, em venda à ordem", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5122,"Venda de produção do estabelecimento remetida para industrialização, por conta e ordem do adquirente, sem transitar pelo estabelecimento do adquirente", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5123,"Venda de mercadoria adquirida ou recebida de terceiros remetida para industrialização, por conta e ordem do adquirente, sem transitar pelo estabelecimento do adquirente", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5124,"Industrialização efetuada para outra empresa", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5125,"Industrialização efetuada para outra empresa quando a mercadoria recebida para utilização no processo não transitar pelo estabelecimento adquirente", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 515,"TRANSFERÊNCIAS DE PRODUÇÃO PRÓPRIA OU DE TERCEIROS", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5151,"Transferência de produção do estabelecimento", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5152,"Transferência de mercadoria adquirida ou recebida de terceiros", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5153,"Transferência de energia elétrica", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5155,"Transferência de produção do estabelecimento, que não deva por ele transitar", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5156,"Transferência de mercadoria adquirida ou recebida de terceiros, que não deva por ele transitar", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 52,"DEVOLUÇÕES DE COMPRAS PARA INDUSTRIALIZAÇÃO, PRODUÇÃO RURAL, COMERCIALIZAÇÃO OU ANULAÇÕES DE VALORES", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5201,"Devolução de compra para industrialização ou produção rural", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5202,"Devolução de compra para comercialização", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5205,"Anulação de valor relativo a aquisição de serviço de comunicação", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5206,"Anulação de valor relativo a aquisição de serviço de transporte", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5207,"Anulação de valor relativo à compra de energia elétrica", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5208,"Devolução de mercadoria recebida em transferência para industrialização ou produção rural", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5209,"Devolução de mercadoria recebida em transferência para comercialização", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 521,"Devolução de compra para utilização na prestação de serviço - Classificam-se neste código as devoluções de mercadorias adquiridas para utilização na prestação de serviços, cujas entradas tenham sido classificadas nos códigos 1.126 - Compra para utilização na prestação de serviço sujeita ao ICMS e 1.128 – Compra para utilização na prestação de serviço sujeita ao ISSQN. ( Efeitos a partir de 1° de janeiro de 2011 )", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 525,"VENDAS DE ENERGIA ELÉTRICA", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5251,"Venda de energia elétrica para distribuição ou comercialização", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5252,"Venda de energia elétrica para estabelecimento industrial", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5253,"Venda de energia elétrica para estabelecimento comercial", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5254,"Venda de energia elétrica para estabelecimento prestador de serviço de transporte", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5255,"Venda de energia elétrica para estabelecimento prestador", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5256,"Venda de energia elétrica para estabelecimento de produtor rural", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5257,"Venda de energia elétrica para consumo por demanda contratada", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5258,"Venda de energia elétrica a não contribuinte", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 53,"PRESTAÇÕES DE SERVIÇOS DE COMUNICAÇÃO", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5301,"Prestação de serviço de comunicação para execução de serviço da mesma natureza", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5302,"Prestação de serviço de comunicação a estabelecimento industrial", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5303,"Prestação de serviço de comunicação a estabelecimento comercial", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5304,"Prestação de serviço de comunicação a estabelecimento de prestador de serviço de transporte", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5305,"Prestação de serviço de comunicação a estabelecimento de geradora ou de distribuidora de energia elétrica", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5306,"Prestação de serviço de comunicação a estabelecimento de produtor rural", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5307,"Prestação de serviço de comunicação a não contribuinte", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 535,"PRESTAÇÕES DE SERVIÇOS DE TRANSPORTE", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5351,"Prestação de serviço de transporte para execução de serviço da mesma natureza", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5352,"Prestação de serviço de transporte a estabelecimento industrial", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5353,"Prestação de serviço de transporte a estabelecimento comercial", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5354,"Prestação de serviço de transporte a estabelecimento de prestador de serviço de comunicação", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5355,"Prestação de serviço de transporte a estabelecimento de geradora ou de distribuidora de energia elétrica", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5356,"Prestação de serviço de transporte a estabelecimento de produtor rural", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5357,"Prestação de serviço de transporte a não contribuinte", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5359,"Prestação de serviço de transporte a contribuinte ou a não contribuinte quando a mercadoria transportada está dispensada de emissão de nota fiscal", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 536,"Prestação de serviço de transporte a contribuinte substituto em relação ao serviço de transporte Classificam-se  neste  código  as  prestações  de  serviços  de transporte  a  contribuinte  ao  qual  tenha  sido  atribuída  a condição   de   substituto   tributário   do   imposto   sobre   a prestação dos serviços.", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 536,"Prestação de serviço de transporte a contribuinte substituto em relação ao serviço de transporte", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 54,"Saídas DE MERCADORIAS SUJEITAS AO REGIME DE SUBSTITUIÇÃO TRIBUTÁRIA", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5401,"Venda de produção do estabelecimento em operação com produto sujeito ao regime de substituição tributária, na condição de contribuinte substituto", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5402,"Venda de produção do estabelecimento de produto sujeito ao regime de substituição tributária, em operação entre contribuintes substitutos do mesmo produto", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5403,"Venda de mercadoria adquirida ou recebida de terceiros em operação com mercadoria sujeita ao regime de substituição tributária, na condição de contribuinte substituto", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5405,"Venda de mercadoria adquirida ou recebida de terceiros em operação com mercadoria sujeita ao regime de substituição tributária, na condição de contribuinte substituído", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5408,"Transferência de produção do estabelecimento em operação com produto sujeito ao regime de substituição tributária", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5409,"Transferência de mercadoria adquirida ou recebida de terceiros em operação com mercadoria sujeita ao regime de substituição tributária", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 541,"Devolução de compra para industrialização, produção rural, em operação com mercadoria sujeita ao regime de substituição tributária", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5411,"Devolução de compra para comercialização em operação com mercadoria sujeita ao regime de substituição tributária", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5412,"Devolução de bem do ativo imobilizado, em operação com mercadoria sujeita ao regime de substituição tributária", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5413,"Devolução de mercadoria destinada ao uso ou consumo, em operação com mercadoria sujeita ao regime de substituição tributária", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5414,"Remessa de produção do estabelecimento para venda fora do estabelecimento em operação com produto sujeito ao regime de substituição tributária", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5415,"Remessa de mercadoria adquirida ou recebida de terceiros para venda fora do estabelecimento, em operação com mercadoria sujeita ao regime de substituição tributária", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 545,"SISTEMAS DE INTEGRAÇÃO", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5451,"Remessa de animal e de insumo para estabelecimento produtor", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 55,"REMESSAS  PARA  FORMAÇÃO  DE  LOTE  E  COM  FIM ESPECÍFICO     DE     EXPORTAÇÃO     E     EVENTUAIS DEVOLUÇÕES", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5501,"Remessa de produção do estabelecimento, com fim específico de exportação", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5502,"Remessa de mercadoria adquirida ou recebida de terceiros, com fim específico de exportação", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5503,"Devolução de mercadoria recebida com fim específico de exportação", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5504,"Remessa de mercadorias para formação de lote de exportação, de produtos industrializados ou produzidos pelo próprio estabelecimento (efeitos a partir de 01.07.2006)", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5505,"Remessa de mercadorias, adquiridas ou recebidas de terceiros, para formação de lote de exportação (efeitos a partir de 01.07.2006)", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 555,"OPERAÇÕES COM BENS DE ATIVO IMOBILIZADO E MATERIAIS PARA USO OU CONSUMO", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5551,"Venda de bem do ativo imobilizado", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5552,"Transferência de bem do ativo imobilizado", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5553,"Devolução de compra de bem para o ativo imobilizado", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5554,"Remessa de bem do ativo imobilizado para uso fora do estabelecimento", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5555,"Devolução de bem do ativo imobilizado de terceiro, recebido para uso no estabelecimento", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5556,"Devolução de compra de material de uso ou consumo", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5557,"Transferência de material de uso ou consumo", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 56,"CRÉDITOS E RESSARCIMENTOS DE ICMS", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5601,"Transferência de crédito de ICMS acumulado", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5602,"Transferência de saldo credor de ICMS para outro estabelecimento da mesma empresa, destinado à compensação de saldo devedor de ICMS", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5603,"Ressarcimento de ICMS retido por substituição tributária", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5605,"Transferência de saldo devedor de ICMS de outro estabelecimento da mesma empresa", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5606,"Utilização de saldo credor de ICMS para extinção por compensação de débitos fiscais", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 565,"Saídas DE COMBUSTÍVEIS, DERIVADOS OU NÃO DE PETRÓLEO E LUBRIFICANTES", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5651,"Venda de combustível ou lubrificante de produção do estabelecimento destinado à industrialização subsequente", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5652,"Venda de combustível ou lubrificante de produção do estabelecimento destinado à comercialização", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5653,"Venda de combustível ou lubrificante de produção do", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5654,"Venda de combustível ou lubrificante adquirido ou recebido de terceiros destinado à industrialização subsequente", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5655,"Venda de combustível ou lubrificante adquirido ou recebido de terceiros destinado à comercialização", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5656,"Venda de combustível ou lubrificante adquirido ou recebido de terceiros destinado a consumidor ou usuário final", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5657,"Remessa de combustível ou lubrificante adquirido ou recebido de terceiros para venda fora do estabelecimento", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5658,"Transferência de combustível ou lubrificante de produção do estabelecimento", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5659,"Transferência de combustível ou lubrificante adquirido ou recebido de terceiro", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 566,"Devolução de compra de combustível ou lubrificante adquirido para industrialização subsequente", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5661,"Devolução de compra de combustível ou lubrificante adquirido para comercialização", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5662,"Devolução de compra de combustível ou lubrificante adquirido por consumidor ou usuário final", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5663,"Remessa para armazenagem de combustível ou lubrificante", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5664,"Retorno de combustível ou lubrificante recebido para armazenagem", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5665,"Retorno simbólico de combustível ou lubrificante recebido para armazenagem", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5666,"Remessa por conta e ordem de terceiros de combustível ou lubrificante recebido para armazenagem", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 59,"OUTRAS SAÍDAS DE MERCADORIAS OU PRESTAÇÕES DE SERVIÇOS", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5901,"Remessa para industrialização por encomenda", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5902,"Retorno de mercadoria utilizada na industrialização por encomenda", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5903,"Retorno de mercadoria recebida para industrialização e não aplicada no referido processo", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5904,"Remessa para venda fora do estabelecimento", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5905,"Remessa para depósito fechado ou armazém-geral", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5906,"Retorno de mercadoria depositada em depósito fechado ou armazém-geral", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5907,"Retorno simbólico de mercadoria depositada em depósito fechado ou armazém-geral", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5908,"Remessa de bem por conta de contrato de comodato", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5909,"Retorno de bem recebido por conta de contrato de comodato", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 591,"Remessa em bonificação, doação ou brinde", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5911,"Remessa de amostra grátis", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5912,"Remessa de mercadoria ou bem para demonstração", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5913,"Retorno de mercadoria ou bem recebido para demonstração", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5914,"Remessa de mercadoria ou bem para exposição ou feira", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5915,"Remessa de mercadoria ou bem para conserto ou reparo", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5916,"Retorno de mercadoria ou bem recebido para conserto ou reparo", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5917,"Remessa de mercadoria em consignação mercantil ou industrial", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5918,"Devolução de mercadoria recebida em consignação mercantil ou industrial", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5919,"Devolução simbólica de mercadoria vendida ou utilizada em processo industrial, recebida anteriormente em consignação mercantil ou industrial", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 592,"Remessa de vasilhame ou sacaria", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5921,"Devolução de vasilhame ou sacaria", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5922,"Lançamento efetuado a título de simples faturamento decorrente de venda para entrega futura", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5923,"Remessa de mercadoria por conta e ordem de terceiros, em venda à ordem", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5923,"Remessa de mercadoria por conta e ordem de terceiros, em venda à ordem ou em operações com", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5924,"Remessa para industrialização por conta e ordem do adquirente da mercadoria, quando esta não transitar pelo estabelecimento do adquirente", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5925,"Retorno de mercadoria recebida para industrialização por conta e ordem do adquirente da mercadoria, quando aquela não transitar pelo estabelecimento do adquirente", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5926,"Lançamento efetuado a título de reclassificação de mercadoria decorrente de formação de kit ou de sua desagregação", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5927,"Lançamento efetuado a título de baixa de estoque decorrente de perda, roubo ou deterioração", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5928,"Lançamento efetuado a título de baixa de estoque decorrente do encerramento da atividade da empresa", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5929,"Lançamento efetuado em decorrência de emissão de documento fiscal relativo a operação ou prestação também registrada em equipamento Emissor de Cupom Fiscal – ECF", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5931,"Lançamento efetuado em decorrência da responsabilidade de retenção do imposto por substituição tributária, atribuída ao remetente ou alienante da mercadoria, pelo serviço de transporte realizado por transportador autônomo ou por transportador não inscrito na unidade da Federação onde iniciado o serviço", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5932,"Prestação de serviço de transporte iniciada em unidade da Federação diversa daquela onde inscrito o prestador", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5933,"Prestação de serviço tributado pelo ISSQN", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5934,"Remessa simbólica de mercadoria depositada em armazém geral ou depósito fechado.", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 5949,"Outra saída de mercadoria ou prestação de serviço não especificado", "Interno", "Saída"));
        listaCfops.add(new TabelaCfop(null, 61,"VENDA DE PRODUÇÃO PRÓPRIA OU DE TERCEIROS", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6101,"Venda de produção do estabelecimento", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6102,"Venda de mercadoria adquirida ou recebida de terceiros", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6103,"Venda de produção do estabelecimento, efetuada fora do estabelecimento", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6104,"Venda de mercadoria adquirida ou recebida de terceiros, efetuada fora do estabelecimento", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6105,"Venda de produção do estabelecimento que não deva por ele transitar", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6106,"Venda de mercadoria adquirida ou recebida de terceiros, que não deva por ele transitar", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6107,"Venda de produção do estabelecimento, destinada a não contribuinte", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6108,"Venda de mercadoria adquirida ou recebida de terceiros, destinada a não contribuinte", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6109,"Venda de produção do estabelecimento, destinada à Zona Franca de Manaus ou Áreas de Livre Comércio", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 611,"Venda de mercadoria adquirida ou recebida de terceiros, destinada à Zona Franca de Manaus ou Áreas de Livre Comércio", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6111,"Venda de produção do estabelecimento remetida anteriormente em consignação industrial", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6112,"Venda de mercadoria adquirida ou recebida de terceiros remetida anteriormente em consignação industrial", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6113,"Venda de produção do estabelecimento remetida anteriormente em consignação mercantil", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6114,"Venda de mercadoria adquirida ou recebida de terceiros remetida anteriormente em consignação mercantil", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6115,"Venda de mercadoria adquirida ou recebida de terceiros, recebida anteriormente em consignação mercantil", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6116,"Venda de produção do estabelecimento originada de encomenda para entrega futura", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6117,"Venda de mercadoria adquirida ou recebida de terceiros, originada de encomenda para entrega futura", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6118,"Venda de produção do estabelecimento entregue ao destinatário por conta e ordem do adquirente originário, em venda à ordem", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6119,"Venda de mercadoria adquirida ou recebida de terceiros entregue ao destinatário por conta e ordem do adquirente originário, em venda à ordem", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 612,"Venda de mercadoria adquirida ou recebida de terceiros entregue ao destinatário pelo vendedor remetente, em venda à ordem", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6122,"Venda de produção do estabelecimento remetida para industrialização, por conta e ordem do adquirente, sem transitar pelo estabelecimento do adquirente", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6123,"Venda de mercadoria adquirida ou recebida de terceiros remetida para industrialização, por conta e ordem do adquirente, sem transitar pelo estabelecimento do adquirente", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6124,"Industrialização efetuada para outra empresa", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6125,"Industrialização efetuada para outra empresa quando a mercadoria recebida para utilização no processo não transitar pelo estabelecimento adquirente", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 615,"TRANSFERÊNCIAS DE PRODUÇÃO PRÓPRIA OU DE TERCEIROS", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6151,"Transferência de produção do estabelecimento", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6152,"Transferência de mercadoria adquirida ou recebida de terceiros", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6153,"Transferência de energia elétrica", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6155,"Transferência de produção do estabelecimento, que não deva por ele transitar", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6156,"Transferência de mercadoria adquirida ou recebida de terceiros, que não deva por ele transitar", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 62,"DEVOLUÇÕES DE COMPRAS PARA INDUSTRIALIZAÇÃO, PRODUÇÃO RURAL, COMERCIALIZAÇÃO OU ANULAÇÕES DE VALORES", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6201,"Devolução de compra para industrialização ou produção rural", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6202,"Devolução de compra para comercialização", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6205,"Anulação de valor relativo a aquisição de serviço de comunicação", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6206,"Anulação de valor relativo a aquisição de serviço de transporte", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6207,"Anulação de valor relativo à compra de energia elétrica", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6208,"Devolução de mercadoria recebida em transferência para industrialização ou produção rural", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6209,"Devolução de mercadoria recebida em transferência para comercialização", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 621,"Devolução de compra para utilização na prestação de serviço - Classificam-se neste código as devoluções de mercadorias adquiridas para utilização na prestação de serviços, cujas entradas tenham sido classificadas nos códigos 1.126 - Compra para utilização na prestação de serviço sujeita ao ICMS e 1.128 – Compra para utilização na prestação de serviço sujeita ao ISSQN. ( Efeitos a partir de 1° de janeiro de 2011 )", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 625,"VENDAS DE ENERGIA ELÉTRICA", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6251,"Venda de energia elétrica para distribuição ou comercialização", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6252,"Venda de energia elétrica para estabelecimento industrial", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6253,"Venda de energia elétrica para estabelecimento comercial", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6254,"Venda de energia elétrica para estabelecimento prestador de serviço de transporte", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6255,"Venda de energia elétrica para estabelecimento prestador", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6256,"Venda de energia elétrica para estabelecimento de produtor rural", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6257,"Venda de energia elétrica para consumo por demanda  contratada", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6258,"Venda de energia elétrica a não contribuinte", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 63,"PRESTAÇÕES DE SERVIÇOS DE COMUNICAÇÃO", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6301,"Prestação de serviço de comunicação para execução de serviço da mesma natureza", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6302,"Prestação de serviço de comunicação a estabelecimento industrial", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6303,"Prestação de serviço de comunicação a estabelecimento comercial", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6304,"Prestação de serviço de comunicação a estabelecimento de prestador de serviço de transporte", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6305,"Prestação de serviço de comunicação a estabelecimento de geradora ou de distribuidora de energia elétrica", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6306,"Prestação de serviço de comunicação a estabelecimento de produtor rural", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6307,"Prestação de serviço de comunicação a não contribuinte", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 635,"PRESTAÇÕES DE SERVIÇOS DE TRANSPORTE", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6351,"Prestação de serviço de transporte para execução de serviço da mesma natureza", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6352,"Prestação de serviço de transporte a estabelecimento industrial", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6353,"Prestação de serviço de transporte a estabelecimento comercial", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6354,"Prestação de serviço de transporte a estabelecimento de prestador de serviço de comunicação", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6355,"Prestação de serviço de transporte a estabelecimento de geradora ou de distribuidora de energia elétrica", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6356,"Prestação de serviço de transporte a estabelecimento de produtor rural", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6357,"Prestação de serviço de transporte a não contribuinte", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6359,"Prestação de serviço de transporte a contribuinte ou a não contribuinte quando a mercadoria transportada está dispensada de emissão de nota fiscal", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 636,"Prestação de serviço de transporte a contribuinte substituto em relação ao serviço de transporte Classificam-se  neste  código  as  prestações  de  serviços  de transporte  a  contribuinte  ao  qual  tenha  sido  atribuída  a condição   de   substituto   tributário   do   imposto   sobre   a prestação dos serviços.", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 64,"Saídas DE MERCADORIAS SUJEITAS AO REGIME DE SUBSTITUIÇÃO TRIBUTÁRIA", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6401,"Venda de produção do estabelecimento em operação com produto sujeito ao regime de substituição tributária, na condição de contribuinte substituto", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6402,"Venda de produção do estabelecimento de produto sujeito ao regime de substituição tributária, em operação entre contribuintes substitutos do mesmo produto", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6403,"Venda de mercadoria adquirida ou recebida de terceiros em operação com mercadoria sujeita ao regime de substituição tributária, na condição de contribuinte substituto", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6404,"Venda de mercadoria sujeita ao regime de substituição tributária, cujo imposto já tenha sido retido anteriormente", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6408,"Transferência de produção do estabelecimento em operação com produto sujeito ao regime de substituição tributária", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6409,"Transferência de mercadoria adquirida ou recebida de terceiros em operação com mercadoria sujeita ao regime de substituição tributária", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 641,"Devolução de compra para industrialização, produção rural, em operação com mercadoria sujeita ao regime de substituição tributária", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6411,"Devolução de compra para comercialização em operação com mercadoria sujeita ao regime de substituição tributária", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6412,"Devolução de bem do ativo imobilizado, em operação com mercadoria sujeita ao regime de substituição tributária", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6413,"Devolução de mercadoria destinada ao uso ou consumo, em operação com mercadoria sujeita ao regime de substituição tributária", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6414,"Remessa de produção do estabelecimento para venda fora do estabelecimento em operação com produto sujeito ao regime de substituição tributária", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6415,"Remessa de mercadoria adquirida ou recebida de terceiros para venda fora do estabelecimento, em operação com mercadoria sujeita ao regime de substituição tributária", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 65,"REMESSAS  PARA  FORMAÇÃO  DE  LOTE  E  COM  FIM ESPECÍFICO     DE     EXPORTAÇÃO     E     EVENTUAIS DEVOLUÇÕES", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6501,"Remessa de produção do estabelecimento, com fim específico de exportação", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6502,"Remessa de mercadoria adquirida ou recebida de terceiros, com fim específico de exportação", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6503,"Devolução de mercadoria recebida com fim específico de exportação", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6504,"Remessa de mercadorias para formação de lote de exportação, de produtos industrializados ou produzidos pelo próprio estabelecimento (efeitos a partir de 01.07.2006)", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6505,"Remessa de mercadorias, adquiridas ou recebidas de terceiros, para formação de lote de exportação (efeitos a partir de 01.07.2006)", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 655,"OPERAÇÕES COM BENS DE ATIVO IMOBILIZADO E MATERIAIS PARA USO OU CONSUMO", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6551,"Venda de bem do ativo imobilizado", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6552,"Transferência de bem do ativo imobilizado", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6553,"Devolução de compra de bem para o ativo imobilizado", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6554,"Remessa de bem do ativo imobilizado para uso fora do estabelecimento", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6555,"Devolução de bem do ativo imobilizado de terceiro, recebido para uso no estabelecimento", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6556,"Devolução de compra de material de uso ou consumo", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6557,"Transferência de material de uso ou consumo", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 66,"CRÉDITOS E RESSARCIMENTOS DE ICMS", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6603,"Ressarcimento de ICMS retido por substituição tributária", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 665,"Saídas DE COMBUSTÍVEIS, DERIVADOS OU NÃO DE PETRÓLEO E LUBRIFICANTES", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6651,"Venda de combustível ou lubrificante de produção do estabelecimento destinado à industrialização subsequente", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6652,"Venda de combustível ou lubrificante de produção do estabelecimento destinado à comercialização", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6653,"Venda de combustível ou lubrificante de produção do", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6654,"Venda de combustível ou lubrificante adquirido ou recebido de terceiros destinado à industrialização subsequente", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6655,"Venda de combustível ou lubrificante adquirido ou recebido de terceiros destinado à comercialização", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6656,"Venda de combustível ou lubrificante adquirido ou recebido de terceiros destinado a consumidor ou usuário final", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6657,"Remessa de combustível ou lubrificante adquirido ou recebido de terceiros para venda fora do estabelecimento", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6658,"Transferência de combustível ou lubrificante de produção do estabelecimento", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6659,"Transferência de combustível ou lubrificante adquirido ou recebido de terceiro", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 666,"Devolução de compra de combustível ou lubrificante adquirido para industrialização subsequente", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6661,"Devolução de compra de combustível ou lubrificante adquirido para comercialização", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6662,"Devolução de compra de combustível ou lubrificante adquirido por consumidor ou usuário final", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6663,"Remessa para armazenagem de combustível ou lubrificante", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6664,"Retorno de combustível ou lubrificante recebido para armazenagem", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6665,"Retorno simbólico de combustível ou lubrificante recebido para armazenagem", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6666,"Remessa por conta e ordem de terceiros de combustível ou lubrificante recebido para armazenagem", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 69,"OUTRAS SAÍDAS DE MERCADORIAS OU PRESTAÇÕES DE SERVIÇOS", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6901,"Remessa para industrialização por encomenda", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6902,"Retorno de mercadoria utilizada na industrialização por encomenda", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6903,"Retorno de mercadoria recebida para industrialização e não aplicada no referido processo", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6904,"Remessa para venda fora do estabelecimento", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6905,"Remessa para depósito fechado ou armazém-geral", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6906,"Retorno de mercadoria depositada em depósito fechado ou armazém-geral", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6907,"Retorno simbólico de mercadoria depositada em depósito fechado ou armazém-geral", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6908,"Remessa de bem por conta de contrato de comodato", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6909,"Retorno de bem recebido por conta de contrato de comodato", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 691,"Remessa em bonificação, doação ou brinde", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6911,"Remessa de amostra grátis", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6912,"Remessa de mercadoria ou bem para demonstração", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6913,"Retorno de mercadoria ou bem recebido para demonstração", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6914,"Remessa de mercadoria ou bem para exposição ou feira", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6915,"Remessa de mercadoria ou bem para conserto ou reparo", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6916,"Retorno de mercadoria ou bem recebido para conserto ou reparo", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6917,"Remessa de mercadoria em consignação mercantil ou industrial", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6918,"Devolução de mercadoria recebida em consignação mercantil ou industrial", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6919,"Devolução simbólica de mercadoria vendida ou utilizada em processo industrial, recebida anteriormente em consignação mercantil ou industrial", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 692,"Remessa de vasilhame ou sacaria", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6921,"Devolução de vasilhame ou sacaria", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6922,"Lançamento efetuado a título de simples faturamento decorrente de venda para entrega futura", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6923,"Remessa de mercadoria por conta e ordem de terceiros, em venda à ordem", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6923,"Remessa de mercadoria por conta e ordem de terceiros, em venda à ordem ou em operações com", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6924,"Remessa para industrialização por conta e ordem do adquirente da mercadoria, quando esta não transitar pelo estabelecimento do adquirente", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6925,"Retorno de mercadoria recebida para industrialização por conta e ordem do adquirente da mercadoria, quando aquela não transitar pelo estabelecimento do adquirente", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6929,"Lançamento efetuado em decorrência de emissão de documento fiscal relativo a operação ou prestação também registrada em equipamento Emissor de Cupom Fiscal – ECF", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6931,"Lançamento efetuado em decorrência da responsabilidade de retenção do imposto por substituição tributária, atribuída ao remetente ou alienante da mercadoria, pelo serviço de transporte realizado por transportador autônomo ou por transportador não inscrito na unidade da Federação onde iniciado o serviço", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6932,"Prestação de serviço de transporte iniciada em unidade da Federação diversa daquela onde inscrito o prestador", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6933,"Prestação de serviço tributado pelo ISSQN", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6934,"Remessa simbólica de mercadoria depositada em armazém geral ou depósito fechado.", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 6949,"Outra saída de mercadoria ou prestação de serviço não especificado", "Interestadual", "Saída"));
        listaCfops.add(new TabelaCfop(null, 71,"VENDA DE PRODUÇÃO PRÓPRIA OU DE TERCEIROS", "Exterior", "Saída"));
        listaCfops.add(new TabelaCfop(null, 7101,"Venda de produção do estabelecimento", "Exterior", "Saída"));
        listaCfops.add(new TabelaCfop(null, 7102,"Venda de mercadoria adquirida ou recebida de terceiros", "Exterior", "Saída"));
        listaCfops.add(new TabelaCfop(null, 7105,"Venda de produção do estabelecimento que não deva por ele transitar", "Exterior", "Saída"));
        listaCfops.add(new TabelaCfop(null, 7106,"Venda de mercadoria adquirida ou recebida de terceiros, que não deva por ele transitar", "Exterior", "Saída"));
        listaCfops.add(new TabelaCfop(null, 7127,"Venda de produção do estabelecimento sob o regime de drawback", "Exterior", "Saída"));
        listaCfops.add(new TabelaCfop(null, 72,"DEVOLUÇÕES DE COMPRAS PARA INDUSTRIALIZAÇÃO, PRODUÇÃO RURAL, COMERCIALIZAÇÃO OU ANULAÇÕES DE VALORES", "Exterior", "Saída"));
        listaCfops.add(new TabelaCfop(null, 7201,"Devolução de compra para industrialização ou produção rural", "Exterior", "Saída"));
        listaCfops.add(new TabelaCfop(null, 7202,"Devolução de compra para comercialização", "Exterior", "Saída"));
        listaCfops.add(new TabelaCfop(null, 7205,"Anulação de valor relativo a aquisição de serviço de comunicação", "Exterior", "Saída"));
        listaCfops.add(new TabelaCfop(null, 7206,"Anulação de valor relativo a aquisição de serviço de transporte", "Exterior", "Saída"));
        listaCfops.add(new TabelaCfop(null, 7207,"Anulação de valor relativo à compra de energia elétrica", "Exterior", "Saída"));
        listaCfops.add(new TabelaCfop(null, 721,"Devolução de compra para utilização na prestação de serviço - Classificam-se neste código as devoluções de mercadorias adquiridas para utilização na prestação de serviços, cujas entradas tenham sido classificadas nos códigos 1.126 - Compra para utilização na prestação de serviço sujeita ao ICMS e 1.128 – Compra para utilização na prestação de serviço sujeita ao ISSQN. ( Efeitos a partir de 1° de janeiro de 2011 )", "Exterior", "Saída"));
        listaCfops.add(new TabelaCfop(null, 7211,"Devolução de compras para industrialização sob o regime de drawback", "Exterior", "Saída"));
        listaCfops.add(new TabelaCfop(null, 725,"VENDAS DE ENERGIA ELÉTRICA", "Exterior", "Saída"));
        listaCfops.add(new TabelaCfop(null, 7251,"Venda de energia elétrica para o exterior", "Exterior", "Saída"));
        listaCfops.add(new TabelaCfop(null, 73,"PRESTAÇÕES DE SERVIÇOS DE COMUNICAÇÃO", "Exterior", "Saída"));
        listaCfops.add(new TabelaCfop(null, 7301,"Prestação de serviço de comunicação para execução de serviço da mesma natureza", "Exterior", "Saída"));
        listaCfops.add(new TabelaCfop(null, 735,"PRESTAÇÕES DE SERVIÇOS DE TRANSPORTE", "Exterior", "Saída"));
        listaCfops.add(new TabelaCfop(null, 7358,"Prestação de serviço de transporte", "Exterior", "Saída"));
        listaCfops.add(new TabelaCfop(null, 75,"EXPORTAÇÃO DE MERCADORIAS RECEBIDAS COM FIM ESPECÍFICO DE EXPORTAÇÃO", "Exterior", "Saída"));
        listaCfops.add(new TabelaCfop(null, 7501,"Exportação de mercadorias recebidas com fim específico de exportação", "Exterior", "Saída"));
        listaCfops.add(new TabelaCfop(null, 755,"OPERAÇÕES COM BENS DE ATIVO IMOBILIZADO E MATERIAIS PARA USO OU CONSUMO", "Exterior", "Saída"));
        listaCfops.add(new TabelaCfop(null, 7551,"Venda de bem do ativo imobilizado", "Exterior", "Saída"));
        listaCfops.add(new TabelaCfop(null, 7553,"Devolução de compra de bem para o ativo imobilizado", "Exterior", "Saída"));
        listaCfops.add(new TabelaCfop(null, 7556,"Devolução de compra de material de uso ou consumo", "Exterior", "Saída"));
        listaCfops.add(new TabelaCfop(null, 765,"Saídas DE COMBUSTÍVEIS, DERIVADOS OU NÃO DE PETRÓLEO E LUBRIFICANTES", "Exterior", "Saída"));
        listaCfops.add(new TabelaCfop(null, 7651,"Venda de combustível ou lubrificante de produção do estabelecimento", "Exterior", "Saída"));
        listaCfops.add(new TabelaCfop(null, 7654,"Venda de combustível ou lubrificante adquirido ou recebido de terceiros", "Exterior", "Saída"));
        listaCfops.add(new TabelaCfop(null, 79,"OUTRAS SAÍDAS DE MERCADORIAS OU PRESTAÇÕES DE SERVIÇOS", "Exterior", "Saída"));
        listaCfops.add(new TabelaCfop(null, 793,"Lançamento efetuado a título de devolução de bem cuja Entrada tenha ocorrido sob amparo de regime especial aduaneiro de admissão temporária", "Exterior", "Saída"));
        listaCfops.add(new TabelaCfop(null, 7949,"Outra saída de mercadoria ou prestação de serviço não especificado", "Exterior", "Saída"));

        return listaCfops;
    }

}