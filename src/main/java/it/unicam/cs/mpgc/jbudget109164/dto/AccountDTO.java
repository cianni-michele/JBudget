package it.unicam.cs.mpgc.jbudget109164.dto;

import java.util.UUID;

public record AccountDTO(
        UUID id,
        String type,
        String name,
        String description,
        Double initialBalance
) {}
