package it.unicam.cs.mpgc.jbudget109164.repository.tag;

/**
 * Interface for an event bus that allows publishing and subscribing to events related to tag deletion.
 * This interface is used to notify listeners when a tag is deleted.
 *
 * @author Michele Cianni
 */
public interface EventBus {

    /**
     * Publishes a TagDeletedEvent to notify subscribers that a tag has been deleted.
     *
     * @param event the event containing details about the deleted tag
     */
    void publish(TagDeletedEvent event);

    /**
     * Subscribes a listener to receive notifications when a tag is deleted.
     *
     * @param listener the listener to be notified of tag deletion events
     */
    void subscribe(TagDeletedEventListener listener);
}
