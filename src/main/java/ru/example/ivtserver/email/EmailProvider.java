package ru.example.ivtserver.email;

import org.springframework.core.io.Resource;
import org.thymeleaf.context.Context;

import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * Этот интерфейс предоставляет методы для отправки электронной почты.
 */
public interface EmailProvider {

    /**
     * Отправляет письмо на указанный адрес со значением темы и сообщения.
     * @param toAddress адрес электронной почты для отправки письма
     * @param title заголовок письма
     * @param message сообщение для включения в письмо
     */
    void sendEmail(String toAddress, String title, String message);

    /**
     * Отправляет письмо на указанный адрес со значением темы, сообщения и ресурсов.
     * @param toAddress адрес электронной почты для отправки письма
     * @param title заголовок письма
     * @param message сообщение для включения в письмо
     * @param resources карта имен ресурсов для включения в письмо
     */
    void sendEmail(String toAddress, String title, String message, Map<String, Resource> resources);

    /**
     * Возвращает HTML для шаблона электронной почты по имени с переменными.
     * @param name имя шаблона электронной почты
     * @param variable функция, которая принимает объект {@link Context} для наполнения переменных HTML шаблона
     * @return HTML для шаблона электронной почты
     */
    String getMailHtml(String name, UnaryOperator<Context> variable);

    /**
     * Возвращает HTML для шаблона электронной почты по имени.
     * @param name имя шаблона электронной почты
     * @return HTML для шаблона электронной почты
     */
    String getMailHtml(String name);
}
