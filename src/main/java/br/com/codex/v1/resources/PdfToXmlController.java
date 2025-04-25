package br.com.codex.v1.resources;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("v1/api/convert_pdf")
public class PdfToXmlController {

    @PostMapping
    public String convertPdfToXml(@RequestParam("pdfFile") MultipartFile pdfFile) throws Exception {
        // 1. Salvar o arquivo PDF temporariamente
        File file = File.createTempFile("uploaded-", ".pdf");
        pdfFile.transferTo(file);

        // 2. Converter PDF para texto usando PDFBox
        String text = extractTextFromPdf(file);

        // 3. Converter texto para XML estruturado
        String xmlContent = convertTextToStructuredXml(text);

        return xmlContent;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveXml(@RequestBody SaveXmlRequest request) {
        try {
            // Aqui você pode salvar o XML no sistema de arquivos se necessário
            return ResponseEntity.ok("Arquivo salvo com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao salvar arquivo: " + e.getMessage());
        }
    }

    private String extractTextFromPdf(File pdfFile) throws IOException {
        PDDocument document = PDDocument.load(pdfFile);
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(document);
        document.close();
        return text;
    }

    private String convertTextToStructuredXml(String text) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        // Elemento raiz
        Element rootElement = document.createElement("orcamento");
        document.appendChild(rootElement);

        // Extrair informações do cabeçalho
        String numeroOrcamento = extrairNumeroOrcamento(text);
        String data = extrairData(text);
        String nome = extrairNome(text);
        String cliente = extrairCliente(text);
        String endereco = extrairEndereco(text);
        String condicaoPagamento = extrairCondicaoPagamento(text);
        String vendedor = extrairVendedor(text);
        String beneficiador = extrairBeneficiador(text);
        String transportadora = extrairTransportadora(text);
        String tipoVenda = extrairTipoVenda(text);
        String desconto = extraiDesconto(text);
        String seguro = extraiSeguro(text);
        String frete = extraiFrete(text);
        String outras = extraiOutras(text);
        String seuPedido = extraiSeuPedido(text);
        String icms = extraiIcms(text);
        String icmsst = extraiIcmsst(text);
        String ipi = extraiIpi(text);


        // Adicionar informações do cabeçalho
        Element cabecalho = document.createElement("cabecalho");
        rootElement.appendChild(cabecalho);

        Element numOrc = document.createElement("numero");
        numOrc.appendChild(document.createTextNode(numeroOrcamento));
        cabecalho.appendChild(numOrc);

        Element dataElement = document.createElement("data");
        dataElement.appendChild(document.createTextNode(data));
        cabecalho.appendChild(dataElement);

        // Informações do cliente
        Element clienteElement = document.createElement("cliente");
        rootElement.appendChild(clienteElement);

        Element nomeCliente = document.createElement("nome");
        nomeCliente.appendChild(document.createTextNode(nome));
        clienteElement.appendChild(nomeCliente);

        Element enderecoElement = document.createElement("endereco");
        enderecoElement.appendChild(document.createTextNode(endereco));
        clienteElement.appendChild(enderecoElement);

        //Condições de Pagamento e Impostos
        Element pagamentoElement = document.createElement("pagamento");
        rootElement.appendChild(pagamentoElement);

        Element condicaoDePagamento = document.createElement("condicaoPagamento");
        condicaoDePagamento.appendChild(document.createTextNode(condicaoPagamento));
        pagamentoElement.appendChild(condicaoDePagamento);

        Element nomeVendedor = document.createElement("vendedor");
        nomeVendedor.appendChild(document.createTextNode(vendedor));
        pagamentoElement.appendChild(nomeVendedor);

        Element nomeBeneficiador = document.createElement("beneficiador");
        nomeBeneficiador.appendChild(document.createTextNode(beneficiador));
        pagamentoElement.appendChild(nomeBeneficiador);

        Element nomeTransportadora = document.createElement("transportadora");
        nomeTransportadora.appendChild(document.createTextNode(transportadora));
        pagamentoElement.appendChild(nomeTransportadora);

        Element tipoDeVenda = document.createElement("tipoVenda");
        tipoDeVenda.appendChild(document.createTextNode(tipoVenda));
        pagamentoElement.appendChild(tipoDeVenda);

        Element nomeDesconto = document.createElement("desconto");
        nomeDesconto.appendChild(document.createTextNode(desconto));
        pagamentoElement.appendChild(nomeDesconto);

        Element nomeSeguro = document.createElement("seguro");
        nomeSeguro.appendChild(document.createTextNode(seguro));
        pagamentoElement.appendChild(nomeSeguro);

        Element nomeFrete = document.createElement("frete");
        nomeFrete.appendChild(document.createTextNode(frete));
        pagamentoElement.appendChild(nomeFrete);

        Element nomeOutras = document.createElement("outras");
        nomeOutras.appendChild(document.createTextNode(outras));
        pagamentoElement.appendChild(nomeOutras);

        Element nomeSeuPedido = document.createElement("seuPedido");
        nomeSeuPedido.appendChild(document.createTextNode(seuPedido));
        pagamentoElement.appendChild(nomeSeuPedido);

        Element valorIcms = document.createElement("icms");
        valorIcms.appendChild(document.createTextNode(icms));
        pagamentoElement.appendChild(valorIcms);

        Element valorIcmsst = document.createElement("icmsst");
        valorIcmsst.appendChild(document.createTextNode(icmsst));
        pagamentoElement.appendChild(valorIcmsst);

        Element valorIpi = document.createElement("ipi");
        valorIpi.appendChild(document.createTextNode(ipi));
        pagamentoElement.appendChild(valorIpi);

        // Lista de produtos
        Element produtos = document.createElement("produtos");
        rootElement.appendChild(produtos);

        // Extrair produtos do texto
        List<Map<String, String>> produtosList = extrairProdutos(text);
        for (Map<String, String> produto : produtosList) {

            Element produtoElement = document.createElement("produto");
            produtos.appendChild(produtoElement);

            Element codigo = document.createElement("codigo");
            codigo.appendChild(document.createTextNode(produto.get("codigo")));
            produtoElement.appendChild(codigo);

            Element descricao = document.createElement("descricao");
            descricao.appendChild(document.createTextNode(produto.get("descricao")));
            produtoElement.appendChild(descricao);

            Element quantidade = document.createElement("quantidade");
            quantidade.appendChild(document.createTextNode(produto.get("quantidade")));
            produtoElement.appendChild(quantidade);

            Element preco = document.createElement("preco");
            preco.appendChild(document.createTextNode(produto.get("preco")));
            produtoElement.appendChild(preco);

            Element valordesconto = document.createElement("desconto");
            valordesconto.appendChild(document.createTextNode(produto.get("desconto")));
            produtoElement.appendChild(valordesconto);

            Element liquido = document.createElement("liquido");
            liquido.appendChild(document.createTextNode(produto.get("liquido")));
            produtoElement.appendChild(liquido);

            Element valoripi = document.createElement("ipi");
            valoripi.appendChild(document.createTextNode(produto.get("ipi")));
            produtoElement.appendChild(valoripi);

            Element total = document.createElement("total");
            total.appendChild(document.createTextNode(produto.get("total")));
            produtoElement.appendChild(total);
        }

        Element totais = document.createElement("totais");
        rootElement.appendChild(totais);

        Element valorTotal = document.createElement("valorTotal");
        valorTotal.appendChild(document.createTextNode(extrairValorTotal(text)));
        totais.appendChild(valorTotal);

        return convertDocumentToString(document);
    }

    private String convertDocumentToString(Document document) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        return writer.toString();
    }

    private String extrairNumeroOrcamento(String text) {
        Pattern pattern = Pattern.compile("Orçamento de Venda - (\\d+)");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1) : "";
    }

    private String extrairData(String text) {
        Pattern pattern = Pattern.compile("(\\d{2}/\\d{2}/\\d{4})");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1) : "";
    }

    private String extrairCliente(String text) {
        Pattern pattern = Pattern.compile("DDL ([A-Z\\s]+)");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1) : "";
    }

    private String extrairNome(String text) {
        Pattern pattern = Pattern.compile("([^\\n]+)");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1) : "";
    }

    private String extrairEndereco(String text) {
        Pattern pattern = Pattern.compile("AV ([^\\n]+)");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1) : "";
    }

    private String extrairCondicaoPagamento(String text) {
        Pattern pattern = Pattern.compile("Cond\\.Pagamento\\s+(\\d+)");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1).trim() : "";
    }

    private String extrairVendedor(String text) {
        Pattern pattern = Pattern.compile("Vendedor\\s+\\d+\\s+([A-Z ]+)");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1).trim() : "";
    }

    private String extrairBeneficiador(String text) {
        Pattern pattern = Pattern.compile("Beneficiador\\s+\\d+\\s+([A-Z ]+)");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1).trim() : "";
    }

    private String extrairTransportadora(String text) {
        Pattern pattern = Pattern.compile("Transportadora\\s+\\d+\\s+([A-Z ]+)");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1).trim() : "";
    }

    private String extrairTipoVenda(String text) {
        Pattern pattern = Pattern.compile("Tipo de Venda\\s+(\\d+)");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1).trim() : "";
    }

    private String extraiDesconto(String text) {
        Pattern pattern = Pattern.compile("Desconto\\s+([\\d,.]+)");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1) : "";
    }

    private String extraiSeguro(String text) {
        Pattern pattern = Pattern.compile("([\\d,.]+)");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1) : "";
    }

    private String extraiFrete(String text) {
        Pattern pattern = Pattern.compile("([A-Z\\s]+)");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1) : "";
    }

    private String extraiOutras(String text) {
        Pattern pattern = Pattern.compile("([A-Z\\s]+)");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1) : "";
    }

    private String extraiSeuPedido(String text) {
        Pattern pattern = Pattern.compile("([A-Z\\s]+)");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1) : "";
    }

    private String extraiIcms(String text) {
        Pattern pattern = Pattern.compile("([\\d,.]+)");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1) : "";
    }

    private String extraiIcmsst(String text) {
        Pattern pattern = Pattern.compile("([\\d,.]+)");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1) : "";
    }

    private String extraiIpi(String text) {
        Pattern pattern = Pattern.compile("([\\d,.]+)");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1) : "";
    }

    private List<Map<String, String>> extrairProdutos(String text) {
        List<Map<String, String>> produtos = new ArrayList<>();

            Pattern pattern = Pattern.compile("([A-Z0-9-]+)\\s+([^\\n]+)\\s+([^\\n]+)\\s+([^\\n]+)\\s+([^\\n]+)\\s+([\\d,.]+)\\s+([\\d,.]+)\\s+([\\d,.]+)");
            Matcher matcher = pattern.matcher(text);

        List<String> palavrasIgnoradas = Arrays.asList(
                "Cond. Pagamento", "Vendedor", "Beneficiador", "Transportadora", "Tipo de Venda",
                "Desconto", "Seguro", "Frete", "Outras D."
        );

        while (matcher.find()) {
            String descricaoProduto = matcher.group(2).trim();

            // Se a descrição contém alguma das palavras-chave, pula essa linha
            boolean ignorar = palavrasIgnoradas.stream().anyMatch(descricaoProduto::contains);
            if (ignorar) {
                continue;
            }

                Map<String, String> produto = new HashMap<>();
                produto.put("codigo", matcher.group(1));
                produto.put("descricao", matcher.group(2));
                produto.put("quantidade", matcher.group(3));
                produto.put("preco", matcher.group(4));
                produto.put("desconto", matcher.group(5));
                produto.put("liquido", matcher.group(6));
                produto.put("ipi", matcher.group(7));
                produto.put("total", matcher.group(8));
                produtos.add(produto);
            }
        return produtos;
    }

    private String extrairValorTotal(String text) {
        Pattern pattern = Pattern.compile("R\\$ ([\\d,.]+)");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1) : "";
    }

    // Classe para receber o request
    public static class SaveXmlRequest {
        private String content;
        private String filePath;

        // Getters e setters
        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }
    }
}
