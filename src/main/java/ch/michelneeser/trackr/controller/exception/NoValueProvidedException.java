package ch.michelneeser.trackr.controller.exception;

public class NoValueProvidedException extends RuntimeException {

    public NoValueProvidedException(String token) {
        super("No stat value provided for adding to token " + token);
    }

}