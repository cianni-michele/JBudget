package it.unicam.cs.mpgc.jbudget109164.repository.tag;

import java.util.UUID;

@FunctionalInterface
public interface TagDeletedEvent {
    UUID deletedTagId();
}
