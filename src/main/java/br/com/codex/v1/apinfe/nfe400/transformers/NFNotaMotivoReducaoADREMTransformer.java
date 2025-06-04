package br.com.codex.v1.apinfe.nfe400.transformers;

import br.com.codex.v1.apinfe.nfe400.classes.NFNotaMotivoReducaoADREM;
import org.simpleframework.xml.transform.Transform;

public class NFNotaMotivoReducaoADREMTransformer implements Transform<NFNotaMotivoReducaoADREM> {

    @Override
    public NFNotaMotivoReducaoADREM read(final String codigo) {
        return NFNotaMotivoReducaoADREM.valueOfCodigo(codigo);
    }

    @Override
    public String write(final NFNotaMotivoReducaoADREM motivoReducaoADREM) {
        return motivoReducaoADREM.getCodigo();
    }
}