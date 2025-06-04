package br.com.codex.v1.apinfe.nfe400.transformers;

import br.com.codex.v1.apinfe.nfe400.classes.nota.NFIndicadorImportacao;
import org.simpleframework.xml.transform.Transform;

public class NFIndicadorImportacaoTransformer implements Transform<NFIndicadorImportacao> {

    @Override
    public NFIndicadorImportacao read(final String codigo) throws Exception {
        return NFIndicadorImportacao.valueOfCodigo(codigo);
    }

    @Override
    public String write(final NFIndicadorImportacao indicadorImportacao) throws Exception {
        return String.valueOf(indicadorImportacao.getCodigo());
    }

}
