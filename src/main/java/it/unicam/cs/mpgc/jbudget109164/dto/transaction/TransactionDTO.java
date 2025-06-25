package it.unicam.cs.mpgc.jbudget109164.dto.transaction;

import it.unicam.cs.mpgc.jbudget109164.dto.tag.TagDTO;

import java.time.LocalDate;
import java.util.UUID;

public record TransactionDTO(
        String id,
        String date,
        String description,
        double amount,
        TagDTO[] tags
) {
    public TransactionDTO(UUID id, LocalDate date, double amount, String description, TagDTO[] tags) {
        this(
                id != null ? id.toString() : null,
                date != null ? date.toString() : null,
                description,
                amount,
                tags
        );
    }
}
