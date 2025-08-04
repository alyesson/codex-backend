package br.com.codex.v1.domain.fiscal.spedefd.registros.bloco0;

/**
 *
 * @author alyes
 */
public class Registro0221 {

    private final String reg = "0221";
    private String cod_item_atomico;
    private String qtd_contida;
    
    /**
     * @return the reg
     */
    public String getReg() {
        return reg;
    }

    /**
     * @return the cod_item_atomico
     */
    public String getCod_item_atomico() {
        return cod_item_atomico;
    }

    /**
     * @param cod_item_atomico the cod_item_atomico to set
     */
    public void setCod_item_atomico(String cod_item_atomico) {
        this.cod_item_atomico = cod_item_atomico;
    }

    /**
     * @return the qtd_contida
     */
    public String getQtd_contida() {
        return qtd_contida;
    }

    /**
     * @param qtd_contida the qtd_contida to set
     */
    public void setQtd_contida(String qtd_contida) {
        this.qtd_contida = qtd_contida;
    }
}
