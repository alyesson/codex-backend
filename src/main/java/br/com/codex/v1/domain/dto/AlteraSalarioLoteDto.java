package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.rh.AlteraSalarioLote;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
public class AlteraSalarioLoteDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String funcionario;
    private BigDecimal salarioAntigo;
    private BigDecimal salarioReajustado;
    private LocalDate dataAlteracao;
    @NotBlank(message = "Motivo do reajuste não pode ficar em branco")
    private String motivo;
    @NotNull(message = "Percentual do reajuste não pode ficar em branco")
    private BigDecimal reajuste;

    public AlteraSalarioLoteDto() {
        super();
    }

    public AlteraSalarioLoteDto(AlteraSalarioLote obj) {
        this.id = obj.getId();
        this.funcionario = obj.getFuncionario();
        this.salarioAntigo = obj.getSalarioAntigo();
        this.salarioReajustado = obj.getSalarioReajustado();
        this.dataAlteracao = obj.getDataAlteracao();
        this.motivo = obj.getMotivo();
        this.reajuste = obj.getReajuste();
    }
}
