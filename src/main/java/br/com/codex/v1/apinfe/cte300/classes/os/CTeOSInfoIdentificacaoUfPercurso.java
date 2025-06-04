package br.com.codex.v1.apinfe.cte300.classes.os;

import br.com.codex.v1.apinfe.DFBase;
import br.com.codex.v1.apinfe.DFUnidadeFederativa;
import br.com.codex.v1.apinfe.cte.CTeConfig;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;


@Root(name = "infPercurso")
@Namespace(reference = CTeConfig.NAMESPACE)
public class CTeOSInfoIdentificacaoUfPercurso extends DFBase {
    private static final long serialVersionUID = -8756073376187638453L;

    @Element(name = "UFPer")
    private String ufPercurso;

    public CTeOSInfoIdentificacaoUfPercurso() {
    }

    public CTeOSInfoIdentificacaoUfPercurso(final DFUnidadeFederativa ufPercurso) {
        this.ufPercurso = ufPercurso.getCodigo();
    }

    public String getUfPercurso() {
        return this.ufPercurso;
    }

    public void setUfPercurso(final String ufPercurso) {
        this.ufPercurso = ufPercurso;
    }

    public void setUfPercurso(final DFUnidadeFederativa ufPercurso) {
        this.ufPercurso = ufPercurso.getCodigo();
    }
}
