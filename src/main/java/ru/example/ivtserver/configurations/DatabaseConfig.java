package ru.example.ivtserver.configurations;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.core.convert.CouchbaseCustomConversions;
import org.springframework.data.couchbase.repository.auditing.EnableCouchbaseAuditing;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;
import org.springframework.data.domain.AuditorAware;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Configuration
@EnableCouchbaseAuditing
@EnableTransactionManagement
@EnableCouchbaseRepositories(basePackages = "ru.example.ivtserver.repositories")
public class DatabaseConfig extends AbstractCouchbaseConfiguration {

    @Value("${database.connection-string}")
    String connectionString;

    @Value("${database.username}")
    String username;

    @Value("${database.password}")
    String password;

    @Value("${database.bucket-name}")
    String bucketName;

    @Override
    public String getConnectionString() {
        return connectionString;
    }

    @Override
    public String getUserName() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getBucketName() {
        return bucketName;
    }

    @Override
    protected boolean autoIndexCreation() {
        return true;
    }


    @Bean
    public AuditorAware<ZonedDateTime> auditorAware() {
        return new ZoneDateTimeAuditorAware();
    }

    private static class ZoneDateTimeAuditorAware implements AuditorAware<ZonedDateTime> {

        @Override
        public Optional<ZonedDateTime> getCurrentAuditor() {
            return Optional.of(ZonedDateTime.now());
        }
    }

    @Override
    public CouchbaseCustomConversions customConversions() {
        return new CouchbaseCustomConversions(Arrays.asList(PathToStringConverter.INSTANCE,
                StringToPathConverter.INSTANCE));
    }

    @WritingConverter
    public enum PathToStringConverter implements Converter<Path, String> {
        INSTANCE;

        @Override
        public String convert(Path value) {
            return value.toString();
        }
    }

    @ReadingConverter
    public enum StringToPathConverter implements Converter<String, Path> {
        INSTANCE;

        @Override
        public Path convert(String value) {
            return Path.of(value);
        }
    }
}
