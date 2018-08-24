package pl.piomin.spring.logging.controller;

import org.springframework.web.bind.annotation.*;
import pl.piomin.spring.logging.domain.Example;

@RestController
public class ExampleController {

    @GetMapping("/hello/{name}")
    public Example hello(@PathVariable("name") String name) {
        return new Example(1, name);
    }

    @PostMapping("/example")
    public Example add(@RequestBody Example example) {
        return example;
    }

}
