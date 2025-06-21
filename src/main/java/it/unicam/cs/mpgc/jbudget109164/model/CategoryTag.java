package it.unicam.cs.mpgc.jbudget109164.model;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

/**
 * An instance of this class represents a category tag, which can have a parent tag and child tags,
 * and is used to categorize other tags or transactions.
 * <p>
 * This class implements the {@link Tag} interface.
 *
 * @author Michele Cianni
 * @see Tag
 */
public class CategoryTag implements Tag {

    private final Tag parent;
    private final String name;
    private final Set<Tag> children;

    /**
     * Creates a new SimpleTag with the specified name.
     *
     * @param name the name of the tag, must not be null or empty
     * @throws NullPointerException     if name is null
     * @throws IllegalArgumentException if name is empty
     */
    public CategoryTag(String name) {
        this(null, name);
    }

    /**
     * Creates a new SimpleTag with the specified name and parent.
     *
     * @param parent the parent tag, can be null if there is no parent
     * @param name   the name of the tag, must not be null or empty
     * @throws NullPointerException     if name is null
     * @throws IllegalArgumentException if name is empty
     */
    protected CategoryTag(Tag parent, String name) {
        this(parent, name, new HashSet<>());
    }

    /**
     * Creates a new SimpleTag with the specified name, parent, and children.
     *
     * @param parent   the parent tag, can be null if there is no parent
     * @param name     the name of the tag, must not be null or empty
     * @param children the child tags, must not be null
     * @throws NullPointerException     if name or children are null
     * @throws IllegalArgumentException if name is empty
     */
    protected CategoryTag(Tag parent, String name, Set<Tag> children) {
        this.parent = parent;
        this.name = validateAndGetName(name);
        this.children = validateAndGetChildren(children);
    }

    private String validateAndGetName(String name) {
        Objects.requireNonNull(name, "Name cannot be null");
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        return name;
    }

    private Set<Tag> validateAndGetChildren(Set<Tag> children) {
        return Objects.requireNonNull(children, "Children cannot be null");
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Optional<Tag> parent() {
        return Optional.ofNullable(parent);
    }

    @Override
    public Set<Tag> children() {
        return children;
    }

    @Override
    public boolean hasChildren() {
        return !children.isEmpty();
    }

    @Override
    public Stream<Tag> stream() {
        return children.stream();
    }

    @Override
    public void addChild(Tag tag) {
        Objects.requireNonNull(tag, "Child tag cannot be null");

        if (tag.containsChild(this)) {
            throw new IllegalArgumentException("Cannot add a tag as its own child");
        }

        Tag child = new CategoryTag(this, tag.name(), tag.children());

        this.children.add(child);
    }


    @Override
    public boolean containsChild(Set<Tag> tags) {
        if (tags == null || tags.isEmpty()) {
            return false;
        }
        Map<Tag, Boolean> tagsFound = tags.stream().collect(toMap(tag -> tag, tag -> false));

        traverseChildren(tag -> {
            if (tagsFound.containsKey(tag)) {
                tagsFound.put(tag, true);
            }
        });

        return tagsFound.values().stream().allMatch(found -> found);
    }

    /**
     * Traverses all child tags and applies the given action to each tag.
     *
     * @param action the action to apply to each tag
     */
    protected void traverseChildren(Consumer<Tag> action) {
        Queue<Tag> queue = new LinkedList<>(this.children);
        Set<Tag> visited = new HashSet<>();

        while (!queue.isEmpty()) {
            Tag current = queue.poll();
            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);

            action.accept(current);

            if (current.hasChildren()) {
                queue.addAll(current.stream().collect(toSet()));
            }
        }
    }


    @Override
    public Iterator<Tag> iterator() {
        return this.children.iterator();
    }

    @Override
    public void forEach(Consumer<? super Tag> action) {
        this.children.forEach(action);
    }

    @Override
    public Spliterator<Tag> spliterator() {
        return this.children.spliterator();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CategoryTag categoryTag = (CategoryTag) o;
        return Objects.equals(name(), categoryTag.name());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name());
    }

    @Override
    public String toString() {
        return "SimpleTag{" +
               "name='" + name + '\'' +
               '}';
    }
}
