package it.unicam.cs.mpgc.jbudget109164.model.tag;

import java.util.Set;
import java.util.UUID;

public interface TagFactory {

    Tag createTag(String name);

    Tag createTag(UUID id, String name, Set<Tag> children);

}
