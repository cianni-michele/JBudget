package it.unicam.cs.mpgc.jbudget109164.model.scheduled;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import it.unicam.cs.mpgc.jbudget109164.model.movement.Movement;
import it.unicam.cs.mpgc.jbudget109164.model.movement.SimpleMovement;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;

public class ScheduledSimpleMovements extends ScheduledMovements {


    public ScheduledSimpleMovements(UUID id, Period scheduledPeriod, double amount, String description, int dayOfMonth) {
        this(id, scheduledPeriod, amount, description, dayOfMonth, new HashSet<>());
    }

    public ScheduledSimpleMovements(UUID id, Period scheduledPeriod, double amount, String description, int dayOfMonth, Set<Tag> tags) {
        super(id, scheduledPeriod, amount, description, dayOfMonth, tags);
    }

    @Override
    public List<Movement> getMovements(Period period) {
        List<Movement> result = new ArrayList<>();

        LocalDate currentDate = period.from().withDayOfMonth(dayOfMonth);

        while (!currentDate.isAfter(period.to())) {
            if (!currentDate.isBefore(scheduledPeriod.from()) && !currentDate.isAfter(scheduledPeriod.to())) {
                Movement movement = new SimpleMovement(UUID.randomUUID(), currentDate, description, amount, tags);
                result.add(movement);
            }
            currentDate = currentDate.plusMonths(1);
        }

        return result;
    }
    
}
