package br.com.codex.v1.apinfe.nfe400.transformers;

import br.com.codex.v1.apinfe.nfe400.classes.nota.NFIndicadorEscalaRelevante;
import org.simpleframework.xml.transform.Transform;

public class NFIndicadorEscalaRelevanteTransformer implements Transform<NFIndicadorEscalaRelevante> {

    @Override
    public NFIndicadorEscalaRelevante read(final String codigo) {
        return NFIndicadorEscalaRelevante.valueOfCodigo(codigo);
    }

    @Override
    public String write(final NFIndicadorEscalaRelevante nfIndicadorEscalaRelevante) {
        return nfIndicadorEscalaRelevante.getCodigo();
    }
}