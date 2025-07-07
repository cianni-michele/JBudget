package it.unicam.cs.mpgc.jbudget109164.utils.mapper.tag;

import it.unicam.cs.mpgc.jbudget109164.dto.TagDTO;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;
import it.unicam.cs.mpgc.jbudget109164.utils.mapper.TemplateMapper;

public abstract class TagMapper extends TemplateMapper<Tag, TagDTO> {

    @Override
    public TagDTO mapToDTO(Tag model) {
        return new TagDTO(
                model.getId(),
                model.getName(),
                model.getChildren().stream().map(this::toDTO).toArray(TagDTO[]::new)
        );
    }

}
