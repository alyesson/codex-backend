package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.OrdemCompraDto;
import br.com.codex.v1.utilitario.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class EnviaEmailService implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String remetente;

    String destinatario = "";

    public String sendSimpleMail(OrdemCompraDto ordemCompraDto){

        String subject = "Solicitação de Compra" + " - " + ordemCompraDto.getSolicitante();

        // Montando a mensagem HTML
        String message = "<p>Você recebeu este e-mail pois o usuário " + ordemCompraDto.getSolicitante() +
                " fez uma solicitação de compra</p>";
        message += "<font color=blue>Nome Solicitante:</font> " + ordemCompraDto.getSolicitante() + "<br/>";
        message += "<font color=blue>Data Abertura:</font> " + ordemCompraDto.getDataSolicitacao() + "<br/>";
        message += "<font color=blue>Departamento:</font> " + ordemCompraDto.getDepartamento() + "<br/>";
        message += "<font color=blue>Centro de Custo:</font> " + ordemCompraDto.getCentroCusto() + "<br/>";
        message += "<font color=blue>Motivo:</font> " + ordemCompraDto.getMotivoCompra() + "<br/>";
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
            ClassPathResource imageResource = new ClassPathResource("/templates/assinatura_ti.png");
            helper.addInline("assinaturaImagem", imageResource); // Associando o Content-ID "assinaturaImagem" com o arquivo

            javaMailSender.send(mimeMessage);
            return "E-mail enviado com sucesso para " + destinatario;
        } catch (Exception e) {
            return "Erro ao enviar e-mail: " + e.getMessage();
        }
    }
}
