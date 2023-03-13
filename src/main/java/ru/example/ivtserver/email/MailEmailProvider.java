package ru.example.ivtserver.email;

import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.UnaryOperator;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class MailEmailProvider implements EmailProvider {

    @Value("${mail.username}")
    String senderName;

    @Value("${spring.mail.username}")
    String senderEmail;

    final JavaMailSender mailSender;
    final ITemplateEngine templateEngine;

    @Autowired
    public MailEmailProvider(JavaMailSender mailSender,
                             ITemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendEmail(String toAddress, String title, String message) {
        sendEmail(toAddress, title, message, Map.of());
    }

    @Override
    public void sendEmail(String toAddress, String title,
                          String message, Map<String, Resource> resources) {
        mailSender.send(mimeMessage -> {
            var helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.toString());
            helper.setFrom(senderEmail, senderName);
            helper.setTo(toAddress);
            helper.setSubject(title);
            helper.setText(message, true);
            resources.forEach((s, resource) -> {
                try {
                    helper.addInline(s, resource);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    @Override
    public String getMailHtml(String name, UnaryOperator<Context> variable) {
        return templateEngine.process(name, variable.apply(new Context()));
    }

    @Override
    public String getMailHtml(String name) {
        return getMailHtml(name, context -> context);
    }

}
