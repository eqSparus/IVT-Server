package ru.example.ivtserver.email;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.function.UnaryOperator;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class MailEmailProvider implements EmailProvider {

    @Value("${spring.mail.username}")
    String emailSender;

    final JavaMailSender mailSender;
    final ITemplateEngine templateEngine;

    @Autowired
    public MailEmailProvider(JavaMailSender mailSender,
                             ITemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendEmail(@NonNull String toAddress, @NonNull String title, @NonNull String mail) {
        mailSender.send(mimeMessage -> {
            var helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.toString());
            helper.setFrom(emailSender);
            helper.setTo(toAddress);
            helper.setSubject(title);
            helper.setText(mail, true);
        });
    }

    @Override
    public String getMailHtml(@NonNull String title, @NonNull UnaryOperator<Context> variable) {
        return templateEngine.process(title, variable.apply(new Context()));
    }

    @NonNull
    @Override
    public String getMailHtml(@NonNull String title) {
        return getMailHtml(title, context -> context);
    }
}
