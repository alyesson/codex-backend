package br.com.codex.v1.apinfe.nfe310.transformers;

import br.com.codex.v1.apinfe.nfe310.classes.NFNotaInfoSituacaoTributariaIPI;
import org.simpleframework.xml.transform.Transform;

public class NFNotaInfoSituacaoTributariaIPITransformer implements Transform<NFNotaInfoSituacaoTributariaIPI> {

    @Override
    public NFNotaInfoSituacaoTributariaIPI read(final String codigo) {
        return NFNotaInfoSituacaoTributariaIPI.valueOfCodigo(codigo);
    }

    @Override
    public String write(final NFNotaInfoSituacaoTributariaIPI situacaoTributariaIPI) {
        return situacaoTributariaIPI.getCodigo();
    }
}