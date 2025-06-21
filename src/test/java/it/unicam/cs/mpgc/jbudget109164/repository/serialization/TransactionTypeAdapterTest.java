package it.unicam.cs.mpgc.jbudget109164.repository.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import it.unicam.cs.mpgc.jbudget109164.model.CategoryTagFactory;
import it.unicam.cs.mpgc.jbudget109164.model.Tag;
import it.unicam.cs.mpgc.jbudget109164.model.TagFactory;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTypeAdapterTest {

    private final TransactionFactory transactionFactory = new SimpleTransactionFactory();
    private final TagFactory tagFactory = new CategoryTagFactory();

    private Gson gson;


    @BeforeEach
    void setUp() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .registerTypeAdapter(Tag.class, new TagTypeAdapter(tagFactory))
                .registerTypeAdapter(Transaction.class, new TransactionTypeAdapter(transactionFactory))
                .create();
    }

    @Nested
    class DeserializationTests {

        @Test
        void shouldDeserializeTransactionWithoutTags() {
            String json = """
                    {
                        "id": "00000000-0000-0000-0000-000000000000",
                        "details": {
                            "description": "Test Transaction",
                            "amount": 100.0,
                            "date": "2025-10-01",
                            "tags": []
                        }
                    }
                    """;

            Transaction actual = gson.fromJson(json, Transaction.class);
            Transaction expected = new SimpleTransaction(
                    UUID.fromString("00000000-0000-0000-0000-000000000000"),
                    new TransactionDetails(
                            "Test Transaction",
                            100.0,
                            LocalDate.of(2025, 10, 1),
                            Set.of()
                    )
            );

            assertEquals(expected, actual);
        }

        @Test
        void shouldDeserializeTransactionWithSingleTag() {
            String json = """
                    {
                        "id": "00000000-0000-0000-0000-000000000001",
                        "details": {
                            "description": "Test Transaction with Tag",
                            "amount": 200.0,
                            "date": "2025-10-02",
                            "tags": [
                                {
                                    "name": "Test Tag",
                                    "children": []
                                }
                            ]
                        }
                    }
                    """;

            Tag tag = tagFactory.createTag("Test Tag");
            Transaction actual = gson.fromJson(json, Transaction.class);
            Transaction expected = new SimpleTransaction(
                    UUID.fromString("00000000-0000-0000-0000-000000000001"),
                    new TransactionDetails(
                            "Test Transaction with Tag",
                            200.0,
                            LocalDate.of(2025, 10, 2),
                            Set.of(tag)
                    )
            );

            assertEquals(expected, actual);
        }

        @Test
        void shouldDeserializeTransactionWithMultipleTags() {
            String json = """
                    {
                        "id": "00000000-0000-0000-0000-000000000002",
                        "details": {
                            "description": "Test Transaction with Multiple Tags",
                            "amount": 300.0,
                            "date": "2025-10-03",
                            "tags": [
                                {
                                    "name": "Tag One",
                                    "children": []
                                },
                                {
                                    "name": "Tag Two",
                                    "children": []
                                }
                            ]
                        }
                    }
                    """;

            Tag tagOne = tagFactory.createTag("Tag One");
            Tag tagTwo = tagFactory.createTag("Tag Two");
            Transaction actual = gson.fromJson(json, Transaction.class);
            Transaction expected = new SimpleTransaction(
                    UUID.fromString("00000000-0000-0000-0000-000000000002"),
                    new TransactionDetails(
                            "Test Transaction with Multiple Tags",
                            300.0,
                            LocalDate.of(2025, 10, 3),
                            Set.of(tagOne, tagTwo)
                    )
            );

            assertEquals(expected, actual);
        }
    }

    @Nested
    class SerializationTests {
        @Test
        void shouldSerializeTransactionWithoutTags() {
            Transaction transaction = new SimpleTransaction(
                    UUID.fromString("00000000-0000-0000-0000-000000000004"),
                    new TransactionDetails(
                            "Transaction without Tags",
                            500.0,
                            LocalDate.of(2025, 10, 5),
                            Set.of()
                    )
            );

            JsonElement actual = gson.toJsonTree(transaction);
            JsonElement expected = gson.fromJson("""
                    {
                        "id": "00000000-0000-0000-0000-000000000004",
                        "details": {
                            "description": "Transaction without Tags",
                            "amount": 500.0,
                            "date": "2025-10-05",
                            "tags": []
                        }
                    }
                    """, JsonElement.class);

            CustomAssertions.Transaction.assertEquals(expected, actual);
        }

        @Test
        void shouldSerializeTransactionWithSingleTag() {
            Tag tag = tagFactory.createTag("Single Tag");
            Transaction transaction = new SimpleTransaction(
                    UUID.fromString("00000000-0000-0000-0000-000000000005"),
                    new TransactionDetails(
                            "Transaction with Single Tag",
                            600.0,
                            LocalDate.of(2025, 10, 6),
                            Set.of(tag)
                    )
            );

            JsonElement actual = gson.toJsonTree(transaction);
            JsonElement expected = gson.fromJson("""
                    {
                        "id": "00000000-0000-0000-0000-000000000005",
                        "details": {
                            "description": "Transaction with Single Tag",
                            "amount": 600.0,
                            "date": "2025-10-06",
                            "tags": [
                                {
                                    "name": "Single Tag",
                                    "children": []
                                }
                            ]
                        }
                    }
                    """, JsonElement.class);

            CustomAssertions.Transaction.assertEquals(expected, actual);
        }

        @Test
        void shouldSerializeTransactionWithMultipleTags() {
            Tag tagOne = tagFactory.createTag("Tag One");
            Tag tagTwo = tagFactory.createTag("Tag Two");
            Transaction transaction = new SimpleTransaction(
                    UUID.fromString("00000000-0000-0000-0000-000000000006"),
                    new TransactionDetails(
                            "Transaction with Multiple Tags",
                            700.0,
                            LocalDate.of(2025, 10, 7),
                            Set.of(tagOne, tagTwo)
                    )
            );

            JsonElement actual = gson.toJsonTree(transaction);
            JsonElement expected = gson.fromJson("""
                    {
                        "id": "00000000-0000-0000-0000-000000000006",
                        "details": {
                            "description": "Transaction with Multiple Tags",
                            "amount": 700.0,
                            "date": "2025-10-07",
                            "tags": [
                                {
                                    "name": "Tag One",
                                    "children": []
                                },
                                {
                                    "name": "Tag Two",
                                    "children": []
                                }
                            ]
                        }
                    }
                    """, JsonElement.class);

            CustomAssertions.Transaction.assertEquals(expected, actual);
        }
    }
}