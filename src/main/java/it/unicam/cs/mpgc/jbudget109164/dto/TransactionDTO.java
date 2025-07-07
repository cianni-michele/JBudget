package it.unicam.cs.mpgc.jbudget109164.dto;

import java.time.LocalDate;
import java.util.UUID;

public record TransactionDTO(
        UUID id,
        LocalDate date,
        String description,
        TagDTO[] tags,
        MovementDTO[] movements
        ) {}
