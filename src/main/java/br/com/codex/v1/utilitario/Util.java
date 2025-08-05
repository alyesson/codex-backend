package br.com.codex.v1.utilitario;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public final class Util {


    /**
     * Construtor privado para garantir o Singleton.
     */
    private Util() {
    }

    /**
     * Cria um arquivo com os dados passados
     *
     * @throws Exception
     */
    public static String criarArquivo(String pasta, String arquivo, String conteudo) throws IOException {
        return Files.write(Paths.get(pasta + "/" + arquivo), conteudo.getBytes()).toFile().getAbsolutePath();
    }

    /**
     * Remove tudo que não numeros de 0 a 9 da String passada.
     *
     * @param s
     * @return
     */
    public static String manterApenasNumeros(String s) {
        return  Util.verifica(s).isPresent() ? s.replaceAll("[^0-9]", ""): null;
    }

    /**
     * Retorna a data no formado do speed (ddmmaaaa)
     *
     * @param data
     * @return
     */
    public static String dataSpeed(LocalDateTime data) {
        return dataSpeed(data.toLocalDate());
    }

    /**
     * Retorna a data no formado do speed (ddmmaaaa)
     *
     * @param data
     * @return
     */
    public static String dataSpeed(LocalDate data) {
        String dia = Util.completarComZerosAEsquerda(String.valueOf(data.getDayOfMonth()), 2);
        String mes = Util.completarComZerosAEsquerda(String.valueOf(data.getMonthValue()), 2);
        String ano = String.valueOf(data.getYear());
        return dia + mes + ano;
    }

    /**
     * Converte o valor em {@link String} para {@link BigDecimal}
     *
     * @param valor
     * @return
     */
    public static BigDecimal valStringToBigDecimal(String valor) {
        return !Util.isEmpty(valor) ? new BigDecimal(valor.replace(",", ".")) : BigDecimal.ZERO;
    }

    /**
     * Retorna uma String do valor bigdecimal com 2 casas decimais
     *
     * @param valor
     * @return
     */
    public static String valorSpeed2Casas(BigDecimal valor) {
        return isValorZeroOuNulo(valor) ? "0" : bigDecimal2Casas(valor).replace(".", ",");
    }

    /**
     * Retorna uma String do valor bigdecimal com 3 casas decimais
     *
     * @param valor
     * @return
     */
    public static String valorSpeed3Casas(BigDecimal valor) {
        return isValorZeroOuNulo(valor) ? "0" : bigDecimal3Casas(valor).replace(".", ",");
    }

    /**
     * Retorna uma String do valor bigdecimal com 4 casas decimais
     *
     * @param valor
     * @return
     */
    public static String valorSpeed4Casas(BigDecimal valor) {
        return isValorZeroOuNulo(valor) ? "0" : bigDecimal4Casas(valor).replace(".", ",");
    }

    /**
     * Retorna true se os dois valores passados em BigDecimal forem iguais,
     * false se forem diferentes.
     *
     * @param valor1
     * @param valor2
     * @return
     */
    public static boolean isValoresIguais(BigDecimal valor1, BigDecimal valor2) {
        return valor1.compareTo(valor2) == 0;
    }

    /**
     * Retorna true se o valor for igual a zero
     *
     * @param valor
     * @return
     */
    public static boolean isValorZero(BigDecimal valor) {
        return isValoresIguais(valor, BigDecimal.ZERO);
    }

    /**
     * Retorna true se o valor for Zero ou Nulo
     *
     * @param valor
     * @return
     */
    private static boolean isValorZeroOuNulo(BigDecimal valor) {
        return Util.isEmpty(valor) || isValorZero(valor);
    }

    /**
     * Retorna true se o valor for maior que zero
     *
     * @param valor
     * @return
     */
    public static boolean maiorQZero(String valor) {
        if (Util.isEmpty(valor)) return false;
        return maiorQZero(valStringToBigDecimal(valor));
    }

    /**
     * Verifica se o valor passado em BigDecimal é maior que zero.
     *
     * @param valor
     * @return
     */
    public static boolean maiorQZero(BigDecimal valor) {
        return valor.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Retorna a data da nfe informado convertida para LocalDateTime
     *
     * @param dataNfe
     * @return
     */
    public static LocalDateTime dataNfeToLocalDateTime(String dataNfe) {
        return LocalDateTime.parse(dataNfe.substring(0, 19));
    }

    public static String removeCaracters(String valor) {
        return Util.verifica(valor).isPresent() ?
                valor.replaceAll("[^0-9]+", "") : null;
    }

    /**
     * Completa com zeros a esquerda ate o tamanho passado.
     *
     * @param value
     * @param length
     * @return
     */
    public static String completarComZerosAEsquerda(String value, int length) {
        int tam = value.length();
        String result = value;

        for (int i = tam; i < length; i++) {
            result = "0" + result;
        }
        return result;

    }

    /**
     * Verifica se um objeto &eacute; vazio.
     *
     * @param obj
     * @return <b>true</b> se o objeto for vazio(empty).
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null)
            return true;
        if (obj instanceof Collection)
            return ((Collection<?>) obj).size() == 0;

        final String s = String.valueOf(obj).trim();

        return s.length() == 0 || s.equalsIgnoreCase("null");
    }

    public static boolean isNull(final Object valor) {
        return valor == null;
    }

    /**
     * Retorna uma String do valor bigdecimal com 2 casas decimais
     *
     * @param valor
     * @return
     */
    public static String bigDecimal2Casas(BigDecimal valor) {
        return formatarCasasDecimais(valor, 2);
    }

    /**
     * Retorna uma String do valor bigdecimal com 3 casas decimais
     *
     * @param valor
     * @return
     */
    public static String bigDecimal3Casas(BigDecimal valor) {
        return formatarCasasDecimais(valor, 3);
    }

    /**
     * Verifica se um objeto &eacute; vazio.
     *
     * @param obj
     * @return <b>true</b> se o objeto for vazio(empty).
     */
    public static <T> Optional<T> verifica(T obj) {
        if (obj == null)
            return Optional.empty();
        if (obj instanceof Collection)
            return ((Collection<?>) obj).size() == 0 ? Optional.empty() : Optional.of(obj);

        final String s = String.valueOf(obj).trim();

        return s.length() == 0 || s.equalsIgnoreCase("null") ? Optional.empty() : Optional.of(obj);
    }

    /**
     * Retorna uma String do valor bigdecimal com 4 casas decimais
     *
     * @param valor
     * @return
     */
    public static String bigDecimal4Casas(BigDecimal valor) {
        return formatarCasasDecimais(valor, 4);
    }

    /**
     * Retorna uma String do valor BigDecimal formatando o numero de casas decimais
     * de acordo com o scale informado.
     *
     * @param valor
     * @param scale
     * @return
     */
    private static String formatarCasasDecimais(BigDecimal valor, int scale) {
        if (!Util.isEmpty(valor)) {
            return valor.setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
        } else {
            return BigDecimal.ZERO.setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
        }
    }
}