package br.com.codex.v1.domain.cadastros;

import java.io.Serial;
import java.io.Serializable;

public class TabelaNcm implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Integer id;
    protected int codigo;
    protected String nome;
    protected String uf;
}
