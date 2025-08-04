package br.com.codex.v1.utilitario;


import br.com.codex.v1.domain.fiscal.spedefd.registros.EfdIcms;
import br.com.codex.v1.service.exceptions.EfdException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
public final class Util {

    private final static LocalDate dataVersao2018 = LocalDate.of(2017, 12, 31);
    private final static LocalDate dataVersao2019 = LocalDate.of(2018, 12, 31);
    private final static LocalDate dataVersao2020 = LocalDate.of(2019, 12, 31);
    private final static LocalDate dataVersao2021 = LocalDate.of(2020, 12, 31);
    private final static LocalDate dataVersao2022 = LocalDate.of(2021, 12, 31);
    private final static LocalDate dataVersao2023 = LocalDate.of(2022, 12, 31);
    private final static LocalDate dataVersao2024 = LocalDate.of(2023, 12, 31);
    private final static LocalDate dataVersao2025 = LocalDate.of(2024, 12, 31);

    /**
     * Construtor privado para garantir o Singleton.
     */
    private Util() {

    }

    /**
     * Verifica se um objeto está vazio.
     *
     * @param obj
     * @return <b>true</b> se o objeto for vazio(empty).
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null)
            return true;
        if (obj instanceof Collection)
            //return ((Collection<?>) obj).size() == 0;
            return ((Collection<?>) obj).isEmpty();

        final String s = String.valueOf(obj).trim();

        return s.length() == 0 || s.equalsIgnoreCase("null");
    }

    /**
     * Preenche o Registro
     *
     * @param string
     * @return 
     */
    public static String preencheRegistro(String string) {
        return Util.isEmpty(string) ? "" : string;
    }

    public static boolean versao2018(String dataStr) {
        return strToDate(dataStr).isAfter(dataVersao2018);
    }

    public static boolean versao2019(String dataStr) {
        return strToDate(dataStr).isAfter(dataVersao2019);
    }

    public static boolean versao2020(String dataStr) {
        return strToDate(dataStr).isAfter(dataVersao2020);
    }

    private static LocalDate strToDate(String dataStr) {
        return LocalDate.of(Integer.parseInt(dataStr.substring(4, 8)), Integer.parseInt(dataStr.substring(2, 4)), Integer.parseInt(dataStr.substring(0, 2)));
    }

    /**
     * Cria um arquivo com os dados passados
     *
     * @param pasta
     * @param arquivo
     * @param conteudo
     */
    public static void criarPastaArquivo(String pasta, String arquivo, String conteudo) throws EfdException {

        File folder = new File(pasta);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(new File(pasta + "/" + arquivo));
            fileWriter.write(conteudo);
            fileWriter.close();
        } catch (IOException e) {
            throw new EfdException("Erro ao Criar Arquivo: " + e.getMessage());
        }
    }

    public static String getCodVersao(EfdIcms efdIcms) {
        if (versao2025(efdIcms.getBloco0().getRegistro0000().getDt_ini())) {
            return "014";
        } else if (versao2024(efdIcms.getBloco0().getRegistro0000().getDt_ini())) {
            return "013";
        } else if (versao2023(efdIcms.getBloco0().getRegistro0000().getDt_ini())) {
            return "012";
        } else {
            return "ERRO_COD_VERSAO";
        }
    }
}