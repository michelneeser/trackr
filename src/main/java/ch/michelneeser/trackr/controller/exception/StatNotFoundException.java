package ch.michelneeser.trackr.controller.exception;

public class StatNotFoundException extends RuntimeException {

    public StatNotFoundException(String token) {
        super("Could not find stat with token " + token);
    }

}