package br.com.codex.v1.apinfe.cte400.classes.nota;

import br.com.codex.v1.apinfe.DFBase;
import br.com.codex.v1.apinfe.cte400.classes.CTTipoPrazoHoraEntrega;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.time.LocalTime;

/**
 * Entrega com hora definida
 */

@Root(name = "comHora")
public class CTeNotaInfoDadosComplementaresEntregaComHoraDefinida extends DFBase {
    private static final long serialVersionUID = -7025725191699193271L;

    @Element(name = "tpHor")
    private CTTipoPrazoHoraEntrega tipoPrazoHoraEntrega;
    
    @Element(name = "hProg")
    private LocalTime horaProgramada;

    public CTTipoPrazoHoraEntrega getTipoPrazoHoraEntrega() {
        return this.tipoPrazoHoraEntrega;
    }

    /**
     * Tipo de hora<br>
     * Preencher com:<br>
     * 1--No horário;<br>
     * 2-Até o horário;<br>
     * 3-A partir do horário
     */
    public void setTipoPrazoHoraEntrega(final CTTipoPrazoHoraEntrega tipoPrazoHoraEntrega) {
        this.tipoPrazoHoraEntrega = tipoPrazoHoraEntrega;
    }

    public LocalTime getHoraProgramada() {
        return this.horaProgramada;
    }

    /**
     * Hora programada<br>
     * Formato HH:MM:SS
     */
    public void setHoraProgramada(final LocalTime horaProgramada) {
        this.horaProgramada = horaProgramada;
    }
}
