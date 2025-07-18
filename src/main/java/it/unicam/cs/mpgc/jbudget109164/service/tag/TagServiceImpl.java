package it.unicam.cs.mpgc.jbudget109164.service.tag;

import it.unicam.cs.mpgc.jbudget109164.exception.service.TagNotFoundException;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;
import it.unicam.cs.mpgc.jbudget109164.model.tag.TagFactory;
import it.unicam.cs.mpgc.jbudget109164.repository.tag.TagRepository;
import it.unicam.cs.mpgc.jbudget109164.mapper.tag.TagMapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class TagServiceImpl implements TagService {

    private final TagRepository repository;

    private final TagFactory factory;

    private final TagMapper mapper;

    public TagServiceImpl(TagRepository repository,
                          TagFactory factory,
                          TagMapper mapper) {
        this.repository = repository;
        this.factory = factory;
        this.mapper = mapper;
    }

    @Override
    public List<Tag> getAllTags() {
        return repository.findAll().stream().map(mapper::toModel).toList();
    }

    @Override
    public Optional<Tag> getTagById(UUID id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    public Tag createTag(String name) {
        Tag tag = factory.createTag(name);

        repository.save(mapper.toDTO(tag));

        return tag;
    }

    @Override
    public Tag updateTag(UUID id, String name) {
        Tag tag = getTagById(id).orElseThrow(() -> new TagNotFoundException(id));

        Tag updatedTag = factory.createTag(tag.getId(), name, tag.getChildren());

        repository.save(mapper.toDTO(updatedTag));

        return updatedTag;
    }

    @Override
    public void deleteTag(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public Tag addChildTag(UUID parentId, String childName) {
        Tag parentTag = getTagById(parentId).orElseThrow(() -> new TagNotFoundException(parentId));
        Tag childTag = factory.createTag(childName);

        parentTag.addChild(childTag);

        repository.save(mapper.toDTO(parentTag));
        return childTag;
    }

    @Override
    public void removeChildTag(UUID parentId, UUID childId) {
        Tag parentTag = getTagById(parentId).orElseThrow(() -> new TagNotFoundException(parentId));
        Tag childTag = getTagById(childId).orElseThrow(() -> new TagNotFoundException(childId));

        parentTag.removeChild(childTag);

        repository.save(mapper.toDTO(parentTag));
        repository.deleteById(childId);
    }

    @Override
    public Set<Tag> getParentTags(UUID id) {
        return getTagById(id).map(Tag::getParents).orElseThrow(() -> new TagNotFoundException(id));
    }

    @Override
    public Set<Tag> getChildTags(UUID id) {
        return getTagById(id).map(Tag::getChildren).orElseThrow(() -> new TagNotFoundException(id));
    }
}
