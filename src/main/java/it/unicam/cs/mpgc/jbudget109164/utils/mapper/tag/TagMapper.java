package it.unicam.cs.mpgc.jbudget109164.utils.mapper.tag;

import it.unicam.cs.mpgc.jbudget109164.dto.tag.TagDTO;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;
import it.unicam.cs.mpgc.jbudget109164.utils.mapper.TemplateMapper;

public abstract class TagMapper extends TemplateMapper<Tag, TagDTO> {

    @Override
    public TagDTO mapToDTO(Tag entity) {
        return new TagDTO(
                entity.getId(),
                entity.getName(),
                entity.getChildren().stream().map(this::toDTO).toArray(TagDTO[]::new)
        );
    }

}
