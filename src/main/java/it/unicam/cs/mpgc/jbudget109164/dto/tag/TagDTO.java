package it.unicam.cs.mpgc.jbudget109164.dto.tag;

import java.util.UUID;

public record TagDTO(
        String id,
        String name,
        TagDTO[] children
) {

    public TagDTO(UUID id, String name, TagDTO[] children) {
        this(
                id != null ? id.toString() : null,
                name,
                children
        );
    }
}
