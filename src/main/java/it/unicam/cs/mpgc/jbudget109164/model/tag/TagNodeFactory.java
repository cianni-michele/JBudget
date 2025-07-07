package it.unicam.cs.mpgc.jbudget109164.model.tag;

import java.util.Set;
import java.util.UUID;

public class TagNodeFactory implements TagFactory {

    @Override
    public Tag createTag(String name) {
        return new TagNode(UUID.randomUUID(), name);
    }

    @Override
    public Tag createTag(UUID id, String name, Set<Tag> children) {
        Tag tag = new TagNode(id, name);
        if (children != null) {
            for (Tag child : children) {
                tag.addChild(child);
            }
        }
        return tag;
    }
}
