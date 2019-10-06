package ch.michelneeser.trackr.controller.exception;

public class StatValueNotFoundException extends RuntimeException {

    public StatValueNotFoundException(String token, long statValueId) {
        super(String.format("could not find stat value '%d' of token '%s'", statValueId, token));
    }

}