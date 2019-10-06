package ch.michelneeser.trackr.controller.exception;

public class NoValueProvidedException extends RuntimeException {

    public NoValueProvidedException(String token) {
        super(String.format("no stat value provided for adding to token '%s'", token));
    }

}