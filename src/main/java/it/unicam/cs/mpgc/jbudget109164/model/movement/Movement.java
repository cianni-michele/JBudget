package it.unicam.cs.mpgc.jbudget109164.model.movement;

import it.unicam.cs.mpgc.jbudget109164.model.Entity;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public interface Movement extends Entity<UUID, MovementDetails> {

    LocalDate getDate();

    double getAmount();

    String getDescription();

    void addTag(Tag tag);

    void removeTag(Tag tag);

    boolean hasTag(Tag tag);

    Set<Tag> getTags();

    Movement copy(MovementDetails details);    
}
