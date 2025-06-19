package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.contabilidade.SerieNfe;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class SerieNfeDto implements Serializable {
    @Serial
    protected static final long serialVersionUID = 1L;

    protected Long id;

    @NotBlank(message = "Número da série é obrigatório")
    @Size(max = 3, message = "Número da série deve ter até 3 caracteres")
    protected String numeroSerie;

    @NotBlank(message = "Nome da série não pode estar em branco")
    protected String nome;

    @NotBlank(message = "Tipo de documento é obrigatório")
    protected String tipoDocumento;

    @NotNull(message = "Último número é obrigatório")
    @Min(value = 0, message = "Último número não pode ser negativo")
    protected Integer ultimoNumero;

    @NotBlank(message = "CNPJ é obrigatório")
    @Size(min = 14, max = 14, message = "CNPJ deve ter 14 dígitos")
    protected String cnpj;

    @NotBlank(message = "Ambiente é obrigatório")
    protected String ambiente;

    @NotNull(message = "Status é obrigatório")
    protected String status;

    @NotNull(message = "Data de criação é obrigatória")
    protected LocalDateTime dataCriacao = LocalDateTime.now();

    public SerieNfeDto() {
        super();
    }

    public SerieNfeDto(SerieNfe obj) {
        this.id = obj.getId();
        this.numeroSerie = obj.getNumeroSerie();
        this.nome = obj.getNome();
        this.tipoDocumento = obj.getTipoDocumento();
        this.ultimoNumero = obj.getUltimoNumero();
        this.cnpj = obj.getCnpj();
        this.ambiente = obj.getAmbiente();
        this.status = obj.getStatus();
        this.dataCriacao = obj.getDataCriacao();
    }
}
