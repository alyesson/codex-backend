package br.com.codex.v1.apinfe.nfe400.transformers;

import org.simpleframework.xml.transform.Transform;

import br.com.codex.v1.apinfe.nfe400.classes.nota.NFIndicadorIntermediador;

public class NFIndicadorIntermediadorTransformer implements Transform<NFIndicadorIntermediador> {

    @Override
    public NFIndicadorIntermediador read(final String codigo) {
        return NFIndicadorIntermediador.valueOfCodigo(codigo);
    }

    @Override
    public String write(final NFIndicadorIntermediador indicadorPresencaComprador) {
        return indicadorPresencaComprador.getCodigo();
    }
}