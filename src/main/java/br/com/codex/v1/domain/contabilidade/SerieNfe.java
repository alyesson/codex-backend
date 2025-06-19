package br.com.codex.v1.domain.contabilidade;

import br.com.codex.v1.domain.dto.SerieNfeDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
public class SerieNfe implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 3)
    private String numeroSerie;
    @Column(length = 50)
    private String nome;
    @Column(length = 15)
    private String tipoDocumento;
    @Column(length = 25)
    private Integer ultimoNumero;
    @Column(length = 20)
    private String cnpj;
    @Column(length = 20)
    private String ambiente;
    @Column(length = 10)
    private String status;
    @Column(length = 50)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    public SerieNfe() {
        super();
    }

    public SerieNfe(Long id, String numeroSerie,String nome, String tipoDocumento, Integer ultimoNumero, String cnpj, String ambiente, String status, LocalDateTime dataCriacao) {
        this.id = id;
        this.numeroSerie = numeroSerie;
        this.nome = nome;
        this.tipoDocumento = tipoDocumento;
        this.ultimoNumero = ultimoNumero;
        this.cnpj = cnpj;
        this.ambiente = ambiente;
        this.status = status;
        this.dataCriacao = dataCriacao;
    }

    public SerieNfe(SerieNfeDto obj) {
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SerieNfe serieNfe = (SerieNfe) o;
        return Objects.equals(id, serieNfe.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
