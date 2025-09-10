package br.com.codex.v1.service;

import br.com.codex.v1.domain.cadastros.Usuario;
import br.com.codex.v1.domain.dto.SolicitacaoCompraDto;
import br.com.codex.v1.domain.repository.UsuarioRepository;
import br.com.codex.v1.utilitario.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.mail.internet.MimeMessage;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EnviaEmailService implements EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EnviaEmailService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Value("${spring.mail.username}")
    private String remetente;

    private static final String NOME_DEPARTAMENTO = "Compras";

    private String carregarTemplate(String nomeArquivo) {

        try {
            ClassPathResource resource = new ClassPathResource("/email_templates/" + nomeArquivo);
            InputStream inputStream = resource.getInputStream();
            byte[] bdata = FileCopyUtils.copyToByteArray(inputStream);
            return new String(bdata, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar template: " + nomeArquivo, e);
        }
    }

    private String processarTemplate(String template, Map<String, Object> variaveis) {
        String resultado = template;

        for (Map.Entry<String, Object> entry : variaveis.entrySet()) {
            String chave = "{{" + entry.getKey() + "}}";
            String valor = entry.getValue() != null ? entry.getValue().toString() : "";
            resultado = resultado.replace(chave, valor);
        }

        // Processar condicionais
        if (variaveis.containsKey("urgente") && Boolean.TRUE.equals(variaveis.get("urgente"))) {
            resultado = resultado.replace("{{#urgente}}", "").replace("{{/urgente}}", "");
        } else {
            resultado = resultado.replaceAll("\\{\\{#urgente\\}\\}[\\s\\S]*?\\{\\{/urgente\\}\\}", "");
        }

        // Processar filtros personalizados
        resultado = resultado.replace(" | emailCodex", "");

        return resultado;
    }

    public String sendSimpleMail(SolicitacaoCompraDto solicitacaoCompraDto) {

        String dataFormatada = "";
        int emailsEnviados = 0;
        int emailsComErro = 0;
        StringBuilder resultado = new StringBuilder();

        String subject = "Notificação Solicitação de Compra " + " - " + solicitacaoCompraDto.getSolicitante() + " realizou uma solicitação de compra";

        try {
            List<Usuario> usuariosCompradores = usuarioRepository.findByDepartamento(NOME_DEPARTAMENTO);

            if (usuariosCompradores.isEmpty()) {
                return "Nenhum usuário com perfil COMPRADOR encontrado para envio de e-mail";
            }

            List<String> destinatarios = usuariosCompradores.stream()
                    .map(Usuario::getEmail)
                    .filter(email -> email != null && !email.trim().isEmpty())
                    .toList();

            if (destinatarios.isEmpty()) {
                return "Usuários com perfil COMPRADOR encontrados, mas nenhum e-mail válido";
            }

            // Carregar o template HTML
            String template = carregarTemplate("email-solicitacao-compra.html");

            if (solicitacaoCompraDto.getDataSolicitacao() != null) {
                try {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date data = inputFormat.parse(solicitacaoCompraDto.getDataSolicitacao().toString());
                    dataFormatada = outputFormat.format(data);
                } catch (Exception e) {
                    logger.warn("Erro ao formatar data, usando valor original: {}", e.getMessage());
                    dataFormatada = solicitacaoCompraDto.getDataSolicitacao().toString();
                }
            }

            // Preparar variáveis (fazemos isso uma vez só)
            Map<String, Object> variaveis = new HashMap<>();
            variaveis.put("{{solicitante}}", solicitacaoCompraDto.getSolicitante());
            variaveis.put("{{dataSolicitacao}}", dataFormatada);
            variaveis.put("{{departamento}}", solicitacaoCompraDto.getDepartamento());
            variaveis.put("{{centroCusto}}", solicitacaoCompraDto.getCentroCusto());
            variaveis.put("{{motivoCompra}}", solicitacaoCompraDto.getMotivoCompra());
            // Remova completamente a lógica de blocos condicionais
            variaveis.put("urgente", Boolean.parseBoolean(solicitacaoCompraDto.getUrgente()) ? "URGENTE" : "");

            // Processar template (fazemos isso uma vez só)
            String message = processarTemplate(template, variaveis);

            for (String destinatario : destinatarios) {
                try {
                    logger.debug("Preparando email para: {}", destinatario);

                    // Criando uma mensagem MIME para cada destinatário
                    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                    helper.setFrom(remetente);
                    helper.setTo(destinatario); // Apenas UM destinatário por vez
                    helper.setSubject(subject);
                    helper.setText(message, true);

                    // Carregando a imagem da assinatura
                    //ClassPathResource imageResource = new ClassPathResource("/templates/assinatura_ti.png");
                    //helper.addInline("assinaturaImagem", imageResource);

                    logger.debug("Enviando email para: {}", destinatario);
                    javaMailSender.send(mimeMessage);

                    emailsEnviados++;
                    logger.info("Email enviado com sucesso para: {}", destinatario);

                } catch (Exception e) {
                    emailsComErro++;
                    logger.error("ERRO ao enviar e-mail para {}: {}", destinatario, e.getMessage(), e);
                    resultado.append("Erro para ").append(destinatario).append(": ").append(e.getMessage()).append("; ");
                }
            }

            // Resultado final
            if (emailsComErro == 0) {
                return "Todos os " + emailsEnviados + " e-mails enviados com sucesso";
            } else {
                return emailsEnviados + " e-mails enviados, " + emailsComErro + " com erro. Detalhes: " + resultado.toString();
            }

        } catch (Exception e) {
            logger.error("ERRO geral no processamento do email: {}", e.getMessage(), e);
            return "Erro geral ao processar e-mail: " + e.getMessage();
        }
    }
}
