package br.com.codex.v1.domain.rh;

import br.com.codex.v1.domain.dto.AlteraSalarioLoteDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
public class AlteraSalarioLote implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String funcionario;
    private BigDecimal salarioAntigo;
    private BigDecimal salarioReajustado;
    private LocalDate dataAlteracao;
    private String motivo;
    private float reajuste;

    public AlteraSalarioLote() {
        super();
    }

    public AlteraSalarioLote(Long id, String funcionario, BigDecimal salarioAntigo, BigDecimal salarioReajustado,
                             LocalDate dataAlteracao, String motivo, float reajuste) {
        this.id = id;
        this.funcionario = funcionario;
        this.salarioAntigo = salarioAntigo;
        this.salarioReajustado = salarioReajustado;
        this.dataAlteracao = dataAlteracao;
        this.motivo = motivo;
        this.reajuste = reajuste;
    }

    public AlteraSalarioLote(AlteraSalarioLoteDto obj) {
        this.id = obj.getId();
        this.funcionario = obj.getFuncionario();
        this.salarioAntigo = obj.getSalarioAntigo();
        this.salarioReajustado = obj.getSalarioReajustado();
        this.dataAlteracao = obj.getDataAlteracao();
        this.motivo = obj.getMotivo();
        this.reajuste = obj.getReajuste();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AlteraSalarioLote that = (AlteraSalarioLote) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
