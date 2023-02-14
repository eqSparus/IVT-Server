package ru.example.ivtserver.rest;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.example.ivtserver.entities.Test;
import ru.example.ivtserver.repositories.TestTestRepositoryImpl;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@Log4j2
public class TestControllerImpl {

    TestTestRepositoryImpl testTest;

    @Autowired
    public TestControllerImpl(TestTestRepositoryImpl testTest) {
        this.testTest = testTest;
    }

    @PostMapping(path = "/test")
    public String create() {
        var test = Test.builder().name("Тест").build();
        log.info("Тест: {}",test);
        testTest.save(test);
        return "";
    }

    @GetMapping(path = "/test")
    public Test get(@RequestParam("id")String id) {
        return testTest.findById(id).orElseThrow();
    }

}
