package ch.michelneeser.trackr.controller.exception;

public class StatValueNotFoundException extends RuntimeException {

    public StatValueNotFoundException(String token, long statValueId) {
        super(String.format("Could not find stat value %d of token %s", statValueId, token));
    }

}