package pl.piomin.spring.logging.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.piomin.spring.logging.domain.TestExample;
import pl.piomin.spring.logging.domain.TestException;

@ControllerAdvice
public class TestControllerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestControllerAdvice.class);

    @ExceptionHandler(TestException.class)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public TestExample handleException() {
        LOGGER.info("Handling");
        return new TestExample(1, "TestException");
    }

}
