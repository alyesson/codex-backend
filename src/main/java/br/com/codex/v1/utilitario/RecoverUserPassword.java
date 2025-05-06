package br.com.codex.v1.utilitario;

import br.com.codex.v1.domain.cadastros.Usuario;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
public class RecoverUserPassword {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(length = 36, updatable = false, nullable = false)
    private UUID id;
    private String token;
    private LocalDateTime expiration;
    private LocalDateTime created;
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public RecoverUserPassword() {
        super();
    }

    public RecoverUserPassword(UUID id, String token, LocalDateTime expiration, LocalDateTime created, Usuario usuario) {
        this.id = id;
        this.token = token;
        this.expiration = expiration;
        this.created = created;
        this.usuario = usuario;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecoverUserPassword that = (RecoverUserPassword) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
