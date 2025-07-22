package it.unicam.cs.mpgc.jbudget109164.model.movement;

import it.unicam.cs.mpgc.jbudget109164.model.Entity;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

public interface Movement extends Entity<MovementDetails> {

    LocalDate getDate();

    double getAmount();

    String getDescription();

    void addTags(Collection<Tag> tags);

    void addTag(Tag tag);

    void removeTag(Tag tag);

    boolean isTaggedBy(Tag tag);

    Set<Tag> getTags();

    Movement copy(MovementDetails details);
}
