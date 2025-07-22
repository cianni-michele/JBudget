package it.unicam.cs.mpgc.jbudget109164.model.movement;

import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public class SimpleMovementFactory implements MovementFactory {

    @Override
    public Movement createMovement(UUID id, MovementDetails details) {
        return new SimpleMovement(id, details, new HashSet<>());
    }

    @Override
    public Movement createMovement(UUID id, MovementDetails details, Collection<Tag> tags) {
        return new SimpleMovement(id, details, new HashSet<>(tags));
    }

}
