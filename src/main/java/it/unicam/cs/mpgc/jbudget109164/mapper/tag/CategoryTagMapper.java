package it.unicam.cs.mpgc.jbudget109164.mapper.tag;

import it.unicam.cs.mpgc.jbudget109164.dto.tag.TagDTO;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;
import it.unicam.cs.mpgc.jbudget109164.model.tag.CategoryTag;

public final class CategoryTagMapper extends TagMapper {

    @Override
    public Tag mapToModel(TagDTO dto) {
        Tag model = new CategoryTag(dto.id(), dto.name());

        if (dto.children() != null) {
            for (TagDTO child : dto.children()) {
                Tag childEntity = toModel(child);
                if (childEntity != null) {
                    model.addChild(childEntity);
                }
            }
        }

        return model;
    }

}
