package ru.example.ivtserver.configuratuons;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.auditing.EnableCouchbaseAuditing;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Configuration
@EnableCouchbaseAuditing
@Log4j2
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

}
