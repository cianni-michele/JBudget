package it.unicam.cs.mpgc.jbudget109164.repository.tag;


public interface EventBus {

    void publish(TagDeletedEvent event);

    void subscribe(TagDeletedEventListener listener);
}
