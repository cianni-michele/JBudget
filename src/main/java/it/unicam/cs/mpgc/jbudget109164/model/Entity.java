package it.unicam.cs.mpgc.jbudget109164.model;

import java.util.UUID;

public interface Entity<D extends EntityDetails> {

    UUID getId();

    D getDetails();
    
}
