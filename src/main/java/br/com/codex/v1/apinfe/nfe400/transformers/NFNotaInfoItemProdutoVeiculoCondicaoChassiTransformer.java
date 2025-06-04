package br.com.codex.v1.apinfe.nfe400.transformers;

import br.com.codex.v1.apinfe.nfe400.classes.NFNotaInfoItemProdutoVeiculoCondicaoChassi;
import org.simpleframework.xml.transform.Transform;

public class NFNotaInfoItemProdutoVeiculoCondicaoChassiTransformer implements Transform<NFNotaInfoItemProdutoVeiculoCondicaoChassi> {

    @Override
    public NFNotaInfoItemProdutoVeiculoCondicaoChassi read(final String codigo) {
        return NFNotaInfoItemProdutoVeiculoCondicaoChassi.valueOfCodigo(codigo);
    }

    @Override
    public String write(final NFNotaInfoItemProdutoVeiculoCondicaoChassi tipo) {
        return tipo.getCodigo();
    }
}