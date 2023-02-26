package ru.example.ivtserver.email;

import org.springframework.lang.NonNull;
import org.thymeleaf.context.Context;

import java.util.function.UnaryOperator;

public interface EmailProvider {

    void sendEmail(@NonNull String toAddress, @NonNull String title, @NonNull String mail);

    @NonNull
    String getMailHtml(@NonNull String title, @NonNull UnaryOperator<Context> variable);

    @NonNull
    String getMailHtml(@NonNull String title);

}
