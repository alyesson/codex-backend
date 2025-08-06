package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.compras.AvaliacaoFornecedoresDetalhes;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

@Setter
@Getter
public class AvaliacaoFornecedoresDetalhesDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Long id;
    protected String criterio;
    protected float peso;
    protected float nota;
    protected AvaliacaoFornecedoresDto avaliacaoFornecedores;

    public AvaliacaoFornecedoresDetalhesDto() {
        super();
    }

    public AvaliacaoFornecedoresDetalhesDto(AvaliacaoFornecedoresDetalhes obj) {
        this.id = obj.getId();
        this.criterio = obj.getCriterio();
        this.peso = obj.getPeso();
        this.nota = obj.getNota();
        this.avaliacaoFornecedores = new AvaliacaoFornecedoresDto(obj.getAvaliacaoFornecedores());
    }

    public void setCriterio(String criterio) {
        this.criterio = capitalizarPalavras(criterio);
    }
}
