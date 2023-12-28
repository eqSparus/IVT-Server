package ru.example.ivtserver.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;

/**
 * Общая конфигурация к приложению
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Configuration
@EnableAsync
public class ApplicationConfig implements AsyncConfigurer {

    /**
     * Создание объекта ObjectMapper, который может сериализовать и десериализовать объекты Java в JSON и обратно.
     * В данном случае, метод создает {@link ObjectMapper}, который зарегистрировали модуль
     * {@link JavaTimeModule} для корректной обработки дат и времени в формате ISO 8601.
     *
     * @return экземпляр {@link ObjectMapper} с зарегистрированным модулем {@link JavaTimeModule}.
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        var timeModule = new JavaTimeModule();
        return new ObjectMapper()
                .registerModule(timeModule);
    }

    /**
     * Этот метод создает экземпляр {@link ITemplateEngine} для конфигурирования Thymeleaf шаблонов.
     *
     * @return экземпляр {@link ITemplateEngine} для конфигурирования Thymeleaf шаблонов.
     */
    @Bean
    public ITemplateEngine templateEngine() {
        var templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(templateResolver());
        return templateEngine;
    }

    /**
     * Создание объекта {@link ITemplateResolver} для настройки разрешения шаблонов Thymeleaf.
     *
     * @return {@link ITemplateResolver} объект-реализацию, настроенный для разрешения шаблонов Thymeleaf
     */
    @Bean
    public ITemplateResolver templateResolver() {
        var resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("/mail/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        resolver.setCacheable(false);
        resolver.setOrder(0);
        return resolver;
    }

    /**
     * Настраивает экземпляр {@link Executor}, который использует пул потоков для выполнения асинхронных задач.
     *
     * @return Executor - настроенный экземпляр {@link ThreadPoolTaskExecutor}
     */
    @Override
    public Executor getAsyncExecutor() {
        var executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("AsyncTaskThread::");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }
}
