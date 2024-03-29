package ru.example.ivtserver.email;

import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
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

/**
 * Этот класс реализует интерфейс {@link EmailProvider} и предоставляет методы для отправки электронной почты
 * с использованием {@link JavaMailSender} и шаблонизатора {@link ITemplateEngine}.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Log4j2
@Service
@RequiredArgsConstructor
public class MailEmailProvider implements EmailProvider {

    @Value("${mail.username}")
    String senderName;

    @Value("${spring.mail.username}")
    String senderEmail;

    @Value("${frontend.hostname}")
    String hostnameFrontend;

    final JavaMailSender mailSender;
    final ITemplateEngine templateEngine;

    /**
     * Отправляет электронное письмо на указанный адрес с указанной темой и сообщением.
     *
     * @param toAddress адрес на который нужно отправить электронное письмо
     * @param title     тема электронного письма
     * @param message   сообщение, которое нужно отправить
     */
    @Override
    public void sendEmail(String toAddress, String title, String message) {
        sendEmail(toAddress, title, message, Map.of());
    }

    /**
     * Отправляет электронное письмо на указанный адрес с указанной темой и сообщением, а также с встроенными
     * ресурсами, если они указаны.
     *
     * @param toAddress адрес на который нужно отправить электронное письмо
     * @param title     тема электронного письма
     * @param message   сообщение, которое нужно отправить
     * @param resources встроенные ресурсы (например, изображения) в соответствии с переменой HTML разметки
     */
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
                    log.error("Не удалось добавить ресурс", e);
                }
            });
        });
    }

    /**
     * Обрабатывает HTML-шаблон с указанным именем, используя движок шаблонов
     * после заполнения {@link Context}.
     *
     * @param name     имя HTML-шаблона для обработки
     * @param variable функция для заполнения {@link Context}.
     * @return обработанный HTML-шаблон в виде строки
     */
    @Override
    public String getMailHtml(String name, UnaryOperator<Context> variable) {
        var context = variable.apply(new Context());
        context.setVariable("hostname", hostnameFrontend);
        return templateEngine.process(name, variable.apply(context));
    }

    /**
     * Обрабатывает HTML-шаблон с указанным именем, используя движок шаблонов.
     *
     * @param name имя HTML-шаблона для обработки
     * @return обработанный HTML-шаблон в виде строки
     */
    @Override
    public String getMailHtml(String name) {
        return getMailHtml(name, context -> context);
    }

}
