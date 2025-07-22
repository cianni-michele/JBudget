package it.unicam.cs.mpgc.jbudget109164.repository.tag;

/**
 * Listener interface for handling events related to tag deletion.
 * Implementations of this interface can be registered to receive notifications
 * when a tag is deleted from the system.
 *
 * @author Michele Cianni
 */
public interface TagDeletedEventListener {

    /**
     * Handles the event of a tag being deleted.
     *
     * @param event the event containing the ID of the deleted tag
     */
    void onTagDeleted(TagDeletedEvent event);
}
