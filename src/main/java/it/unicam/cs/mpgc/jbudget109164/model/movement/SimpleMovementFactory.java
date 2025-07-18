package it.unicam.cs.mpgc.jbudget109164.model.movement;

import java.util.HashSet;
import java.util.UUID;

public class SimpleMovementFactory implements MovementFactory {

    @Override
    public Movement createMovement(UUID id, MovementDetails details) {
        return new SimpleMovement(id, details, new HashSet<>());
    }
    
}
