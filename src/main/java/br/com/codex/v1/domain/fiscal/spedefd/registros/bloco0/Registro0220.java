
package br.com.codex.v1.domain.fiscal.spedefd.registros.bloco0;

/**
 * @author Alyesson Sousa
 *
 */
public class Registro0220 {

    private final String reg = "0220";
    private String unid_conv;
    private String fat_conv;
    private String cod_barra;
    
    /**
     * @return the reg
     */
    public String getReg() {
        return reg;
    }

    /**
     * @return the unid_conv
     */
    public String getUnid_conv() {
        return unid_conv;
    }

    /**
     * @param unid_conv the unid_conv to set
     */
    public void setUnid_conv(String unid_conv) {
        this.unid_conv = unid_conv;
    }

    /**
     * @return the fat_conv
     */
    public String getFat_conv() {
        return fat_conv;
    }

    /**
     * @param fat_conv the fat_conv to set
     */
    public void setFat_conv(String fat_conv) {
        this.fat_conv = fat_conv;
    }

    /**
     * @return the cod_barra
     */
    public String getCod_barra() {
        return cod_barra;
    }

    /**
     * @param cod_barra the cod_barra to set
     */
    public void setCod_barra(String cod_barra) {
        this.cod_barra = cod_barra;
    }    
}
