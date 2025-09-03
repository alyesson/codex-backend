package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.SolicitacaoCompraDto;
import br.com.codex.v1.utilitario.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.reflections.Reflections.log;

@Service
public class EnviaEmailService implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String remetente;

    String destinatario = "";

    public String sendSimpleMail(SolicitacaoCompraDto solicitacaoCompraDto) {
        try {

            // Carregar o template HTML
            String templateContent = new String(Files.readAllBytes(
                    Paths.get(getClass().getResource("/templates/email-solicitacao-compra.html").toURI())
            ));

            // Preencher o template com os dados
            String message = templateContent
                    .replace("{{numeroSolicitacao}}", solicitacaoCompraDto.getId().toString())
                    .replace("{{solicitante}}", solicitacaoCompraDto.getSolicitante())
                    .replace("{{dataSolicitacao}}", solicitacaoCompraDto.getDataSolicitacao().toString())
                    .replace("{{departamento}}", solicitacaoCompraDto.getDepartamento())
                    .replace("{{centroCusto}}", solicitacaoCompraDto.getCentroCusto())
                    .replace("{{motivoCompra}}", solicitacaoCompraDto.getMotivoCompra())
                    .replace("{{urgente}}", solicitacaoCompraDto.getUrgente());

            // Remover seção urgente se não for urgente
            if (!Boolean.parseBoolean(solicitacaoCompraDto.getUrgente())) {
                message = message.replace("{{#urgente}}", "").replace("{{/urgente}}", "");
            }

            String subject = "📦 Solicitação de Compra #" + solicitacaoCompraDto.getId() +
                    " - " + solicitacaoCompraDto.getSolicitante();

            // Criar mensagem MIME
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(remetente);
            helper.setTo(destinatario);
            helper.setSubject(subject);
            helper.setText(message, true);

            // Adicionar imagem da assinatura
            ClassPathResource imageResource = new ClassPathResource("/templates/assinatura_ti.png");
            helper.addInline("assinaturaImagem", imageResource);

            javaMailSender.send(mimeMessage);
            return "E-mail enviado com sucesso para " + destinatario;

        } catch (Exception e) {
            log.error("Erro ao enviar e-mail: {}", e.getMessage(), e);
            return "Erro ao enviar e-mail: " + e.getMessage();
        }
    }

    /*public String sendSimpleMail(SolicitacaoCompraDto solicitacaoCompraDto){

        String subject = "Solicitação de Compra" + " - " + solicitacaoCompraDto.getSolicitante();

        // Montando a mensagem HTML
        String message = "<p>Você recebeu este e-mail pois o usuário " + solicitacaoCompraDto.getSolicitante() +
                " fez uma solicitação de compra</p>";
        message += "<font color=blue>Nome Solicitante:</font> " + solicitacaoCompraDto.getSolicitante() + "<br/>";
        message += "<font color=blue>Data Abertura:</font> " + solicitacaoCompraDto.getDataSolicitacao() + "<br/>";
        message += "<font color=blue>Departamento:</font> " + solicitacaoCompraDto.getDepartamento() + "<br/>";
        message += "<font color=blue>Centro de Custo:</font> " + solicitacaoCompraDto.getCentroCusto() + "<br/>";
        message += "<font color=blue>Motivo:</font> " + solicitacaoCompraDto.getMotivoCompra() + "<br/>";
        // Adicionando a assinatura com a imagem
        message += "<br/><p>Atenciosamente,</p>";
        message += "<p>Sistema Codex</p>";
        message += "<img src='cid:assinaturaImagem'>"; // Referência ao Content-ID

        try {
            // Criando uma mensagem MIME
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(remetente);
            helper.setTo(destinatario);
            helper.setSubject(subject);
            helper.setText(message, true); // O segundo argumento 'true' define que o conteúdo é HTML

            // Carregando a imagem da assinatura
            ClassPathResource imageResource = new ClassPathResource("/email_templates/assinatura_ti.png");
            helper.addInline("assinaturaImagem", imageResource); // Associando o Content-ID "assinaturaImagem" com o arquivo

            javaMailSender.send(mimeMessage);
            return "E-mail enviado com sucesso para " + destinatario;
        } catch (Exception e) {
            return "Erro ao enviar e-mail: " + e.getMessage();
        }
    }*/
}
