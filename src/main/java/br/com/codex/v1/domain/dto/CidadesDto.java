package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.cadastros.Cidades;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
public class CidadesDto implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        protected Integer id;
        @NotBlank(message = "Código Uf não pode estar em branco")
        protected Integer codigoUf;
        @NotBlank(message = "Uf não pode estar em branco")
        protected String uf;
        @NotBlank(message = "Nome do estado não pode estar em branco")
        protected String nomeUf;
        @NotBlank(message = "Código do município não pode estar em branco")
        protected String codigoMunicipio;
        @NotBlank(message = "Nome do município não pode estar em branco")
        protected String municipio;

    public CidadesDto() {
        super();
    }

    public CidadesDto(Cidades obj) {
        this.id = obj.getId();
        this.codigoUf = obj.getCodigoUf();
        this.uf = obj.getUf();
        this.nomeUf = obj.getNomeUf();
        this.codigoMunicipio = obj.getCodigoMunicipio();
        this.municipio = obj.getMunicipio();
    }

}
