package it.unicam.cs.mpgc.jbudget109164.repository.serialization;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class YearMonthTypeAdapter implements JsonDeserializer<YearMonth>, JsonSerializer<YearMonth> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    @Override
    public YearMonth deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return YearMonth.parse(json.getAsString(), FORMATTER);
    }

    @Override
    public JsonElement serialize(YearMonth src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.format(FORMATTER));
    }
}
