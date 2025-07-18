package it.unicam.cs.mpgc.jbudget109164.model;

public interface Entity<I, D extends EntityDetails> {

    I getId();

    D getDetails();
    
}
