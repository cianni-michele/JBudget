package it.unicam.cs.mpgc.jbudget109164.dto.budget;

import it.unicam.cs.mpgc.jbudget109164.dto.tag.TagDTO;

import java.util.UUID;

public record BudgetDTO(UUID id, TagDTO tag, String period, double expectedAmount) {

	private static final Builder BUILDER = new Builder();

	public static class Builder {
		private UUID id;
		private TagDTO tag;
		private String period;
		private double expectedAmount;

		private Builder() {
		}

		public Builder copyFrom(BudgetDTO budget) {
			this.tag = budget.tag;
			this.period = budget.period;
			this.expectedAmount = budget.expectedAmount;
			return this;
		}

		public Builder withId(UUID id) {
			this.id = id;
			return this;
		}

		public Builder withTag(TagDTO tag) {
			this.tag = tag;
			return this;
		}

		public Builder withPeriod(String period) {
			this.period = period;
			return this;
		}

		public Builder withExpectedAmount(double expectedAmount) {
			this.expectedAmount = expectedAmount;
			return this;
		}

		public BudgetDTO build() {
			return new BudgetDTO(id, tag, period, expectedAmount);
		}
	}

	public static Builder builder() {
		return BUILDER;
	}

}
