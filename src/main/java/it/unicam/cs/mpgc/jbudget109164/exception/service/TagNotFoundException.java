package it.unicam.cs.mpgc.jbudget109164.exception.service;

import java.util.UUID;

public class TagNotFoundException extends RuntimeException {

    public TagNotFoundException(String message){
        super(message);
    }

    public TagNotFoundException(UUID id) {
        this("Tag with id " + id + " not found.");
    }
}
