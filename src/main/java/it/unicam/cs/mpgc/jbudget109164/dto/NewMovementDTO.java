package it.unicam.cs.mpgc.jbudget109164.dto;

import java.time.LocalDate;
import java.util.UUID;

public record NewMovementDTO(
        UUID id,
        String name,
        double amount,
        LocalDate date,
        TagDTO[] tags
) {
}
