package br.com.codex.v1.service;

import br.com.codex.v1.domain.cadastros.Usuario;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

@Service
public class EmailRecuperaSenhaService {

    public void recoverPassword(String link, Usuario usuario) throws MessagingException {

        Properties props = new Properties();

        Date dataAtual = new Date();
        SimpleDateFormat dataFormato = new SimpleDateFormat("dd/MM/yyyy");

        String from = "suporte@codexsolucoes.com.br";
        String to = usuario.getEmail();
        String qmEnvia = "suporte@codexsolucoes.com.br";
        String subject = "Recuperação De Senha";
        String message = "E-mail com solicitação de recuperação de senha";
        message += "<p>Obrigado por entrar em contato, você está recebendo este e-mail porque você solicitou uma recuperação de senha</p>";
        message += "<font color=red>Nome Solicitante:</font>" + usuario.getNome() + "<br/>";
        message += "<font color=red>Data Solicitação:</font>" + dataFormato.format(dataAtual) + "<br/>";
        message += "<font color=blue>Link: </font>" + link + "<br/>";
        message += "<*********************************************************************</font>";
        message += "<p>************************* Este é um e-mail automático, por favor não responder *************************</p>";

        props.put("mail.transport.protocol", "smtp"); // define protocolo de envio como SMTP
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.enable","true");
        props.put("mail.smtp.host", "smtp.hostinger.com"); // server SMTP do Servidor
        props.put("mail.smtp.auth", "true"); // ativa autenticacao
        props.put("mail.smtp.user", from); // usuario ou seja, a conta que esta enviando o email (tem que ser do GMAIL)
        props.put("mail.debug", "true");
        props.put("mail.smtp.port", 465); // porta
        props.put("mail.smtp.socketFactory.port", 465); // mesma porta para o socket
        props.put("mail.smtp.socketFactory.fallback", "false");
        SimpleAuthAutomatico auth = null;
        auth = new  SimpleAuthAutomatico(from, "Sup0rt3@2024");

        Session session = Session.getDefaultInstance(props, auth);
        session.setDebug(true);
        Message msg = new MimeMessage(session);

        try {
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            msg.setFrom(new InternetAddress(qmEnvia));
            msg.setSubject(subject);
            msg.setContent(message, "text/html; charset=UTF-8");


        } catch (MessagingException e) {
            throw new MessagingException("Erro: "+e);
        }

        Transport tr;
        try {
            tr = session.getTransport("smtp");
            tr.connect("smtp.hostinger.com", from, "Sup0rt3@2024");
            msg.saveChanges();
            tr.sendMessage(msg, msg.getAllRecipients());
            tr.close();
        } catch (MessagingException e) {
            throw new ObjectNotFoundException("Erro ao enviar e-mail para " + usuario.getEmail() + ": " + e);
        }
    }

    class SimpleAuthAutomatico extends Authenticator {
        public String username = null;
        public String password = null;

        public SimpleAuthAutomatico(String user, String pwd) {
            username = user;
            password = pwd;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    }
}
