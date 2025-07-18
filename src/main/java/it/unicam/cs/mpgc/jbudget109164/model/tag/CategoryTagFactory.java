package it.unicam.cs.mpgc.jbudget109164.model.tag;

import java.util.Set;
import java.util.UUID;

public class CategoryTagFactory implements TagFactory {

    @Override
    public Tag createTag(String name) {
        return new CategoryTag(UUID.randomUUID(), name);
    }

    @Override
    public Tag createTag(UUID id, String name, Set<Tag> children) {
        Tag tag = new CategoryTag(id, name);
        if (children != null) {
            for (Tag child : children) {
                tag.addChild(child);
            }
        }
        return tag;
    }
}
