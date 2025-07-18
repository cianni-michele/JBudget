package it.unicam.cs.mpgc.jbudget109164.model.scheduled;

import java.util.UUID;

public class ScheduledSimpleMovementsFactory implements ScheduledMovementsFactory {
    @Override
    public ScheduledMovements createScheduledMovements(UUID id, ScheduledMovementsDetails details) {
        return new ScheduledSimpleMovements(
                id,
                details.scheduledPeriod(),
                details.amount(),
                details.description(),
                details.dayOfMonth()
        );
    }
}
