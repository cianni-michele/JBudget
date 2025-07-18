package it.unicam.cs.mpgc.jbudget109164;

import it.unicam.cs.mpgc.jbudget109164.config.DefaultJsonRepositoryConfig;
import it.unicam.cs.mpgc.jbudget109164.config.JsonRepositoryConfig;
import it.unicam.cs.mpgc.jbudget109164.controller.movement.MovementController;
import it.unicam.cs.mpgc.jbudget109164.controller.tag.TagController;
import it.unicam.cs.mpgc.jbudget109164.model.movement.MovementFactory;
import it.unicam.cs.mpgc.jbudget109164.model.movement.SimpleMovementFactory;
import it.unicam.cs.mpgc.jbudget109164.model.tag.TagFactory;
import it.unicam.cs.mpgc.jbudget109164.model.tag.CategoryTagFactory;
import it.unicam.cs.mpgc.jbudget109164.repository.movement.JsonMovementRepository;
import it.unicam.cs.mpgc.jbudget109164.repository.movement.MovementRepository;
import it.unicam.cs.mpgc.jbudget109164.repository.tag.JsonTagRepository;
import it.unicam.cs.mpgc.jbudget109164.repository.tag.TagRepository;
import it.unicam.cs.mpgc.jbudget109164.service.movement.MovementService;
import it.unicam.cs.mpgc.jbudget109164.service.movement.MovementServiceImpl;
import it.unicam.cs.mpgc.jbudget109164.service.tag.TagService;
import it.unicam.cs.mpgc.jbudget109164.service.tag.TagServiceImpl;
import it.unicam.cs.mpgc.jbudget109164.mapper.movement.MovementMapper;
import it.unicam.cs.mpgc.jbudget109164.mapper.movement.SimpleMovementMapper;
import it.unicam.cs.mpgc.jbudget109164.mapper.tag.TagMapper;
import it.unicam.cs.mpgc.jbudget109164.mapper.tag.CategoryTagMapper;

public final class ControllerFactory {

    private final TagService tagService;
    private final MovementService movementService;

    public ControllerFactory() {
        JsonRepositoryConfig config = new DefaultJsonRepositoryConfig();

        TagRepository tagRepository = new JsonTagRepository(config);
        TagFactory tagFactory = new CategoryTagFactory();
        TagMapper tagMapper = new CategoryTagMapper();
        tagService = new TagServiceImpl(tagRepository, tagFactory, tagMapper);

        MovementRepository movementRepository = new JsonMovementRepository(config, tagRepository);
        MovementFactory movementFactory = new SimpleMovementFactory();
        MovementMapper movementMapper = new SimpleMovementMapper(tagMapper);
        movementService = new MovementServiceImpl(movementRepository, movementFactory ,movementMapper);
    }


    public TagController getTagController() {
        return new TagController(tagService);
    }

    public MovementController getMovementController() {
        return new MovementController(movementService, tagService);
    }

}
