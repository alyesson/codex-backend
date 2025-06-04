package br.com.codex.v1.apinfe.cte400.classes.nota;

import br.com.codex.v1.apinfe.DFBase;
import br.com.codex.v1.apinfe.cte.CTeConfig;
import br.com.codex.v1.apinfe.cte400.classes.CTIndicadoNegociavel;
import br.com.codex.v1.apinfe.validadores.DFStringValidador;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

/**
 * Informações do Multimodal
 */

@Root(name = "multimodal")
@Namespace(reference = CTeConfig.NAMESPACE)
public class CTeNotaInfoCTeNormalInfoModalMultiModal extends DFBase {
    private static final long serialVersionUID = 1803479809438053133L;

    @Element(name = "COTM")
    private String numeroCertificado;
    
    @Element(name = "indNegociavel")
    private CTIndicadoNegociavel indicadorNegociavel;

    @Element(name = "seg", required = false)
    private CTeNotaInfoCTeNormalInfoModalMultiModalSeguro seguro;

    public String getNumeroCertificado() {
        return this.numeroCertificado;
    }

    /**
     * Número do Certificado do Operador de Transporte Multimodal
     */
    public void setNumeroCertificado(final String numeroCertificado) {
        DFStringValidador.tamanho20(numeroCertificado, "Número do Certificado do Operador de Transporte Multimodal");
        this.numeroCertificado = numeroCertificado;
    }

    public CTIndicadoNegociavel getIndicadorNegociavel() {
        return this.indicadorNegociavel;
    }

    /**
     * Indicador Negociável Preencher com: 0 - Não Negociável; 1 - Negociável
     */
    public void setIndicadorNegociavel(final CTIndicadoNegociavel indicadorNegociavel) {
        this.indicadorNegociavel = indicadorNegociavel;
    }

    public CTeNotaInfoCTeNormalInfoModalMultiModalSeguro getSeguro() {
        return this.seguro;
    }

    /**
     * Informações de Seguro do Multimodal
     */
    public void setSeguro(final CTeNotaInfoCTeNormalInfoModalMultiModalSeguro seguro) {
        this.seguro = seguro;
    }
}
