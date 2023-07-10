package ru.example.ivtserver;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;


@SpringBootApplication
@Log4j2
public class IvtServerApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(IvtServerApplication.class, args);
    }
}
