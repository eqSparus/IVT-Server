package ru.example.ivtserver.rest;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.example.ivtserver.entities.Test;
import ru.example.ivtserver.entities.TestDto;
import ru.example.ivtserver.repositories.TestTestRepositoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@Log4j2
@RequestMapping(path = "/test")
public class TestControllerImpl {

    TestTestRepositoryImpl testTest;

    @Autowired
    public TestControllerImpl(TestTestRepositoryImpl testTest) {
        this.testTest = testTest;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Test create(
            @RequestBody TestDto testDto
    ) {
        log.info("Тело запроса: {}", testDto);
        var test = Test.builder().name(testDto.getName()).build();
        log.info("Объект: {}", test);
        testTest.save(test);
        return test;
    }

    @GetMapping
    public Test get(@RequestParam("id") UUID id) {
        return testTest.findById(id).orElseThrow();
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Test update(
            @RequestBody TestDto testDto,
            @RequestParam UUID id
    ) {

        var test = testTest.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        test.setName(testDto.getName());
        testTest.save(test);
        return test;
    }

    @DeleteMapping(params = {"id"})
    public ResponseEntity<String> delete(@RequestParam("id") UUID id) {
        testTest.deleteById(id);
        return ResponseEntity.ok("Удаление");
    }

    @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Test> getAll() {
        var list = new ArrayList<Test>();
        var iter = testTest.findAll();
        iter.forEach(list::add);
        return list;
    }

}
