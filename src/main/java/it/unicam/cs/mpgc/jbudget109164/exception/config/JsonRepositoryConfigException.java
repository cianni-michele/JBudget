package it.unicam.cs.mpgc.jbudget109164.exception.config;

public class JsonRepositoryConfigException extends RuntimeException {

    public JsonRepositoryConfigException(Class<?> aClass) {
        super("No directory configured for repository: " + aClass.getName());
    }
}
