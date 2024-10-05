package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.cadastros.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByCpf(String cpf);

    @Modifying
    @Transactional
    @Query("UPDATE Pessoa u set u.senha=:senha WHERE u.cpf=:cpf")
    int updatePassword(@Param("cpf") String cpf, @Param("senha") String senha);

    Optional<Usuario> findByEmail(String email);

    List<Usuario> findByPerfis(Integer perfil);

    @Query("SELECT COUNT(c) FROM Usuario c WHERE 3 MEMBER OF c.perfis")
    int countByPerfis(@Param("perfis") String perfis);
}
