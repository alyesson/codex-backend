package br.com.codex.v1.apinfe.mdfe3.transformers;

import br.com.codex.v1.apinfe.mdfe3.classes.def.MDFTipoInfPagComp;
import org.simpleframework.xml.transform.Transform;

/**
 * Created by Edivaldo Merlo Stens on 30/06/20.
 */
public class MDFTipoInfPagCompTransformer implements Transform<MDFTipoInfPagComp> {

    @Override
    public MDFTipoInfPagComp read(String value) {
        return MDFTipoInfPagComp.valueOfCodigo(value);
    }

    @Override
    public String write(MDFTipoInfPagComp value) {
        return value.getCodigo();
    }
}
