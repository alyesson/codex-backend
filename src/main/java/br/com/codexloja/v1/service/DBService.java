package br.com.codexloja.v1.service;

import br.com.codexloja.v1.domain.cadastros.Usuario;
import br.com.codexloja.v1.domain.enums.Perfil;
import br.com.codexloja.v1.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class DBService {

   @Autowired
    private UsuarioRepository usuarioRepository;

   @Autowired
   private BCryptPasswordEncoder encoder;

    public void instanciaDB(){
        Usuario pessoa = new Usuario(null, "Administrador", "80374841063", Date.valueOf("2024-01-07"), "Neutro", "19974061119", "Rua Indefinida 07", "Indefinido", "Hortolândia", "SP", "13185-421", "suporte@codexsolucoes.com.br", encoder.encode("Admin@2024!"), "Sistema", "00000");
        pessoa.addPerfil(Perfil.ADMINISTRADOR);
        usuarioRepository.saveAll(List.of(pessoa));
    }
}
