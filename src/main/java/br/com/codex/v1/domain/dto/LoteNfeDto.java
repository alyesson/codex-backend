package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.fiscal.LoteNfe;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
public class LoteNfeDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String idLote;
    private String cnpjEmitente;
    private Integer ambiente;
    private LocalDateTime dataEnvio;
    private String status;
    private Integer quantidadeNotas;
    private String protocolo;
    private String ultimoNumero;
    private String tipoDocumento;
    @Column(columnDefinition = "TEXT")
    private String xmlResposta;

    public LoteNfeDto() {
        super();
    }

    public LoteNfeDto(LoteNfe obj) {
        this.id = obj.getId();
        this.idLote = obj.getIdLote();
        this.cnpjEmitente = obj.getCnpjEmitente();
        this.ambiente = obj.getAmbiente();
        this.dataEnvio = obj.getDataEnvio();
        this.status = obj.getStatus();
        this.quantidadeNotas = obj.getQuantidadeNotas();
        this.protocolo = obj.getProtocolo();
        this.ultimoNumero = obj.getUltimoNumero();
        this.tipoDocumento = obj.getTipoDocumento();
        this.xmlResposta = obj.getXmlResposta();
    }
}
