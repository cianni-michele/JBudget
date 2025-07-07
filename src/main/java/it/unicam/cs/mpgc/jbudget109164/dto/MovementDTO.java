package it.unicam.cs.mpgc.jbudget109164.dto;

public record MovementDTO(
        Double amount,
        String description,
        AccountDTO account
) {}
