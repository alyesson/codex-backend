package br.com.codex.v1.apinfe.cte400.classes.nota;

import br.com.codex.v1.apinfe.DFBase;
import br.com.codex.v1.apinfe.validadores.DFStringValidador;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * @author Caio
 * @info
 */

@Root(name = "pass")
public class CTeNotaInfoDadosComplementaresFluxoPass extends DFBase {
    private static final long serialVersionUID = 4975128158804971520L;

    @Element(name = "xPass", required = false)
    private String pass;

    public String getPass() {
        return this.pass;
    }

    /**
     * Sigla ou código interno da Filial/Porto/Estação/Aeroporto de Passagem<br>
     * Observação para o modal aéreo: O código de três letras IATA, referente ao aeroporto de transferência, deverá ser incluído, quando for o caso. Quando não for possível, utilizar a sigla OACI. Qualquer solicitação de itinerário deverá ser incluída.
     */
    public void setPass(final String pass) {
        DFStringValidador.tamanho15(pass, "Sigla ou código interno da Filial/Porto/Estação/Aeroporto de Passagem");
        this.pass = pass;
    }
}
