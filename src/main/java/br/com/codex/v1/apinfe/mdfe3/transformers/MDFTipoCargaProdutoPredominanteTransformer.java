package br.com.codex.v1.apinfe.mdfe3.transformers;

import br.com.codex.v1.apinfe.mdfe3.classes.def.MDFTipoCargaProdutoPredominante;
import org.simpleframework.xml.transform.Transform;

/**
 * Created by Edivaldo Merlo Stens on 30/06/20.
 */
public class MDFTipoCargaProdutoPredominanteTransformer implements Transform<MDFTipoCargaProdutoPredominante> {

    @Override
    public MDFTipoCargaProdutoPredominante read(String value) {
        return MDFTipoCargaProdutoPredominante.valueOfCodigo(value);
    }

    @Override
    public String write(MDFTipoCargaProdutoPredominante value) {
        return value.getCodigo();
    }
}
