package it.unicam.cs.mpgc.jbudget109164.repository.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.*;

class YearMonthSerializerTest {

    private Gson gson;

    @BeforeEach
    void setUp() {
        gson = new GsonBuilder()
                .registerTypeAdapter(YearMonth.class, new YearMonthSerializer())
                .create();
    }

    @Test
    void deserialize() {
        String json = "\"2025-10\"";

        YearMonth actual = gson.fromJson(json, YearMonth.class);
        YearMonth expected = YearMonth.of(2025, 10);

        assertEquals(expected, actual);
    }

    @Test
    void serialize() {
        YearMonth yearMonth = YearMonth.of(2025, 10);

        String actual = gson.toJson(yearMonth, YearMonth.class);
        String expected = "\"2025-10\"";

        assertEquals(expected, actual);
    }
}