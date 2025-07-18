package it.unicam.cs.mpgc.jbudget109164.dto.movement;

import it.unicam.cs.mpgc.jbudget109164.dto.tag.TagDTO;

import java.time.LocalDate;
import java.util.UUID;

public record MovementDTO(UUID id, String description, Double amount, LocalDate date, TagDTO[] tags) {

	private static final Builder BUILDER = new Builder();

	public static class Builder {
		private UUID id;
		private String description;
		private Double amount;
		private LocalDate date;
		private TagDTO[] tags;

		private Builder() {
		}

		public Builder copyFrom(MovementDTO movementFound) {
			this.id = movementFound.id;
			this.description = movementFound.description;
			this.amount = movementFound.amount;
			this.date = movementFound.date;
			this.tags = movementFound.tags;
			return this;
		}

		public Builder withId(UUID id) {
			this.id = id;
			return this;
		}

		public Builder withDescription(String description) {
			this.description = description;
			return this;
		}

		public Builder withAmount(Double amount) {
			this.amount = amount;
			return this;
		}

		public Builder withDate(LocalDate date) {
			this.date = date;
			return this;
		}

		public Builder withTags(TagDTO[] tags) {
			this.tags = tags;
			return this;
		}

		public MovementDTO build() {
			return new MovementDTO(id, description, amount, date, tags);
		}
    }

	public static Builder builder() {
		return BUILDER;
	}
}
