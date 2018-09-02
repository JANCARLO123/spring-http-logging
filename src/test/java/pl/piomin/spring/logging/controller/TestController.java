package pl.piomin.spring.logging.controller;

import org.springframework.web.bind.annotation.*;
import pl.piomin.spring.logging.domain.TestExample;
import pl.piomin.spring.logging.domain.TestException;

@RestController
@RequestMapping("/example")
public class TestController {


    @GetMapping("/{name}")
    public TestExample hello(@PathVariable("name") String name) {
        return new TestExample(1, name);
    }

    @GetMapping("/error")
    public TestExample error() throws TestException {
        throw new TestException("TestError");
    }

    @PostMapping
    public TestExample add(@RequestBody TestExample example) {
        return example;
    }

    @PutMapping
    public void update(@RequestBody TestExample example) {

    }
}
