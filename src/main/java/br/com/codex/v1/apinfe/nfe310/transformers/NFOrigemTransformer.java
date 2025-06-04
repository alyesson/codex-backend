package br.com.codex.v1.apinfe.nfe310.transformers;

import br.com.codex.v1.apinfe.nfe310.classes.NFOrigem;
import org.simpleframework.xml.transform.Transform;

public class NFOrigemTransformer implements Transform<NFOrigem> {

    @Override
    public NFOrigem read(final String codigo) {
        return NFOrigem.valueOfCodigo(codigo);
    }

    @Override
    public String write(final NFOrigem origem) {
        return origem.getCodigo();
    }
}