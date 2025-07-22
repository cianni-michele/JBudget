package it.unicam.cs.mpgc.jbudget109164.controller.statistic;

import it.unicam.cs.mpgc.jbudget109164.exception.service.TagNotFoundException;
import it.unicam.cs.mpgc.jbudget109164.util.time.Period;
import it.unicam.cs.mpgc.jbudget109164.model.statistic.Statistics;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;
import it.unicam.cs.mpgc.jbudget109164.service.statistic.StatisticService;
import it.unicam.cs.mpgc.jbudget109164.service.tag.TagService;

import java.time.LocalDate;
import java.util.UUID;

public final class StatisticsController {

    private final StatisticService statisticService;
    private final TagService tagService;

    public StatisticsController(StatisticService statisticService,
                                TagService tagService) {
        this.statisticService = statisticService;
        this.tagService = tagService;
    }

    public Statistics getStatistics(UUID tagId, LocalDate startDate, LocalDate endDate) {
        if (tagId == null) {
            throw new IllegalArgumentException("Tag ID cannot be null");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }
        Tag tag = tagService.getTagById(tagId)
                .orElseThrow(() -> new TagNotFoundException("Tag with ID " + tagId + " not found"));

        return statisticService.getStatisticsByTagAndPeriod(tag, new Period(startDate, endDate));
    }

    public Statistics getStatistics(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }
        return statisticService.getStatisticsByPeriod(new Period(startDate, endDate));
    }
}
