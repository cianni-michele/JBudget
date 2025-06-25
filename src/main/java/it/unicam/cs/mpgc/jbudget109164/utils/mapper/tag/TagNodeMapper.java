package it.unicam.cs.mpgc.jbudget109164.utils.mapper.tag;

import it.unicam.cs.mpgc.jbudget109164.dto.tag.TagDTO;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;
import it.unicam.cs.mpgc.jbudget109164.model.tag.TagNode;

public final class TagNodeMapper extends TagMapper {

    @Override
    public Tag mapToEntity(TagDTO dto) {
        Tag entity = new TagNode(dto.id(), dto.name());

        if (dto.children() != null) {
            for (TagDTO child : dto.children()) {
                Tag childEntity = toEntity(child);
                if (childEntity != null) {
                    entity.addChild(childEntity);
                }
            }
        }

        return entity;
    }

}
