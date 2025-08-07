package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.contabilidade.Contas;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

@Getter
@Setter
public class ContasDto {

    protected Long id;
    protected String conta;
    protected String nome;
    protected String reduzido;
    protected String utilidade;
    protected String saldo;
    protected String tipo;
    protected String natureza;
    protected Date inclusao;
    protected String situacao;
    protected String observacao;

    public ContasDto() {
        super();
    }

    public ContasDto(Contas obj) {
        this.id = obj.getId();
        this.conta = obj.getConta();
        this.nome = obj.getNome();
        this.reduzido = obj.getReduzido();
        this.utilidade = obj.getUtilidade();
        this.saldo = obj.getSaldo();
        this.tipo = obj.getTipo();
        this.natureza = obj.getNatureza();
        this.inclusao = obj.getInclusao();
        this.situacao = obj.getSituacao();
        this.observacao = obj.getObservacao();
    }

    public void setNome(String nome) {
        this.nome = capitalizarPalavras(nome);
    }
}
