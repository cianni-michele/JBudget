package it.unicam.cs.mpgc.jbudget109164.repository.serialization;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

/**
 * A custom serializer interface that combines both serialization and deserialization capabilities.
 * It extends the {@link JsonSerializer} and {@link JsonDeserializer} interfaces from Gson.
 *
 * @param <T> the type of objects this serializer handles
 */
public interface CustomSerializer<T> extends JsonSerializer<T>, JsonDeserializer<T> {
}
