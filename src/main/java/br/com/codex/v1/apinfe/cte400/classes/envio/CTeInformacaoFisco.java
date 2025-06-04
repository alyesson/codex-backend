package br.com.codex.v1.apinfe.cte400.classes.envio;

import br.com.codex.v1.apinfe.DFBase;
import br.com.codex.v1.apinfe.cte.CTeConfig;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "infFisco")
@Namespace(reference = CTeConfig.NAMESPACE)
public class CTeInformacaoFisco extends DFBase {
    private static final long serialVersionUID = 5156819883520032103L;

    @Element(name = "cMsg")
    private String codigoMensagem;

    @Element(name = "xMsg")
    private String mensagem;

    public String getCodigoMensagem() {
        return codigoMensagem;
    }

    public void setCodigoMensagem(String codigoMensagem) {
        this.codigoMensagem = codigoMensagem;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
