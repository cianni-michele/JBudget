package it.unicam.cs.mpgc.jbudget109164.mapper.tag;

import it.unicam.cs.mpgc.jbudget109164.dto.tag.TagDTO;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;
import it.unicam.cs.mpgc.jbudget109164.mapper.AbstractMapper;

public abstract class TagMapper extends AbstractMapper<Tag, TagDTO> {

    @Override
    public TagDTO mapToDTO(Tag model) {
        return new TagDTO(
                model.getId(),
                model.getName(),
                model.getChildren().stream().map(this::toDTO).toArray(TagDTO[]::new)
        );
    }

}
