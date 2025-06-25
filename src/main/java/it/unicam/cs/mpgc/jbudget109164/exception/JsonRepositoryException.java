package it.unicam.cs.mpgc.jbudget109164.exception;

public class JsonRepositoryException extends RuntimeException {
    public JsonRepositoryException(String message) {
        super(message);
    }

    public JsonRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
