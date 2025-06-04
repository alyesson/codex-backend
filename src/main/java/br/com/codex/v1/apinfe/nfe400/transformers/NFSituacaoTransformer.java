package br.com.codex.v1.apinfe.nfe400.transformers;

import br.com.codex.v1.apinfe.nfe400.classes.NFSituacao;
import org.simpleframework.xml.transform.Transform;

public class NFSituacaoTransformer implements Transform<NFSituacao> {

    @Override
    public NFSituacao read(final String codigo) {
        return NFSituacao.valueOfCodigo(codigo);
    }

    @Override
    public String write(final NFSituacao situacao) {
        return situacao.getCodigo();
    }
}