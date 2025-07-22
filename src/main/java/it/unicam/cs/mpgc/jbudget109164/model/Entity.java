package it.unicam.cs.mpgc.jbudget109164.model;

import java.util.UUID;

/**
 * Interface representing an entity with a unique identifier and details.
 *
 * @param <D> the type of details associated with the entity, which must extend {@link EntityDetails}.
 * @author Michele Cianni
 */
public interface Entity<D extends EntityDetails> {

    /**
     * Returns the unique identifier of this entity.
     *
     * @return the UUID of this entity.
     */
    UUID getId();

    /**
     * Returns the details associated with this entity.
     *
     * @return the details of this entity.
     */
    D getDetails();

}
