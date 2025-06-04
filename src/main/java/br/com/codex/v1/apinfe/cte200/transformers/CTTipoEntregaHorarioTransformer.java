package br.com.codex.v1.apinfe.cte200.transformers;

import br.com.codex.v1.apinfe.cte200.classes.CTTipoEntregaHorario;
import org.simpleframework.xml.transform.Transform;

public class CTTipoEntregaHorarioTransformer implements Transform<CTTipoEntregaHorario> {

    @Override
    public CTTipoEntregaHorario read(final String codigo) {
        return CTTipoEntregaHorario.valueOfCodigo(codigo);
    }

    @Override
    public String write(final CTTipoEntregaHorario tipo) {
        return tipo.getCodigo();
    }
}