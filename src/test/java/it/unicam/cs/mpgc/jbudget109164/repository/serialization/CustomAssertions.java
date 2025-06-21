package it.unicam.cs.mpgc.jbudget109164.repository.serialization;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Assertions;

final class CustomAssertions {

    static class Tag {

        static void assertEquals(JsonElement expected, JsonElement actual) {
            JsonObject expectedObject = expected.getAsJsonObject();
            JsonObject actualObject = actual.getAsJsonObject();

            Assertions.assertEquals(
                    expectedObject.get("name").getAsString(),
                    actualObject.get("name").getAsString(),
                    "Tag names do not match"
            );


            JsonArray expectedChildrenJsonArray = expectedObject.getAsJsonArray("children");
            JsonArray actualChildrenJsonArray = actualObject.getAsJsonArray("children");

            Assertions.assertEquals(
                    expectedChildrenJsonArray.size(),
                    actualChildrenJsonArray.size(),
                    "Number of children does not match"
            );

            for (JsonElement actualJsonChild : actualChildrenJsonArray) {
                boolean foundMatch = false;
                for (JsonElement expectedJsonChild : expectedChildrenJsonArray) {
                    String actualChildName = actualJsonChild.getAsJsonObject().get("name").getAsString();
                    String expectedChildName = expectedJsonChild.getAsJsonObject().get("name").getAsString();

                    if (actualChildName.equals(expectedChildName)) {
                        JsonArray actualGrandchildren = actualJsonChild.getAsJsonObject().getAsJsonArray("children");
                        JsonArray expectedGrandchildren = expectedJsonChild.getAsJsonObject().getAsJsonArray("children");

                        if (!actualGrandchildren.isEmpty() || !expectedGrandchildren.isEmpty()) {
                            JsonObject actualChildObj = actualJsonChild.getAsJsonObject();
                            JsonObject expectedChildObj = expectedJsonChild.getAsJsonObject();

                            assertEquals(expectedChildObj, actualChildObj);
                        }

                        foundMatch = true;
                        break;
                    }
                }

                Assertions.assertTrue(
                        foundMatch,
                        "Expected child not found in actual children"
                );
            }
        }
    }

    static class Transaction {

        static void assertEquals(JsonElement expected, JsonElement actual) {
            JsonObject expectedObject = expected.getAsJsonObject();
            JsonObject actualObject = actual.getAsJsonObject();

            Assertions.assertEquals(
                    expectedObject.get("id").getAsString(),
                    actualObject.get("id").getAsString(),
                    "Transaction IDs do not match"
            );

            TransactionDetails.assertEquals(
                    expectedObject.get("details"),
                    actualObject.get("details")
            );

        }

        static class TransactionDetails {

            static void assertEquals(JsonElement expected, JsonElement actual) {
                JsonObject expectedObject = expected.getAsJsonObject();
                JsonObject actualObject = actual.getAsJsonObject();

                Assertions.assertEquals(
                        expectedObject.get("description").getAsString(),
                        actualObject.get("description").getAsString(),
                        "Transaction descriptions do not match"
                );

                Assertions.assertEquals(
                        expectedObject.get("amount").getAsDouble(),
                        actualObject.get("amount").getAsDouble(),
                        "Transaction amounts do not match"
                );

                Assertions.assertEquals(
                        expectedObject.get("date").getAsString(),
                        actualObject.get("date").getAsString(),
                        "Transaction dates do not match"
                );

                JsonArray expectedTags = expectedObject.getAsJsonArray("tags");
                JsonArray actualTags = actualObject.getAsJsonArray("tags");
                Assertions.assertEquals(
                        expectedTags.size(),
                        actualTags.size(),
                        "Number of tags does not match"
                );
            }
        }
    }

}
