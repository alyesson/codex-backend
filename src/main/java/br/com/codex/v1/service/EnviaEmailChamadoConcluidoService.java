package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.AtendimentosDto;
import br.com.codex.v1.utilitario.EmailAtendimentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class EnviaEmailChamadoConcluidoService implements EmailAtendimentoService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String remetente;

    public String sendSimpleMail(AtendimentosDto atendimentosDto) {

        String destinatario = atendimentosDto.getEmail();
        String subject = "Chamado Finalizado " + "#" + atendimentosDto.getId() + " - " + atendimentosDto.getTitulo();

        // Montando a mensagem HTML
        String message = "<p>Seu chamado foi concluído #" + atendimentosDto.getId()+ "<br/>";
        message += "<font color=blue>Nome Solicitante:</font> " + atendimentosDto.getSolicitante() + "<br/>";
        message += "<font color=blue>Data Abertura:</font> " + atendimentosDto.getDataAbertura() + "<br/>";
        message += "<font color=blue>Problema:</font> " + atendimentosDto.getProblema() + "<br/>";
        message += "<font color=blue>Conclusão:</font> " + atendimentosDto.getResolucao() + "<br/>";
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
