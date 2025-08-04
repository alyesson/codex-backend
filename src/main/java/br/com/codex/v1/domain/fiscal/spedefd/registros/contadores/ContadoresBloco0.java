
package br.com.codex.v1.domain.fiscal.spedefd.registros.contadores;

import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco0.Bloco0Enum;

/**
 * @author Alyesson Sousa
 *
 */
public class ContadoresBloco0 {

    //Bloco 0
    private int contRegistro0000 = 0;
    private int contRegistro0001 = 0;
    private int contRegistro0002 = 0;
    private int contRegistro0005 = 0;
    private int contRegistro0015 = 0;
    private int contRegistro0100 = 0;
    private int contRegistro0150 = 0;
    private int contRegistro0175 = 0;
    private int contRegistro0190 = 0;
    private int contRegistro0200 = 0;
    private int contRegistro0205 = 0;
    private int contRegistro0206 = 0;
    private int contRegistro0210 = 0;
    private int contRegistro0220 = 0;
    private int contRegistro0221 = 0;
    private int contRegistro0300 = 0;
    private int contRegistro0305 = 0;
    private int contRegistro0400 = 0;
    private int contRegistro0450 = 0;
    private int contRegistro0460 = 0;
    private int contRegistro0500 = 0;
    private int contRegistro0600 = 0;
    private int contRegistro0990 = 0;

    public void incrementar(Bloco0Enum bloco0) {

        contRegistro0990++;

        switch (bloco0) {
            case Registro0000 -> contRegistro0000++;
            case Registro0001 -> contRegistro0001++;
            case Registro0002 -> contRegistro0002++;
            case Registro0005 -> contRegistro0005++;
            case Registro0015 -> contRegistro0015++;
            case Registro0100 -> contRegistro0100++;
            case Registro0150 -> contRegistro0150++;
            case Registro0175 -> contRegistro0175++;
            case Registro0190 -> contRegistro0190++;
            case Registro0200 -> contRegistro0200++;
            case Registro0205 -> contRegistro0205++;
            case Registro0206 -> contRegistro0206++;
            case Registro0210 -> contRegistro0210++;
            case Registro0220 -> contRegistro0220++;
            case Registro0221 -> contRegistro0221++;
            case Registro0300 -> contRegistro0300++;
            case Registro0305 -> contRegistro0305++;
            case Registro0400 -> contRegistro0400++;
            case Registro0450 -> contRegistro0450++;
            case Registro0460 -> contRegistro0460++;
            case Registro0500 -> contRegistro0500++;
            case Registro0600 -> contRegistro0600++;
            default -> {
            }
        }
    }

    /**
     * @return the contRegistro0000
     */
    public int getContRegistro0000() {
        return contRegistro0000;
    }

    /**
     * @return the contRegistro0001
     */
    public int getContRegistro0001() {
        return contRegistro0001;
    }

    /**
     * @return the contRegistro0002
     */
    public int getContRegistro0002() {
        return contRegistro0002;
    }

    /**
     * @return the contRegistro0005
     */
    public int getContRegistro0005() {
        return contRegistro0005;
    }

    /**
     * @return the contRegistro0015
     */
    public int getContRegistro0015() {
        return contRegistro0015;
    }

    /**
     * @return the contRegistro0100
     */
    public int getContRegistro0100() {
        return contRegistro0100;
    }

    /**
     * @return the contRegistro0150
     */
    public int getContRegistro0150() {
        return contRegistro0150;
    }

    /**
     * @return the contRegistro0190
     */
    public int getContRegistro0190() {
        return contRegistro0190;
    }

    /**
     * @return the contRegistro0200
     */
    public int getContRegistro0200() {
        return contRegistro0200;
    }

    /**
     * @return the contRegistro0300
     */
    public int getContRegistro0300() {
        return contRegistro0300;
    }

    /**
     * @return the contRegistro0400
     */
    public int getContRegistro0400() {
        return contRegistro0400;
    }

    /**
     * @return the contRegistro0450
     */
    public int getContRegistro0450() {
        return contRegistro0450;
    }

    /**
     * @return the contRegistro0460
     */
    public int getContRegistro0460() {
        return contRegistro0460;
    }

    /**
     * @return the contRegistro0500
     */
    public int getContRegistro0500() {
        return contRegistro0500;
    }

    /**
     * @return the contRegistro0600
     */
    public int getContRegistro0600() {
        return contRegistro0600;
    }

    /**
     * @return the contRegistro0990
     */
    public int getContRegistro0990() {
        return contRegistro0990;
    }

    /**
     * @return the contRegistro0175
     */
    public int getContRegistro0175() {
        return contRegistro0175;
    }

    /**
     * @return the contRegistro0205
     */
    public int getContRegistro0205() {
        return contRegistro0205;
    }

    /**
     * @return the contRegistro0206
     */
    public int getContRegistro0206() {
        return contRegistro0206;
    }

    /**
     * @return the contRegistro0210
     */
    public int getContRegistro0210() {
        return contRegistro0210;
    }

    /**
     * @return the contRegistro0220
     */
    public int getContRegistro0220() {
        return contRegistro0220;
    }
    
    /**
     * @return the contRegistro0221
     */
    public int getContRegistro0221() {
        return contRegistro0221;
    }

    /**
     * @return the contRegistro0305
     */
    public int getContRegistro0305() {
        return contRegistro0305;
    }

}
