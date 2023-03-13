package ru.example.ivtserver.configuratuons;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.auditing.EnableCouchbaseAuditing;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;
import org.springframework.data.domain.AuditorAware;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.ZonedDateTime;
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
}
