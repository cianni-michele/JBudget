package it.unicam.cs.mpgc.jbudget109164.dto.tag;

import java.util.UUID;

public record TagDTO(
        UUID id,
        String name,
        TagDTO[] children) {
    private static final Builder BUILDER = new Builder();

    public static class Builder {
        private UUID id;
        private String name;
        private TagDTO[] children;

        private Builder() {
        }

        public Builder copyFrom(TagDTO tagDTO) {
			this.id = tagDTO.id;
			this.name = tagDTO.name;
			this.children = tagDTO.children;
			return this;
		}

        public Builder withId(UUID id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withChildren(TagDTO[] children) {
            this.children = children;
            return this;
        }

        public TagDTO build() {
            return new TagDTO(id, name, children);
        }
    }

    public static Builder builder() {
        return BUILDER;
    }
}
