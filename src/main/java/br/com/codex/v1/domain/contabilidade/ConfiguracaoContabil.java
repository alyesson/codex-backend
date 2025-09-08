package br.com.codex.v1.domain.contabilidade;

import br.com.codex.v1.domain.cadastros.Empresa;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
public class ConfiguracaoContabil implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    private String tipo; // 'RECEITA', 'CUSTO', 'DESPESA'
    private String criterio; // 'PREFIXO_CODIGO', 'TIPO', 'NATUREZA', 'UTILIDADE'
    private String valor; // Ex: "4.", "Receitas", "Operacional", etc.

    private String nomeGrupo; // Nome amigável para exibição

    public ConfiguracaoContabil() {
    }

    public ConfiguracaoContabil(Long id, Empresa empresa, String tipo, String criterio, String valor, String nomeGrupo) {
        this.id = id;
        this.empresa = empresa;
        this.tipo = tipo;
        this.criterio = criterio;
        this.valor = valor;
        this.nomeGrupo = nomeGrupo;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ConfiguracaoContabil that = (ConfiguracaoContabil) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
