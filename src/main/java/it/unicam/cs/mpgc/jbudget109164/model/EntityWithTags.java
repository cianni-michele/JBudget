package it.unicam.cs.mpgc.jbudget109164.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;

public abstract class EntityWithTags<I, D extends EntityDetails> implements Entity<I, D>, Iterable<Tag> {
    
    protected final Set<Tag> tags;

    protected EntityWithTags(Set<Tag> tags) {
        this.tags = Objects.requireNonNullElse(tags, new HashSet<>());
    }

    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
    }

    public boolean hasTag(Tag tag) {
        return tags.contains(tag);
    }

    @Override
    public Iterator<Tag> iterator() {
        return tags.iterator();
    }

}
