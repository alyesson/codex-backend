package br.com.codex.v1.apinfe.cte300.utils;

import br.com.codex.v1.apinfe.cte.CTTipoEmissao;
import br.com.codex.v1.apinfe.cte.CTeConfig;
import br.com.codex.v1.apinfe.cte300.classes.CTAutorizador31;
import br.com.codex.v1.apinfe.cte300.classes.nota.CTeNota;
import br.com.codex.v1.apinfe.cte300.classes.os.CTeOS;
import br.com.codex.v1.apinfe.utils.DFAssinaturaDigital;

public class CTeGeraQRCode {

    private final CTeConfig config;

    public CTeGeraQRCode(CTeConfig config) {
        this.config = config;
    }

    private String getQRCode(String chaveAcesso) throws Exception {
        String url = CTAutorizador31.valueOfChaveAcesso(chaveAcesso).getCteQrCode(this.config.getAmbiente());
        final StringBuilder parametros = new StringBuilder();
        parametros.append("chCTe=").append(chaveAcesso).append("&");
        parametros.append("tpAmb=").append(this.config.getAmbiente().getCodigo());
        if(this.config.getTipoEmissao().equals(CTTipoEmissao.CONTINGENCIA_EPEC)){
            parametros.append("&sign=").append(new DFAssinaturaDigital(this.config).assinarString(chaveAcesso));
        }
        // retorna a url do qrcode
        return url + "?" + parametros.toString();
    }

    public String getQRCode(CTeNota cteNota) throws Exception {
        return getQRCode(cteNota.getCteNotaInfo().getChaveAcesso());
    }

    public String getQRCode(CTeOS cteOS) throws Exception {
        return getQRCode(cteOS.getInfo().getChaveAcesso());
    }

}
