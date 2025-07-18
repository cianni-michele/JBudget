package it.unicam.cs.mpgc.jbudget109164.model.scheduled;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import it.unicam.cs.mpgc.jbudget109164.model.EntityWithTags;
import it.unicam.cs.mpgc.jbudget109164.model.movement.Movement;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;

public abstract class ScheduledMovements extends EntityWithTags<UUID, ScheduledMovementsDetails> {
    
    protected final UUID id;

    protected final Period scheduledPeriod;

    protected final double amount;

    protected final String description;

    protected final int dayOfMonth;
    

    protected ScheduledMovements(UUID id, Period scheduledPeriod, double amount, String description, int dayOfMonth) {
        this(id, scheduledPeriod, amount, description, dayOfMonth, new HashSet<>());
    }

    protected ScheduledMovements(UUID id, Period scheduledPeriod, double amount, String description, int dayOfMonth, Set<Tag> tags) {
        super(tags);
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.scheduledPeriod = Objects.requireNonNull(scheduledPeriod, "Period cannot be null");
        this.amount = amount;
        this.description = Objects.requireNonNullElse(description, "");
        this.dayOfMonth = Math.max(1, Math.min(dayOfMonth, 28));
    }

    public Period getScheduledPeriod() {
        return scheduledPeriod;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public abstract List<Movement> getMovements(Period period);
    
    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public ScheduledMovementsDetails getDetails() {
        return new ScheduledMovementsDetails(scheduledPeriod, amount, description, dayOfMonth);
    }
    
 
}
