package it.unicam.cs.mpgc.jbudget109164.utils.builder;

import it.unicam.cs.mpgc.jbudget109164.dto.TagDTO;
import it.unicam.cs.mpgc.jbudget109164.dto.MovementDTO;
import it.unicam.cs.mpgc.jbudget109164.dto.TransactionDTO;

import java.time.LocalDate;
import java.util.*;

public final class TransactionDTOBuilder {

    private static final TransactionDTOBuilder INSTANCE = new TransactionDTOBuilder();

    private UUID id;
    private LocalDate date;
    private String description;
    private List<MovementDTO> movements;
    private List<TagDTO> tags;

    private TransactionDTOBuilder() {

    }

    public static TransactionDTOBuilder getInstance() {
        return INSTANCE;
    }

    public TransactionDTOBuilder copyOf(TransactionDTO transaction) {
        id = transaction.id();
        date = transaction.date();
        description = transaction.description();
        tags = transaction.tags() != null ? Arrays.asList(transaction.tags()) : null;
        movements = transaction.movements() != null ? Arrays.asList(transaction.movements()) : null;
        return this;
    }

    public TransactionDTOBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public TransactionDTOBuilder withDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public TransactionDTOBuilder withDescription(String description) {
        this.description = description;
        return this;
    }


    public TransactionDTOBuilder withTags(TagDTO[] tags) {
        this.tags = tags != null ? Arrays.asList(tags) : null;
        return this;
    }

    public TransactionDTOBuilder addTag(TagDTO tag) {
        if (tags == null) {
            tags = new ArrayList<>();
        }
        tags.add(tag);
        return this;
    }

    public TransactionDTOBuilder withMovements(MovementDTO[] movements) {
        this.movements = movements != null ? Arrays.asList(movements) : null;
        return this;
    }

    public TransactionDTOBuilder addMovement(MovementDTO movement) {
        if (movements == null) {
            movements = new ArrayList<>();
        }
        movements.add(movement);
        return this;
    }

    public TransactionDTO build() {
        TransactionDTO result = new TransactionDTO(
                id,
                date,
                description,
                tags != null ? tags.toArray(new TagDTO[0]) : null,
                movements != null ? movements.toArray(new MovementDTO[0]) : null
        );
        reset();
        return result;
    }

    private void reset() {
        id = null;
        date = null;
        description = null;
        tags = null;
        movements = null;
    }
}
