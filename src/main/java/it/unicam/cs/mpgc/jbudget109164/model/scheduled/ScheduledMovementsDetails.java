package it.unicam.cs.mpgc.jbudget109164.model.scheduled;

import it.unicam.cs.mpgc.jbudget109164.model.EntityDetails;

public record ScheduledMovementsDetails(Period scheduledPeriod, double amount, String description, int dayOfMonth)
        implements EntityDetails {
}
