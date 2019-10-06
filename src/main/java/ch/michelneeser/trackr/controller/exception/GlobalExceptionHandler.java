package ch.michelneeser.trackr.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(StatNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String statNotFoundHandler(StatNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(NoValueProvidedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String noValueProvidedHandler(NoValueProvidedException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(StatValueNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String statValueNotFoundHandler(StatValueNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String illegalArgumentHandler(IllegalArgumentException ex) {
        return ex.getMessage();
    }

}