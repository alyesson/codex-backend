package br.com.codex.v1.service;

import br.com.codex.v1.domain.cadastros.Usuario;
import br.com.codex.v1.domain.enums.Perfil;
import br.com.codex.v1.domain.repository.TiposCategoriasRepository;
import br.com.codex.v1.domain.repository.UsuarioRepository;
import br.com.codex.v1.domain.ti.TiposCategorias;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class ConfiguracaoPadraoService {

   @Autowired
    private UsuarioRepository usuarioRepository;

   @Autowired
   private BCryptPasswordEncoder encoder;

   @Autowired
   private TiposCategoriasRepository tiposCategoriasRepository;

    public void instanciaDB(){
        Usuario pessoa = new Usuario(null, "Administrador", "80374841063", Date.valueOf("2024-06-04"), "Neutro", "19971095445", "Av. Júlio Vasconcelos Bordon 10, Monte Mor, SP, 13190-307 ", "Indefinido", "Monte Mor", "SP", "13190-307", "ti@novadecamp.com.br", encoder.encode("Admin@2024!"), "TI", "00000");
        pessoa.addPerfil(Perfil.ADMINISTRADOR);
        usuarioRepository.saveAll(List.of(pessoa));
    }

    public void criaTabelaServicosTi() {
        List<TiposCategorias> tiposCategoriasList = List.of(
                new TiposCategorias(null, "Atualização de sistema", "Software"),
                new TiposCategorias(null, "Computador não liga", "Hardware"),
                new TiposCategorias(null, "Conexão de internet instável", "Software"),
                new TiposCategorias(null, "Erro ao acessar drive na rede", "Software"),
                new TiposCategorias(null, "Erro de acesso a servidores", "Software"),
                new TiposCategorias(null, "Erros após atualização do Windows", "Software"),
                new TiposCategorias(null, "Instalação de antivírus", "Software"),
                new TiposCategorias(null, "Instalação de impressora", "Impressora"),
                new TiposCategorias(null, "Instalação de programa", "Software"),
                new TiposCategorias(null, "Instalação de ramal", "Telefone"),
                new TiposCategorias(null, "Instalação ponto de rede", "Hardware"),
                new TiposCategorias(null, "Mensagem de erro sistema", "Software"),
                new TiposCategorias(null, "Monitor não liga", "Hardware"),
                new TiposCategorias(null, "Outros problemas", "Hardware"),
                new TiposCategorias(null, "Outros problemas", "Software"),
                new TiposCategorias(null, "Outros problemas", "Celular"),
                new TiposCategorias(null, "Outros problemas", "Telefone"),
                new TiposCategorias(null, "Papel atolado na impressora", "Impressora"),
                new TiposCategorias(null, "Papel manchado na impressão", "Impressora"),
                new TiposCategorias(null, "Problema no celular", "Celular"),
                new TiposCategorias(null, "Problema no ramal", "Telefone"),
                new TiposCategorias(null, "Problema headset", "Hardware"),
                new TiposCategorias(null, "Problema no mouse", "Hardware"),
                new TiposCategorias(null, "Problema no ramal", "Telefone"),
                new TiposCategorias(null, "Problema no teclado", "Hardware"),
                new TiposCategorias(null, "Problema com impressão", "Impressora"),
                new TiposCategorias(null, "Problema no Wi-fi", "Outros"),
                new TiposCategorias(null, "Problema na usb", "Hardware"),
                new TiposCategorias(null, "Sistema lento", "Software"),
                new TiposCategorias(null, "Superaquecimento do computador", "Hardware"),
                new TiposCategorias(null, "Solicitação cabo de rede", "Hardware"),
                new TiposCategorias(null, "Solicitação de acesso à pasta na rede", "Software"),
                new TiposCategorias(null, "Troca de celular", "Celular"),
                new TiposCategorias(null, "Troca de headset", "Hardware"),
                new TiposCategorias(null, "Troca de hardware", "Hardware"),
                new TiposCategorias(null, "Troca de teclado", "Hardware"),
                new TiposCategorias(null, "Troca de mouse", "Hardware"),
                new TiposCategorias(null, "Troca de toner", "Impressora"),
                new TiposCategorias(null, "Troca de nome no ramal", "Telefone"),
                new TiposCategorias(null, "Upgrade de hardware", "Hardware"),
                new TiposCategorias(null, "Vírus no computador", "Software"),
                new TiposCategorias(null, "Windows lento", "Software"),
                new TiposCategorias(null, "Outros problemas", "Outros"),
                new TiposCategorias(null, "Cadastro de biometria", "Software"),
                new TiposCategorias(null, "Link E1/E2 fora", "Outros")
        );
        tiposCategoriasRepository.saveAll(tiposCategoriasList);
    }
}
