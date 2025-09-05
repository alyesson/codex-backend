package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.cadastros.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCpf(String cpf);

    @Modifying
    @Transactional
    @Query("UPDATE Pessoa u set u.senha=:senha WHERE u.email=:email")
    int updatePassword(@Param("email") String email, @Param("senha") String senha);

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByNome(String nome);

    List<Usuario> findByPerfis(Integer perfil);

    @Query("SELECT COUNT(c) FROM Usuario c WHERE 3 MEMBER OF c.perfis")
    int countByPerfis(@Param("perfis") String perfis);

    @Query("SELECT d FROM Usuario d WHERE d.departamento =:departamento")
    List<Usuario> findByDepartamento(@Param("departamento") String departamento);
}
