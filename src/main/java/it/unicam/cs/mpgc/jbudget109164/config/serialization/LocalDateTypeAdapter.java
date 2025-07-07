package it.unicam.cs.mpgc.jbudget109164.config.serialization;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;

/**
 * This class is responsible for serializing and deserializing
 * {@link LocalDate} objects to and from JSON format.
 * <p>
 * It implements the {@link CustomTypeAdapter} interface.
 *
 * @author Michele Cianni
 */
public class LocalDateTypeAdapter implements CustomTypeAdapter<LocalDate> {

    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return LocalDate.parse(json.getAsString());
    }

    @Override
    public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }
}
