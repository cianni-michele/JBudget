package it.unicam.cs.mpgc.jbudget109164.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;

public abstract class EntityWithTags<D extends EntityDetails> implements Entity<D>, Iterable<Tag> {
    
    protected final Set<Tag> tags;

    protected EntityWithTags(Set<Tag> tags) {
        this.tags = Objects.requireNonNullElse(tags, new HashSet<>());
    }

    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public void addTags(Set<Tag> tags) {
        if (tags == null) {
            throw new IllegalArgumentException("Tags cannot be null");
        }
        this.tags.addAll(tags);
    }

    public void addTag(Tag tag) {
        if (tag == null) {
            throw new IllegalArgumentException("Tag cannot be null");
        }
        tags.add(tag);
    }

    public void removeTag(Tag tag) {
        if (tag == null) {
            throw new IllegalArgumentException("Tag cannot be null");
        }
        tags.remove(tag);
    }

    public boolean isTaggedBy(Tag tag) {
        if (tag == null) {
            throw new IllegalArgumentException("Tag cannot be null");
        }
        return tags.contains(tag);
    }

    @Override
    public Iterator<Tag> iterator() {
        return tags.iterator();
    }

}
