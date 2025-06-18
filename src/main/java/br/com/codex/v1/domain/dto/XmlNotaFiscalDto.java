package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.contabilidade.XmlNotaFiscal;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class XmlNotaFiscalDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String chaveAcesso;
    private String xmlContent;
    private LocalDateTime dataCriacao;

    public XmlNotaFiscalDto() {
        super();
    }

    public XmlNotaFiscalDto(XmlNotaFiscal obj) {
        this.dataCriacao = obj.getDataCriacao();
        this.xmlContent = obj.getXmlContent();
        this.chaveAcesso = obj.getChaveAcesso();
        this.id = obj.getId();
    }
}
