package br.com.codex.v1.apinfe.cte400.transformers;

import br.com.codex.v1.apinfe.cte400.classes.CTTipoPrazoHoraEntrega;
import org.simpleframework.xml.transform.Transform;

public class CTTipoPrazoHoraEntregaTransformer implements Transform<CTTipoPrazoHoraEntrega> {

    @Override
    public CTTipoPrazoHoraEntrega read(final String arg0) {
        return CTTipoPrazoHoraEntrega.valueOfCodigo(arg0);
    }

    @Override
    public String write(final CTTipoPrazoHoraEntrega arg0) {
        return arg0.getCodigo();
    }

}
