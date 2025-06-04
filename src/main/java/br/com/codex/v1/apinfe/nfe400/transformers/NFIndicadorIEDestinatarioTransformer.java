package br.com.codex.v1.apinfe.nfe400.transformers;

import br.com.codex.v1.apinfe.nfe400.classes.nota.NFIndicadorIEDestinatario;
import org.simpleframework.xml.transform.Transform;

public class NFIndicadorIEDestinatarioTransformer implements Transform<NFIndicadorIEDestinatario> {

    @Override
    public NFIndicadorIEDestinatario read(final String codigo) {
        return NFIndicadorIEDestinatario.valueOfCodigo(codigo);
    }

    @Override
    public String write(final NFIndicadorIEDestinatario destinatario) {
        return destinatario.getCodigo();
    }
}