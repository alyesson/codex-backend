package br.com.codex.v1.utilitario;

import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CertificadoUtils {

    public static CertificadoInfo extrairInfoCertificado(byte[] arquivoCertificado, String senha) throws Exception {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(new ByteArrayInputStream(arquivoCertificado), senha.toCharArray());

        Enumeration<String> aliases = ks.aliases();
        String alias = aliases.nextElement();
        X509Certificate cert = (X509Certificate) ks.getCertificate(alias);

        String nome = cert.getSubjectX500Principal().getName();
        String razaoSocial = extrairRazaoSocial(nome);
        String cnpj = extrairCNPJ(nome);
        Date dataValidade = cert.getNotAfter();
        String uf = extrairUF(nome);

        return new CertificadoInfo(razaoSocial, cnpj, dataValidade, uf);
    }

    private static String extrairRazaoSocial(String nome) {
        // Implemente a lógica para extrair a razão social do nome do certificado
        // Exemplo: "CN=RAZAO SOCIAL LTDA, O=..."
        if (nome.contains("CN=")) {
            String cn = nome.split("CN=")[1].split(",")[0];
            return cn.trim();
        }
        return "Certificado Digital";
    }

    private static String extrairCNPJ(String nome) {
        // Implemente a lógica para extrair o CNPJ do nome do certificado
        // Exemplo: "CN=RAZAO SOCIAL LTDA:12345678901234, O=..."
        if (nome.contains(":")) {
            String[] parts = nome.split(":");
            if (parts.length > 1) {
                String cnpjPart = parts[1].split(",")[0];
                return cnpjPart.replaceAll("[^0-9]", "");
            }
        }
        return "";
    }

    private static String extrairUF(String nome) {
        // Padrão 1: Busca por "ST=SP" (sigla direta)
        Pattern pattern = Pattern.compile("ST=([A-Z]{2})");
        Matcher matcher = pattern.matcher(nome);
        if (matcher.find()) {
            return matcher.group(1); // Retorna a UF (ex: "SP")
        }

        // Padrão 2: Busca por "OU=RFB e-CNPJ A1 SP" (formato comum em certificados da Receita)
        pattern = Pattern.compile("OU=.*?(A1|A3)\\s([A-Z]{2})");
        matcher = pattern.matcher(nome);
        if (matcher.find()) {
            return matcher.group(2); // Retorna a UF (ex: "SP")
        }

        return "UF não identificada";
    }


    public static class CertificadoInfo {
        private String razaoSocial;
        private String cnpj;
        private Date dataValidade;
        private String uf;

        public CertificadoInfo(String razaoSocial, String cnpj, Date dataValidade, String uf) {
            this.razaoSocial = razaoSocial;
            this.cnpj = cnpj;
            this.dataValidade = dataValidade;
            this.uf = uf;
        }

        // Getters
        public String getRazaoSocial() { return razaoSocial; }
        public String getCnpj() { return cnpj; }
        public Date getDataValidade() { return dataValidade; }
        public String getUf() { return uf; }
    }
}
