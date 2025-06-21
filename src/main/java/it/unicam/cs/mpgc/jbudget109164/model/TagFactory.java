package it.unicam.cs.mpgc.jbudget109164.model;

@FunctionalInterface
public interface TagFactory {

    /**
     * Creates a new root tag with the specified name and child tags.
     *
     * @param name     the name of the root tag
     * @param children the child tags of this root tag, can be empty if this tag has no children
     * @return a new Tag object with the specified parameters
     */
    Tag createTag(String name, Tag... children);


}
