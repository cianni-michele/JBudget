package it.unicam.cs.mpgc.jbudget109164.model.movement;

import java.util.UUID;

public interface Movement {

    UUID getId();

    String getDescription();

    double getAmount();

}
