package ch.michelneeser.trackr.controller.exception;

public class StatNotFoundException extends RuntimeException {

    public StatNotFoundException(String token) {
        super(String.format("could not find stat with token '%s'", token));
    }

}