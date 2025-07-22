package it.unicam.cs.mpgc.jbudget109164.repository.tag;

import java.util.ArrayList;
import java.util.List;

public final class JsonRepositoryEventBus implements EventBus {
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
