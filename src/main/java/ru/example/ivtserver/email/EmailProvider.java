package ru.example.ivtserver.email;

import org.springframework.core.io.Resource;
import org.thymeleaf.context.Context;

import java.util.Map;
import java.util.function.UnaryOperator;

public interface EmailProvider {

    void sendEmail(String toAddress, String title, String message);

    void sendEmail(String toAddress, String title, String message, Map<String, Resource> resources);


    String getMailHtml(String name, UnaryOperator<Context> variable);

    String getMailHtml(String name);

}
