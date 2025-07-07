package it.unicam.cs.mpgc.jbudget109164;

import it.unicam.cs.mpgc.jbudget109164.dto.AccountDTO;
import it.unicam.cs.mpgc.jbudget109164.dto.TagDTO;
import it.unicam.cs.mpgc.jbudget109164.dto.MovementDTO;
import it.unicam.cs.mpgc.jbudget109164.dto.TransactionDTO;
import it.unicam.cs.mpgc.jbudget109164.utils.builder.AccountDTOBuilder;
import it.unicam.cs.mpgc.jbudget109164.utils.builder.MovementDTOBuilder;
import it.unicam.cs.mpgc.jbudget109164.utils.builder.TagDTOBuilder;
import it.unicam.cs.mpgc.jbudget109164.utils.builder.TransactionDTOBuilder;

import java.time.LocalDate;
import java.util.UUID;

public final class TestDTOs {
    public static TransactionDTO getTransaction() {
        return TransactionDTOBuilder.getInstance()
                .withId(UUID.randomUUID())
                .withDate(LocalDate.now())
                .withDescription("Test Transaction")
                .addMovement(getMovement())
                .addTag(getTag())
                .build();
    }

    public static MovementDTO getMovement() {
        return MovementDTOBuilder.getInstance()
                .withAmount(100.0)
                .withDescription("Test Movement")
                .withAccount(getAccount())
                .build();
    }

    public static AccountDTO getAccount() {
        return AccountDTOBuilder.getInstance()
                .withId(1L)
                .withType("ASSET")
                .withName("Test Account")
                .withDescription("Test Description")
                .withInitialBalance(1000.0)
                .build();
    }

    public static TagDTO getTag() {
        return TagDTOBuilder.getInstance()
                .withId(1L)
                .withName("Test Tag")
                .build();
    }
}
