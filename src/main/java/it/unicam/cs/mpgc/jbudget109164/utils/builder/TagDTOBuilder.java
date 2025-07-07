package it.unicam.cs.mpgc.jbudget109164.utils.builder;

import it.unicam.cs.mpgc.jbudget109164.dto.TagDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public final class TagDTOBuilder {
    
    private static final TagDTOBuilder INSTANCE = new TagDTOBuilder();
    
    private UUID id;
    private String name;
    private List<TagDTO> children;
    
    private TagDTOBuilder() {

    }
    
    public static TagDTOBuilder getInstance() {
        return INSTANCE;
    }
    
    public TagDTOBuilder copyOf(TagDTO tag) {
        id = tag.id();
        name = tag.name();
        children = tag.children() != null ? Arrays.asList(tag.children()) : null;
        return this;
    }
    
    public TagDTOBuilder withId(UUID id) {
        this.id = id;
        return this;
    }
    
    public TagDTOBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TagDTOBuilder withChildren(TagDTO[] children) {
        this.children = children != null ? Arrays.asList(children) : null;
        return this;
    }
    
    public TagDTOBuilder addChild(TagDTO child) {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        this.children.add(child);
        return this;
    }
    
    public TagDTO build() {
        TagDTO result = new TagDTO(
                id,
                name,
                children != null ? children.toArray(new TagDTO[0]) : null
        );
        reset();
        return result;
    }

    private void reset() {
        id = null;
        name = null;
        children = null;
    }

}
