package it.unicam.cs.mpgc.jbudget109164.model;

public class CategoryTagFactory implements TagFactory {
    @Override
    public Tag createTag(String name, Tag... children) {
        Tag tag = new CategoryTag(name);
        for (Tag child : children) {
            tag.addChild(child);
        }
        return tag;
    }
}
