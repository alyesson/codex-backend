package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.estoque.Grupo;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

public class GrupoDto implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        protected Long id;
        protected String codigo;
        @NotNull(message = "O campo grupo é obrigatório")
        protected String descricao;

        public GrupoDto() {
            super();
        }

        public GrupoDto(Grupo obj) {
            this.id = obj.getId();
            this.codigo = obj.getCodigo();
            this.descricao = obj.getDescricao();
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getCodigo() {
            return codigo;
        }

        public void setCodigo(String codigo) {
            this.codigo = codigo;
        }

        public String getDescricao() {
            return descricao;
        }

        public void setDescricao(String descricao) {
            this.descricao = capitalizarPalavras(descricao);
        }
}
