package it.unicam.cs.mpgc.jbudget109164.service.scheduled;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import it.unicam.cs.mpgc.jbudget109164.model.movement.Movement;
import it.unicam.cs.mpgc.jbudget109164.model.scheduled.Period;
import it.unicam.cs.mpgc.jbudget109164.model.scheduled.ScheduledMovements;
import it.unicam.cs.mpgc.jbudget109164.model.scheduled.ScheduledMovementsDetails;

public interface ScheduledMovementsService {

    List<ScheduledMovements> getAllScheduledMovements();

    Optional<ScheduledMovements> getScheduledMovements(UUID id);

    ScheduledMovements createScheduledMovements(ScheduledMovementsDetails scheduledMovementsDetails);

    ScheduledMovements updateScheduledMovements(UUID id, ScheduledMovementsDetails scheduledMovementsDetails);

    boolean deleteScheduledMovements(UUID id);

    List<Movement> getAllScheduledMovements(Period scheduledPeriod);
}
