package br.com.codex.v1.apinfe.cte400.classes.nota;

import br.com.codex.v1.apinfe.DFBase;
import br.com.codex.v1.apinfe.cte.CTeConfig;
import br.com.codex.v1.apinfe.cte400.classes.CTTipoUnidadeCarga;
import br.com.codex.v1.apinfe.validadores.DFBigDecimalValidador;
import br.com.codex.v1.apinfe.validadores.DFStringValidador;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import java.math.BigDecimal;
import java.util.List;

/**
 * Informações das Unidades de Carga (Containeres/ULD/Outros)<br>
 *       Dispositivo de carga utilizada (Unit Load Device - ULD) significa todo tipo de contêiner de carga, vagão, contêiner de avião, palete de aeronave com rede ou palete de aeronave com rede sobre um iglu.
 */

@Root(name = "infUnidCarga")
@Namespace(reference = CTeConfig.NAMESPACE)
public class CTeNotaInfoCTeNormalInfoDocumentosInfoUnidadeCarga extends DFBase {
    private static final long serialVersionUID = 4127763600684144275L;

    @Element(name = "tpUnidCarga")
    private CTTipoUnidadeCarga unidadeCarga;
    
    @Element(name = "idUnidCarga")
    private String identificacaoCarga;

    @ElementList(name = "lacUnidCarga", inline = true, required = false)
    private List<CTeNotaInfoCTeNormalInfoDocumentosLacre> lacre;

    @Element(name = "qtdRat", required = false)
    private String quantidadeRateada;

    public CTTipoUnidadeCarga getUnidadeCarga() {
        return this.unidadeCarga;
    }

    /**
     * Tipo da Unidade de Carga<br>
     * 1 - Container<br>
     * 2 - ULD<br>
     * 3 - Pallet<br>
     * 4 - Outros
     */
    public void setUnidadeCarga(final CTTipoUnidadeCarga unidadeCarga) {
        this.unidadeCarga = unidadeCarga;
    }

    public String getIdentificacaoCarga() {
        return this.identificacaoCarga;
    }

    /**
     * Identificação da Unidade de Carga<br>
     * Informar a identificação da unidade de carga, por exemplo: número do container.
     */
    public void setIdentificacaoCarga(final String identificacaoCarga) {
        DFStringValidador.tamanho20(identificacaoCarga, "Identificação da Unidade de Carga");
        this.identificacaoCarga = identificacaoCarga;
    }

    public List<CTeNotaInfoCTeNormalInfoDocumentosLacre> getLacre() {
        return this.lacre;
    }

    /**
     * Lacres das Unidades de Carga
     */
    public void setLacre(final List<CTeNotaInfoCTeNormalInfoDocumentosLacre> lacre) {
        this.lacre = lacre;
    }

    public String getQuantidadeRateada() {
        return this.quantidadeRateada;
    }

    /**
     * Quantidade rateada (Peso,Volume)
     */
    public void setQuantidadeRateada(final BigDecimal quantidadeRateada) {
        this.quantidadeRateada = DFBigDecimalValidador.tamanho5Com2CasasDecimais(quantidadeRateada, "Quantidade rateada (Peso,Volume)");
    }
}
