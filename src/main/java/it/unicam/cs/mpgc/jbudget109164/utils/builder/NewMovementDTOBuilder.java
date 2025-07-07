package it.unicam.cs.mpgc.jbudget109164.utils.builder;

import it.unicam.cs.mpgc.jbudget109164.dto.NewMovementDTO;
import it.unicam.cs.mpgc.jbudget109164.dto.TagDTO;

import java.time.LocalDate;
import java.util.UUID;

public class NewMovementDTOBuilder {

    private static final NewMovementDTOBuilder instance = new NewMovementDTOBuilder();

    private UUID id;
    private String name;
    private double amount;
    private LocalDate date;
    private TagDTO[] tags;

    private NewMovementDTOBuilder() {
    }

    public static NewMovementDTOBuilder getInstance() {
        return instance;
    }

    public NewMovementDTOBuilder copyOf(NewMovementDTO movement) {
        this.id = movement.id();
        this.name = movement.name();
        this.amount = movement.amount();
        this.date = movement.date();
        this.tags = movement.tags();
        return this;
    }

    public NewMovementDTOBuilder setId(UUID id) {
        this.id = id;
        return this;
    }

    public NewMovementDTOBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public NewMovementDTOBuilder setAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public NewMovementDTOBuilder setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public NewMovementDTOBuilder setTags(TagDTO[] tags) {
        this.tags = tags;
        return this;
    }

    public NewMovementDTO build() {
        return new NewMovementDTO(id, name, amount, date, tags);
    }
}
