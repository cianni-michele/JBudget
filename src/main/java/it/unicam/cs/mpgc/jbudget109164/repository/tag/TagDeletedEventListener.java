package it.unicam.cs.mpgc.jbudget109164.repository.tag;

public interface TagDeletedEventListener {
    /**
     * Handles the event of a tag being deleted.
     *
     * @param event the event containing the ID of the deleted tag
     */
    void onTagDeleted(TagDeletedEvent event);
}
