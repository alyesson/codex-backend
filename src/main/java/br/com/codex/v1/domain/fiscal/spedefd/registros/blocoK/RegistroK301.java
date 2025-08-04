
package br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK;

/**
 * @author Alyesson Sousa
 */
public class RegistroK301 {

    private final String reg = "K301";
    private String cod_item;
    private String qtd;

    public String getCod_item() {
        return cod_item;
    }

    public void setCod_item(String cod_item) {
        this.cod_item = cod_item;
    }

    public String getQtd() {
        return qtd;
    }

    public void setQtd(String qtd) {
        this.qtd = qtd;
    }

    /**
     * @return the reg
     */
    public String getReg() {
        return reg;
    }

}
