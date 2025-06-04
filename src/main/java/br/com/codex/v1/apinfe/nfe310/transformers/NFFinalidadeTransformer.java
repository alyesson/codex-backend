package br.com.codex.v1.apinfe.nfe310.transformers;

import br.com.codex.v1.apinfe.nfe310.classes.NFFinalidade;
import org.simpleframework.xml.transform.Transform;

public class NFFinalidadeTransformer implements Transform<NFFinalidade> {

    @Override
    public NFFinalidade read(final String codigo) {
        return NFFinalidade.valueOfCodigo(codigo);
    }

    @Override
    public String write(final NFFinalidade tipo) {
        return tipo.getCodigo();
    }
}