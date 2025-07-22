package it.unicam.cs.mpgc.jbudget109164.model.budget;

import java.time.temporal.Temporal;

import it.unicam.cs.mpgc.jbudget109164.model.Entity;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;

public interface Budget<P extends Temporal> extends Entity<BudgetDetails<P>> {

    Tag getTag();

    P getPeriod();

    double getExpectedAmount();

}
