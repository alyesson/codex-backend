package br.com.codex.v1.domain.cadastros;

import br.com.codex.v1.domain.dto.CidadesDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@Entity
public class Cidades implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        protected Long id;
        @Column(length = 7)
        protected Integer codigoUf;
        @Column(length = 2)
        protected String uf;
        @Column(length = 65)
        protected String nomeUf;
        @Column(length = 10)
        protected String codigoMunicipio;
        @Column(length = 60)
        protected String municipio;

    public Cidades() {
        super();
    }

    public Cidades(Long id, Integer codigoUf, String uf, String nomeUf, String codigoMunicipio, String municipio) {
        this.id = id;
        this.codigoUf = codigoUf;
        this.uf = uf;
        this.nomeUf = nomeUf;
        this.codigoMunicipio = codigoMunicipio;
        this.municipio = municipio;
    }

    public Cidades(CidadesDto obj) {
        this.id = obj.getId();
        this.codigoUf = obj.getCodigoUf();
        this.uf = obj.getUf();
        this.nomeUf = obj.getNomeUf();
        this.codigoMunicipio = obj.getCodigoMunicipio();
        this.municipio = obj.getMunicipio();
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cidades cidades = (Cidades) o;
        return Objects.equals(id, cidades.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
