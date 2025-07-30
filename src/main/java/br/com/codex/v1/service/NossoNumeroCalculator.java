package br.com.codex.v1.service;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class NossoNumeroCalculator {
    private String bancoCodigo;
    private String convenio;
    private String carteira;
    private String agencia;
    private String contaCorrente;
    private String digitoConta;
    private String tipoNossoNumero;
    private Date dataVencimento;
    private String sequencial;

    public NossoNumeroCalculator() {
    }

    // Métudo principal que será chamado pelo serviço
    public String calcularNossoNumeroCompleto(String sequencial, Date dataVencimento, String bancoCodigo, String convenio, String carteira,
                                              String agencia, String contaCorrente, String digitoConta, String tipoNossoNumero) {
        this.bancoCodigo = bancoCodigo;
        this.convenio = convenio;
        this.carteira = carteira;
        this.agencia = agencia;
        this.contaCorrente = contaCorrente;
        this.digitoConta = digitoConta;
        this.tipoNossoNumero = tipoNossoNumero;
        this.dataVencimento = dataVencimento;
        this.sequencial = sequencial;

        return calcularNossoNumero(sequencial, dataVencimento);
    }

    public String calcularNossoNumero(String sequencial, Date dataVencimento) {
        this.sequencial = sequencial;
        this.dataVencimento = dataVencimento;

        switch (this.bancoCodigo) {
            case "001":
            case "655": // Banco do Brasil
                return calcularBB();
            case "004": // Banco do Nordeste
                return calcularBNB();
            case "104": // Caixa Econômica Federal
                return calcularCaixa();
            case "237":
            case "274": // Bradesco e Grafeno
                return calcularBradesco();
            case "341": // Itaú
                return calcularItau();
            case "422": // Safra
                return calcularSafra();
            case "033":
            case "008":
            case "353": // Santander
                return calcularSantander();
            case "260": // Nubank
                return calcularNubank();
            default:
                throw new IllegalArgumentException("Banco " + this.bancoCodigo + " não suportado");
        }
    }

    private String calcularBB() {
        switch (this.carteira) {
            case "11":
                return "00000000000"; // Valor fixo para carteira 11
            case "13":
                // Formato: CCCCCCNNN01D
                String baseConv13 = padLeft(this.convenio, 6, '0').substring(0, 6);
                String nnBase13 = padLeft(this.sequencial, 3, '0').substring(0, 3);
                String dv13 = calcularDVMod11BB(baseConv13 + nnBase13 + "01");
                return baseConv13 + nnBase13 + "01" + dv13;
            case "15":
                // Formato: CCCCCCNNNNND
                String baseConv15 = padLeft(this.convenio, 6, '0').substring(0, 6);
                String nnBase15 = padLeft(this.sequencial, 5, '0').substring(0, 5);
                String dv15 = calcularDVMod11BB(baseConv15 + nnBase15);
                return baseConv15 + nnBase15 + dv15;
            case "16":
                if (this.convenio.length() == 7) {
                    // Formato: CCCCCCCNNNNNNNNNN
                    String baseConv16 = padLeft(this.convenio, 7, '0').substring(0, 7);
                    String nnBase16 = padLeft(this.sequencial, 10, '0').substring(0, 10);
                    return baseConv16 + nnBase16;
                } else {
                    // Formato: NNNNNNNNNNNNNNNNN
                    return padLeft(this.sequencial, 17, '0').substring(0, 17);
                }
            case "17":
            case "18":
                if (this.convenio.length() == 4 && this.sequencial.length() <= 7) {
                    // Formato: CCCCNNNNNNND
                    String baseConv17 = padLeft(this.convenio, 4, '0').substring(0, 4);
                    String nnBase17 = padLeft(this.sequencial, 7, '0').substring(0, 7);
                    String dv17 = calcularDVMod11BB(baseConv17 + nnBase17);
                    return baseConv17 + nnBase17 + dv17;
                } else {
                    // Formato: CCCCCCNNNNND
                    String baseConv1718 = padLeft(this.convenio, 6, '0').substring(0, 6);
                    String nnBase1718 = padLeft(this.sequencial, 5, '0').substring(0, 5);
                    String dv1718 = calcularDVMod11BB(baseConv1718 + nnBase1718);
                    return baseConv1718 + nnBase1718 + dv1718;
                }
            case "31":
            case "51":
                return "00000000000"; // Valor fixo para carteiras 31 e 51
            default:
                throw new IllegalArgumentException("Carteira " + this.carteira + " do Banco do Brasil não suportada");
        }
    }

    private String calcularBNB() {
        // Banco do Nordeste - Formato: NNNNNNND
        String nnBase = padLeft(this.sequencial, 7, '0').substring(0, 7);
        String dv = calcularDVMod11(nnBase);
        return nnBase + dv;
    }

    private String calcularCaixa() {
        switch (this.carteira) {
            case "01":
                // Formato: 9NNNNNNNNNNNNNNNNND
                String nnBase01 = padLeft(this.sequencial, 17, '0').substring(0, 17);
                String dv01 = calcularDVMod11Caixa("9" + nnBase01);
                return "9" + nnBase01 + dv01;
            case "11":
                return "000000000000"; // Valor fixo para carteira 11
            case "12":
                // Formato: 9NNNNNNNNND
                String nnBase12 = padLeft(this.sequencial, 9, '0').substring(0, 9);
                String dv12 = calcularDVMod11Caixa("9" + nnBase12);
                return "9" + nnBase12 + dv12;
            case "14":
            case "24":
                // Formato: CCNNNNNNNNNNNNNNND
                String nnBase1424 = padLeft(this.sequencial, 15, '0').substring(0, 15);
                String dv1424 = calcularDVMod11Caixa(this.carteira + nnBase1424);
                return this.carteira + nnBase1424 + dv1424;
            case "82":
                // Formato: 82NNNNNNNND
                String nnBase82 = padLeft(this.sequencial, 8, '0').substring(0, 8);
                String dv82 = calcularDVMod11Caixa("82" + nnBase82);
                return "82" + nnBase82 + dv82;
            default:
                throw new IllegalArgumentException("Carteira " + this.carteira + " da Caixa Econômica não suportada");
        }
    }

    private String calcularBradesco() {
        // Bradesco - Formato: NNNNNNNNNNND
        String nnBase = padLeft(this.sequencial, 11, '0').substring(0, 11);
        // DV é calculado com carteira + nosso número
        String dv = calcularDVMod11Bradesco(padLeft(this.carteira, 2, '0') + nnBase);
        return nnBase + dv;
    }

    private String calcularItau() {
        // Itaú - várias carteiras com regras diferentes
        if ("198".equals(this.carteira)) {
            // Formato: NNNNNNNNNNND
            String nnBase = padLeft(this.sequencial, 11, '0').substring(0, 11);
            // DV calculado com agência + conta + carteira + nosso número
            String dvBase = padLeft(this.agencia, 4, '0') +
                    padLeft(this.contaCorrente, 5, '0') +
                    padLeft(this.carteira, 3, '0') +
                    nnBase;
            String dv = calcularDVMod10Itau(dvBase);
            return nnBase + dv;
        } else {
            // Formato padrão Itaú: NNNNNNNND
            String nnBase = padLeft(this.sequencial, 8, '0').substring(0, 8);
            // DV calculado com carteira + nosso número
            String dvBase = padLeft(this.carteira, 2, '0') + nnBase;
            String dv = calcularDVMod10Itau(dvBase);
            return nnBase + dv;
        }
    }

    private String calcularSafra() {
        // Safra - Formato: NNNNNNNND
        String nnBase = padLeft(this.sequencial, 8, '0').substring(0, 8);
        // DV é calculado com base no nosso número
        String dv = calcularDVMod11(nnBase);
        return nnBase + dv;
    }

    private String calcularSantander() {
        // Santander - Formato padrão: NNNNNNNNNNNND
        String nnBase = padLeft(this.sequencial, 12, '0').substring(0, 12);
        // DV é calculado com base no nosso número
        String dv = calcularDVMod10Santander(nnBase);
        return nnBase + dv;
    }

    private String calcularNubank() {
        // Nubank - Formato: NNNNNNNNNNN (sem DV)
        return padLeft(this.sequencial, 11, '0').substring(0, 11);
    }

    // Métodos auxiliares para cálculo de dígitos verificadores
    private String calcularDVMod11(String numero) {
        int soma = 0;
        int peso = 2;

        for (int i = numero.length() - 1; i >= 0; i--) {
            int digito = Character.getNumericValue(numero.charAt(i));
            soma += digito * peso;
            peso = (peso == 9) ? 2 : peso + 1;
        }

        int resto = soma % 11;
        int dv = 11 - resto;

        if (dv == 0 || dv == 10 || dv == 11) {
            return "1";
        } else {
            return String.valueOf(dv);
        }
    }

    private String calcularDVMod11BB(String numero) {
        // Implementação específica para Banco do Brasil
        int soma = 0;
        int peso = 2;

        for (int i = numero.length() - 1; i >= 0; i--) {
            int digito = Character.getNumericValue(numero.charAt(i));
            soma += digito * peso;
            peso = (peso == 9) ? 2 : peso + 1;
        }

        int resto = soma % 11;
        int dv = 11 - resto;

        if (dv == 10) {
            return "X";
        } else if (dv == 11) {
            return "0";
        } else {
            return String.valueOf(dv);
        }
    }

    private String calcularDVMod10Itau(String numero) {
        int soma = 0;
        boolean multiplica = true;

        for (int i = numero.length() - 1; i >= 0; i--) {
            int digito = Character.getNumericValue(numero.charAt(i));
            int resultado = multiplica ? digito * 2 : digito;

            if (resultado > 9) {
                resultado = (resultado / 10) + (resultado % 10);
            }

            soma += resultado;
            multiplica = !multiplica;
        }

        int dv = (10 - (soma % 10)) % 10;
        return String.valueOf(dv);
    }

    private String calcularDVMod10Santander(String numero) {
        // Similar ao Itaú, mas pode ter variações específicas
        return calcularDVMod10Itau(numero);
    }

    private String calcularDVMod11Bradesco(String numero) {
        // Implementação específica para Bradesco
        return calcularDVMod11(numero);
    }

    private String calcularDVMod11Caixa(String numero) {
        // Implementação específica para Caixa Econômica
        return calcularDVMod11(numero);
    }

    // Métudo auxiliar para preencher com zeros à esquerda
    private String padLeft(String input, int length, char padChar) {
        if (input.length() >= length) {
            return input;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - input.length()) {
            sb.append(padChar);
        }
        sb.append(input);
        return sb.toString();
    }

    // Métudo para formatar data (usado em alguns bancos)
    private String formatarData(Date data, String formato) {
        SimpleDateFormat sdf = new SimpleDateFormat(formato);
        return sdf.format(data);
    }
}
