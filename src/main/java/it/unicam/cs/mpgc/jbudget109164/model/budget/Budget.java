package it.unicam.cs.mpgc.jbudget109164.model.budget;

import java.time.temporal.Temporal;
import java.util.UUID;

import it.unicam.cs.mpgc.jbudget109164.model.Entity;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;

public interface Budget<P extends Temporal> extends Entity<UUID, BudgetDetails<P>> {

    Tag getTag();

    P getPeriod();

    double getExpectedAmount();
}
