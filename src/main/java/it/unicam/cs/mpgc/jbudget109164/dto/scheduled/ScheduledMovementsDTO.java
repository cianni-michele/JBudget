package it.unicam.cs.mpgc.jbudget109164.dto.scheduled;

import it.unicam.cs.mpgc.jbudget109164.dto.tag.TagDTO;

import java.time.LocalDate;
import java.util.UUID;

public record ScheduledMovementsDTO(
        UUID id,
        LocalDate fromDate,
        LocalDate toDate,
        String description,
        double amount,
        int dayOfMonth,
        TagDTO[] tags
) {

    private static final Builder BUILDER = new Builder();

    public static class Builder {
        private UUID id;
        private LocalDate fromDate;
        private LocalDate toDate;
        private String description;
        private Double amount;
        private Integer dayOfMonth;
        private TagDTO[] tags;

        private Builder() {
        }

        public Builder copyFrom(ScheduledMovementsDTO scheduledMovements) {
            this.id = scheduledMovements.id();
            this.fromDate = scheduledMovements.fromDate();
            this.toDate = scheduledMovements.toDate();
            this.description = scheduledMovements.description();
            this.amount = scheduledMovements.amount();
            this.tags = scheduledMovements.tags();
            return this;
        }

        public Builder withId(UUID id) {
            this.id = id;
            return this;
        }

        public Builder withFromDate(LocalDate fromDate) {
            this.fromDate = fromDate;
            return this;
        }

        public Builder withToDate(LocalDate toDate) {
            this.toDate = toDate;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withAmount(double amount) {
            this.amount = amount;
            return this;
        }

        public Builder withDayOfMonth(int dayOfMonth) {
            this.dayOfMonth = dayOfMonth;
            return this;
        }

        public Builder withTags(TagDTO[] tags) {
            this.tags = tags;
            return this;
        }

        public ScheduledMovementsDTO build() {
            return new ScheduledMovementsDTO(id, fromDate, toDate, description, amount, dayOfMonth, tags);
        }
    }

    public static Builder builder() {
        return BUILDER;
    }
}
