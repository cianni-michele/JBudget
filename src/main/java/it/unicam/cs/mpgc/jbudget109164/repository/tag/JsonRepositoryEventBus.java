package it.unicam.cs.mpgc.jbudget109164.repository.tag;

import java.util.ArrayList;
import java.util.List;

/**
 * An event bus implementation for handling tag deletion events.
 * This class allows listeners to subscribe to tag deletion events and
 * notifies them when a tag is deleted.
 *
 * @author Michele Cianni
 */
public class JsonRepositoryEventBus implements EventBus {
    private final List<TagDeletedEventListener> listeners = new ArrayList<>();

    @Override
    public void publish(TagDeletedEvent event) {
        for (TagDeletedEventListener listener : listeners) {
            listener.onTagDeleted(event);
        }
    }

    @Override
    public void subscribe(TagDeletedEventListener listener) {
        listeners.add(listener);
    }
}
