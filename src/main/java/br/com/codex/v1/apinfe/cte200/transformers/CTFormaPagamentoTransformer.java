package br.com.codex.v1.apinfe.cte200.transformers;

import br.com.codex.v1.apinfe.cte200.classes.CTFormaPagamento;
import org.simpleframework.xml.transform.Transform;

public class CTFormaPagamentoTransformer implements Transform<CTFormaPagamento> {

    @Override
    public CTFormaPagamento read(final String codigo) {
        return CTFormaPagamento.valueOfCodigo(codigo);
    }

    @Override
    public String write(final CTFormaPagamento tipo) {
        return tipo.getCodigo();
    }
}