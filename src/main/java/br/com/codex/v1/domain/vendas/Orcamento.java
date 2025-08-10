package br.com.codex.v1.domain.vendas;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Orcamento implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 6)
    private String codigo;

    @Column(nullable = false, length = 11)
    private LocalDate dataEmissao;

    @Column(nullable = false, length = 11)
    private LocalDate dataValidade;

    @Column(nullable = false, length = 30)
    private String vendedor;

    @Column(nullable = false, length = 6)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal descontoTotal = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal valorFinal = BigDecimal.ZERO;

    @Column(length = 500)
    private String observacoes;

}
