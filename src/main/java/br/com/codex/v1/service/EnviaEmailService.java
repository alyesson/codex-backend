package br.com.codex.v1.service;

import br.com.codex.v1.domain.cadastros.Usuario;
import br.com.codex.v1.domain.dto.SolicitacaoCompraDto;
import br.com.codex.v1.domain.repository.UsuarioRepository;
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
import java.util.List;
import java.util.stream.Collectors;

import static org.reflections.Reflections.log;

@Service
public class EnviaEmailService implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Value("${spring.mail.username}")
    private String remetente;

    private static final String NOME_DEPARTAMENTO = "Compras";

    public String sendSimpleMail(SolicitacaoCompraDto solicitacaoCompraDto) {

        try {
            List<Usuario> usuariosCompradores = usuarioRepository.findByDepartamento(NOME_DEPARTAMENTO);

            if (usuariosCompradores.isEmpty()) {
                return "Nenhum usuário com perfil COMPRADOR encontrado para envio de e-mail";
            }

            List<String> destinatarios = usuariosCompradores.stream()
                    .map(Usuario::getEmail)
                    .filter(email -> email != null && !email.trim().isEmpty())
                    .collect(Collectors.toList());

            if (destinatarios.isEmpty()) {
                return "Usuários com perfil COMPRADOR encontrados, mas nenhum e-mail válido";
            }

            // Carregar o template HTML
            String templateContent = new String(Files.readAllBytes(
                    Paths.get(getClass().getResource("/email_templates/email-solicitacao-compra.html").toURI())
            ));

            // Preencher o template com os dados
            String message = templateContent
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

            String subject = "📦 Solicitação de Compra #" + solicitacaoCompraDto.getId() + " - " + solicitacaoCompraDto.getSolicitante();

            // Criar mensagem MIME
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(remetente);
            helper.setTo(destinatarios.toArray(new String[0]));
            helper.setSubject(subject);
            helper.setText(message, true);

            // Adicionar imagem da assinatura
            ClassPathResource imageResource = new ClassPathResource("/templates/assinatura_ti.png");
            helper.addInline("assinaturaImagem", imageResource);

            javaMailSender.send(mimeMessage);
            return "E-mail enviado com sucesso para " + destinatarios;

        } catch (Exception e) {
            log.error("Erro ao enviar e-mail: {}", e.getMessage(), e);
            return "Erro ao enviar e-mail: " + e.getMessage();
        }
    }
}
