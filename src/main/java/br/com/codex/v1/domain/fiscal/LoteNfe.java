package br.com.codex.v1.domain.fiscal;

import br.com.codex.v1.domain.dto.LoteNfeDto;
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
@Table(name = "nfe_lotes")
public class LoteNfe implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String idLote;
    private String cnpjEmitente;
    private Integer ambiente;
    private LocalDateTime dataEnvio;
    private String status;
    private Integer quantidadeNotas;
    private String protocolo;
    private String ultimoNumero;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String xmlResposta;

    public LoteNfe() {
        super();
    }

    public LoteNfe(Long id, String idLote, String cnpjEmitente, Integer ambiente, LocalDateTime dataEnvio, String status, Integer quantidadeNotas, String protocolo, String ultimoNumero, String xmlResposta) {
        this.id = id;
        this.idLote = idLote != null ? idLote : "";
        this.cnpjEmitente = cnpjEmitente != null ? cnpjEmitente : "";
        this.ambiente = ambiente != null ? ambiente : 0;
        this.dataEnvio = dataEnvio != null ? dataEnvio : LocalDateTime.now();
        this.status = status != null ? status : "";
        this.quantidadeNotas = quantidadeNotas != null ? quantidadeNotas : 0;
        this.protocolo = protocolo != null ? protocolo : "";
        this.ultimoNumero = ultimoNumero != null ? ultimoNumero : "0";
        this.xmlResposta = xmlResposta != null ? xmlResposta : "";
    }

    public LoteNfe(LoteNfeDto obj) {
        this.id = obj.getId();
        this.idLote = obj.getIdLote();
        this.cnpjEmitente = obj.getCnpjEmitente();
        this.ambiente = obj.getAmbiente();
        this.dataEnvio = obj.getDataEnvio();
        this.status = obj.getStatus();
        this.quantidadeNotas = obj.getQuantidadeNotas();
        this.protocolo = obj.getProtocolo();
        this.ultimoNumero = obj.getUltimoNumero();
        this.xmlResposta = obj.getXmlResposta();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LoteNfe loteNfe = (LoteNfe) o;
        return Objects.equals(id, loteNfe.id) && Objects.equals(idLote, loteNfe.idLote);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idLote);
    }
}
