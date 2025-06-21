package it.unicam.cs.mpgc.jbudget109164.repository.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LocalDateTypeAdapterTest {

    private Gson gson;

    @BeforeEach
    void setUp() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .create();
    }

    @Test
    void deserialize() {
        String json = "\"2025-10-01\"";

        LocalDate actual = gson.fromJson(json, LocalDate.class);
        LocalDate expected = LocalDate.of(2025, 10, 1);

        assertEquals(expected, actual);
    }

    @Test
    void serialize() {
        LocalDate date = LocalDate.of(2025, 10, 1);

        String actual = gson.toJson(date, LocalDate.class);
        String expected = "\"2025-10-01\"";

        assertEquals(expected, actual);
    }
}