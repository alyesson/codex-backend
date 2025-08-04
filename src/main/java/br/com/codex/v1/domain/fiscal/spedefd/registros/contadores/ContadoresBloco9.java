package br.com.codex.v1.domain.fiscal.spedefd.registros.contadores;

import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco9.Bloco9Enum;

/**
 *
 * @author alyes
 */
public class ContadoresBloco9 {

    private int contRegistro9001 = 0;
    private int contRegistro9900 = 0;
    private int contRegistro9990 = 0;
    private int contRegistro9999 = 0;

    public void incrementar(Bloco9Enum bloco) {

        switch (bloco) {
            case Registro9001:
                contRegistro9001++;
                break;
            case Registro9900:
                contRegistro9900++;
                break;
            case Registro9990:
                contRegistro9990++;
                break;
            case Registro9999:
                contRegistro9999++;
                break;
            default:
                break;
        }

    }

    /**
     * @return the contRegistro9001
     */
    public int getContRegistro9001() {
        return contRegistro9001;
    }

    /**
     * @return the contRegistro9900
     */
    public int getContRegistro9900() {
        return contRegistro9900;
    }

    /**
     * @return the contRegistro9990
     */
    public int getContRegistro9990() {
        return contRegistro9990;
    }

    /**
     * @return the contRegistro9999
     */
    public int getContRegistro9999() {
        return contRegistro9999;
    }

}
