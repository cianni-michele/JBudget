package it.unicam.cs.mpgc.jbudget109164.dto;

import java.util.UUID;

public record TagDTO(
        UUID id,
        String name,
        TagDTO[] children
) {}
