package it.unicam.cs.mpgc.jbudget109164.model.scheduled;

import java.util.UUID;

public interface ScheduledMovementsFactory {

    default ScheduledMovements createScheduledMovements(ScheduledMovementsDetails details) {
        return createScheduledMovements(UUID.randomUUID(), details);
    }

    ScheduledMovements createScheduledMovements(UUID id, ScheduledMovementsDetails details);
}
