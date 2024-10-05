package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.financeiro.ContaBancaria;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

public class ContaBancariaDto implements Serializable {
        private static final long serialVersionUID = 1L;

        protected Integer id;
        @NotNull(message = "Nome da conta não pode ficar em branco")
        protected String nome;
        protected BigDecimal saldo;

    public ContaBancariaDto() {
            super();
        }

    public ContaBancariaDto(ContaBancaria obj) {
            this.id = obj.getId();
            this.nome = obj.getNome();
            this.saldo = obj.getSaldo();
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = capitalizarPalavras(nome);
        }

        public BigDecimal getSaldo() {
            return saldo;
        }

        public void setSaldo(BigDecimal saldo) {
            this.saldo = saldo;
        }
}
