package it.unicam.cs.mpgc.jbudget109164.controller;

import it.unicam.cs.mpgc.jbudget109164.config.DefaultJsonRepositoryConfig;
import it.unicam.cs.mpgc.jbudget109164.controller.budget.MonthlyBudgetController;
import it.unicam.cs.mpgc.jbudget109164.controller.movement.MovementController;
import it.unicam.cs.mpgc.jbudget109164.controller.statistic.StatisticsController;
import it.unicam.cs.mpgc.jbudget109164.controller.tag.TagController;
import it.unicam.cs.mpgc.jbudget109164.mapper.budget.MonthlyBudgetMapper;
import it.unicam.cs.mpgc.jbudget109164.model.budget.MonthlyBudgetFactory;
import it.unicam.cs.mpgc.jbudget109164.model.movement.SimpleMovementFactory;
import it.unicam.cs.mpgc.jbudget109164.model.statistic.DefaultStatisticsFactory;
import it.unicam.cs.mpgc.jbudget109164.model.tag.CategoryTagFactory;
import it.unicam.cs.mpgc.jbudget109164.repository.budget.JsonBudgetRepository;
import it.unicam.cs.mpgc.jbudget109164.repository.movement.JsonMovementRepository;
import it.unicam.cs.mpgc.jbudget109164.repository.tag.JsonRepositoryEventBus;
import it.unicam.cs.mpgc.jbudget109164.repository.tag.JsonTagRepository;
import it.unicam.cs.mpgc.jbudget109164.service.budget.MonthlyBudgetService;
import it.unicam.cs.mpgc.jbudget109164.service.movement.MovementServiceImpl;
import it.unicam.cs.mpgc.jbudget109164.service.statistic.StatisticServiceImpl;
import it.unicam.cs.mpgc.jbudget109164.service.tag.TagServiceImpl;
import it.unicam.cs.mpgc.jbudget109164.mapper.movement.SimpleMovementMapper;
import it.unicam.cs.mpgc.jbudget109164.mapper.tag.CategoryTagMapper;

public final class ControllerFactory {

    private final TagServiceImpl tagService;
    private final MovementServiceImpl movementService;
    private final StatisticServiceImpl statisticsService;
    private final MonthlyBudgetService budgetService;

    public ControllerFactory() {
        DefaultJsonRepositoryConfig config = new DefaultJsonRepositoryConfig();

        JsonRepositoryEventBus eventBus = new JsonRepositoryEventBus();
        JsonTagRepository tagRepository = new JsonTagRepository(config, eventBus);
        CategoryTagFactory tagFactory = new CategoryTagFactory();
        CategoryTagMapper tagMapper = new CategoryTagMapper();
        tagService = new TagServiceImpl(
                tagRepository,
                tagFactory,
                tagMapper
        );

        JsonMovementRepository movementRepository = new JsonMovementRepository(config, tagRepository);
        SimpleMovementFactory movementFactory = new SimpleMovementFactory();
        SimpleMovementMapper movementMapper = new SimpleMovementMapper(tagMapper);
        movementService = new MovementServiceImpl(
                movementRepository,
                movementFactory ,
                movementMapper
        );
        eventBus.subscribe(movementRepository);

        JsonBudgetRepository budgetRepository = new JsonBudgetRepository(config, tagRepository);
        MonthlyBudgetFactory budgetFactory = new MonthlyBudgetFactory();
        MonthlyBudgetMapper budgetMapper = new MonthlyBudgetMapper(tagMapper);
        budgetService = new MonthlyBudgetService(
                budgetRepository,
                budgetFactory,
                budgetMapper,
                movementService
        );
        eventBus.subscribe(budgetRepository);

        DefaultStatisticsFactory satisticsFactory = new DefaultStatisticsFactory();
        statisticsService = new StatisticServiceImpl(
                movementService,
                satisticsFactory
        );
    }


    public TagController getTagController() {
        return new TagController(tagService);
    }

    public MovementController getMovementController() {
        return new MovementController(movementService, tagService);
    }

    public StatisticsController getStatisticsController() {
        return new StatisticsController(statisticsService, tagService);
    }

    public MonthlyBudgetController getMonthlyBudgetController() {
        return new MonthlyBudgetController(budgetService, tagService);
    }
}
