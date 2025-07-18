package it.unicam.cs.mpgc.jbudget109164.model.budget;

import it.unicam.cs.mpgc.jbudget109164.model.EntityDetails;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;

import java.time.temporal.Temporal;

public record BudgetDetails<P extends Temporal>(
        Tag tag,
        P period,
        double expectedAmount
) implements EntityDetails {
}
