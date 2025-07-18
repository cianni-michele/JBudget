package it.unicam.cs.mpgc.jbudget109164.model.movement;

import it.unicam.cs.mpgc.jbudget109164.model.EntityDetails;
import java.time.LocalDate;

public record MovementDetails(LocalDate date, String description, double amount) implements EntityDetails {
}
