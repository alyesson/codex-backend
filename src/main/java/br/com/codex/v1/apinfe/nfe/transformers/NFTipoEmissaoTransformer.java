package br.com.codex.v1.apinfe.nfe.transformers;

import br.com.codex.v1.apinfe.nfe.NFTipoEmissao;
import org.simpleframework.xml.transform.Transform;

public class NFTipoEmissaoTransformer implements Transform<NFTipoEmissao> {

    @Override
    public NFTipoEmissao read(final String codigo) {
        return NFTipoEmissao.valueOfCodigo(codigo);
    }

    @Override
    public String write(final NFTipoEmissao tipo) {
        return tipo.getCodigo();
    }
}