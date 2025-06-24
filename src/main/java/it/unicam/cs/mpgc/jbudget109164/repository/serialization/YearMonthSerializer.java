package it.unicam.cs.mpgc.jbudget109164.repository.serialization;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

/**
 * This class is responsible for serializing and deserializing
 * {@link YearMonth} objects to and from JSON format.
 * <p>
 * It implements the {@link CustomSerializer} interface.
 *
 * @author Michele Cianni
 */
public class YearMonthSerializer implements CustomSerializer<YearMonth> {

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
