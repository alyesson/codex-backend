package br.com.codex.v1.apinfe.transformers;

import br.com.codex.v1.apinfe.DFPais;
import org.simpleframework.xml.transform.Transform;

/**
 * Created by Eldevan Nery Junior on 07/05/18.
 *
 * Define os Classe de tranformação para a classe br.com.codex.v1.apinfe.DFPais.java em XML e vice-versa.
 *
 */
public class DFPaisTransformer implements Transform<DFPais> {

    @Override
    public DFPais read(final String codigo) {
        return DFPais.valueOfCodigo(codigo);
    }

    @Override
    public String write(final DFPais unidadeFederativa) {
        return unidadeFederativa.getCodigo().toString();
    }
}