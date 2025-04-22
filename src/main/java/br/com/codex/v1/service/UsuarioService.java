package br.com.decamptech.v1.service;

import br.com.decamptech.v1.domain.cadastros.Usuario;
import br.com.decamptech.v1.domain.dto.UsuarioDto;
import br.com.decamptech.v1.domain.repository.UsuarioRepository;
import br.com.decamptech.v1.service.exceptions.ObjectNotFoundException;
import br.com.decamptech.v1.utilitario.Base64Util;
import br.com.decamptech.v1.utilitario.RecoverUserPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;
    Base64Util encode = new Base64Util();

    @Autowired
   private EmailRecuperaSenhaService emailRecuperaSenhaService;

    String senhaCriptografada;

    public Usuario create(UsuarioDto usuariodto) {
        usuariodto.setId(null);
        validaEmail(usuariodto);
        senhaCriptografada = encoder.encode(usuariodto.getSenha());
        usuariodto.setSenha(senhaCriptografada);
        Usuario usuario = new Usuario(usuariodto);
        return usuarioRepository.save(usuario);
    }

    public Usuario update(Integer id, UsuarioDto usuariodto) {
        usuariodto.setId(id);
        Usuario objUsuario = findById(id); // Recupera o usuário atual do banco de dados

        // Ignora a senha enviada do front-end e mantém a senha existente no banco de dados
        usuariodto.setSenha(objUsuario.getSenha());

        // Atualiza o objeto usuário com os novos dados, exceto a senha
        objUsuario = new Usuario(usuariodto);
        return usuarioRepository.save(objUsuario);
    }

    public void delete(Integer id) {
        usuarioRepository.deleteById(id);
    }

    public Usuario findById(Integer id){
        Optional<Usuario> objId = usuarioRepository.findById(id);
        return objId.orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado"));
    }

    public Usuario findByCpf(String cpf){
        Optional<Usuario> objId = usuarioRepository.findByCpf(cpf);
        return objId.orElseThrow(() -> new ObjectNotFoundException("Este cpf não existe no cadastro, informe outro"));
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    private void validaEmail(UsuarioDto usuariodto){
        Optional<Usuario> objUsuario = usuarioRepository.findByCpf(usuariodto.getCpf());

        if(objUsuario.isPresent() && objUsuario.get().getEmail().equals(usuariodto.getEmail())){
            throw new DataIntegrityViolationException("Este e-mail já foi cadastrado");
        }
    }

    public void changePassword(String cpf, String senha) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String novaSenha = encoder.encode(senha);
        Optional<Usuario> usuario = usuarioRepository.findByCpf(cpf);

        if (!usuario.isPresent()) {
            throw new ObjectNotFoundException("Cpf: " + cpf + " não encontrado");
        }
        usuarioRepository.updatePassword(cpf, novaSenha);
    }

    public void recoverPassword(String email, String baseUrl) {
        Optional<Usuario> user = usuarioRepository.findByEmail(email);
        if (!user.isPresent()) {
            throw new ObjectNotFoundException("Este email não foi cadastrado");
        }

        try {
            RecoverUserPassword recoverUserPassword = new RecoverUserPassword();
            recoverUserPassword.setToken(UUID.randomUUID().toString());
            recoverUserPassword.setUsuario(user.get());
            LocalDateTime localDateTime = LocalDateTime.now();
            recoverUserPassword.setExpiration(LocalDate.from(localDateTime.plusHours(1)));
            recoverUserPassword.setCreated(localDateTime);

            String link = baseUrl + "?" + Base64Util.encode("key") + "=" + Base64Util.encode(recoverUserPassword.getToken()) +
                    "&" + Base64Util.encode("usuario") + "=" + Base64Util.encode(user.get().getEmail());
            emailRecuperaSenhaService.recoverPassword(link, user.get());
        } catch (Exception erro) {
            throw new ObjectNotFoundException("Erro ao enviar e-mail para " + user.get().getEmail() + ": " + erro.getMessage());
        }
    }

    public Usuario findByEmail(String email) {
        Optional<Usuario> objId = usuarioRepository.findByEmail(email);
        return objId.orElseThrow(() -> new ObjectNotFoundException("Este e-mail não existe no cadastro, informe outro"));
    }

    public List<Usuario> findByPerfis(Integer perfil) {
        List<Usuario> usuarios = usuarioRepository.findByPerfis(perfil);
        if (usuarios.isEmpty()) {
            throw new ObjectNotFoundException("Erro ao procurar profissionais");
        }
        return usuarios;
    }

    public int countByPerfis(String perfis) {
        int quantidade = usuarioRepository.countByPerfis(perfis);
        return quantidade;
    }
}
