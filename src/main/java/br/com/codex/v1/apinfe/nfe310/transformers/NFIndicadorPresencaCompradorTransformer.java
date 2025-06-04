package br.com.codex.v1.apinfe.nfe310.transformers;

import br.com.codex.v1.apinfe.nfe310.classes.nota.NFIndicadorPresencaComprador;
import org.simpleframework.xml.transform.Transform;

public class NFIndicadorPresencaCompradorTransformer implements Transform<NFIndicadorPresencaComprador> {

    @Override
    public NFIndicadorPresencaComprador read(final String codigo) {
        return NFIndicadorPresencaComprador.valueOfCodigo(codigo);
    }

    @Override
    public String write(final NFIndicadorPresencaComprador indicadorPresencaComprador) {
        return indicadorPresencaComprador.getCodigo();
    }
}