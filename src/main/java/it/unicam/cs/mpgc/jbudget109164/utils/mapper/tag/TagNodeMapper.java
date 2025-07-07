package it.unicam.cs.mpgc.jbudget109164.utils.mapper.tag;

import it.unicam.cs.mpgc.jbudget109164.dto.TagDTO;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;
import it.unicam.cs.mpgc.jbudget109164.model.tag.TagNode;

public final class TagNodeMapper extends TagMapper {

    @Override
    public Tag mapToModel(TagDTO dto) {
        Tag model = new TagNode(dto.id(), dto.name());

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
