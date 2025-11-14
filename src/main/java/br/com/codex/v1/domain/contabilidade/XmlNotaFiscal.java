package br.com.codex.v1.domain.contabilidade;

import br.com.codex.v1.domain.dto.XmlNotaFiscalDto;
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
public class XmlNotaFiscal implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false)
    protected String chaveAcesso;

    @Column(columnDefinition = "TEXT")
    protected String xmlContent;

    @Column(nullable = false)
    protected LocalDateTime dataCriacao;

    @Column(length = 60)
    protected String tipoDocumento;

    public XmlNotaFiscal() {
        super();
    }

    public XmlNotaFiscal(LocalDateTime dataCriacao, String xmlContent, String chaveAcesso, Long id, String tipoDocumento) {
        this.dataCriacao = dataCriacao;
        this.xmlContent = xmlContent;
        this.chaveAcesso = chaveAcesso;
        this.id = id;
        this.tipoDocumento = tipoDocumento;
    }

    public XmlNotaFiscal(XmlNotaFiscalDto obj) {
        this.dataCriacao = obj.getDataCriacao();
        this.xmlContent = obj.getXmlContent();
        this.chaveAcesso = obj.getChaveAcesso();
        this.id = obj.getId();
        this.tipoDocumento = obj.getTipoDocumento();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        XmlNotaFiscal that = (XmlNotaFiscal) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
