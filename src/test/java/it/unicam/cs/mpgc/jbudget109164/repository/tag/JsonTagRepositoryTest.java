package it.unicam.cs.mpgc.jbudget109164.repository.tag;

import it.unicam.cs.mpgc.jbudget109164.TestDTOs;
import it.unicam.cs.mpgc.jbudget109164.config.DefaultJsonRepositoryConfig;
import it.unicam.cs.mpgc.jbudget109164.config.JsonRepositoryConfig;
import it.unicam.cs.mpgc.jbudget109164.dto.TagDTO;
import it.unicam.cs.mpgc.jbudget109164.exception.repository.JsonRepositoryException;
import it.unicam.cs.mpgc.jbudget109164.utils.builder.TagDTOBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class JsonTagRepositoryTest {
    private static final String DIRECTORY_PATH = "src/test/resources/tags";

    private JsonTagRepository underTest;

    private TagDTO testTag;

    @BeforeEach
    void setUp() {
        JsonRepositoryConfig config = spy(DefaultJsonRepositoryConfig.class);

        when(config.getDirectory(JsonTagRepository.class)).thenReturn(new File(DIRECTORY_PATH));

        underTest = new JsonTagRepository(config);

        testTag = TestDTOs.getTag();
    }

    @Test
    @DisplayName("Should throw exception when save a null tag")
    void shouldThrowExceptionWhenSaveNullTag() {
        assertThrows(JsonRepositoryException.class, () -> underTest.save(null));
    }

    @Test
    @DisplayName("Should throw exception when save a tag with null ID")
    void shouldThrowExceptionWhenSaveTagWithNullId() {
        TagDTO tagWithNullId = TagDTOBuilder.getInstance()
                .withId(null)
                .build();

        assertThrows(JsonRepositoryException.class, () -> underTest.save(tagWithNullId));
    }

    @Test
    @DisplayName("Should save a tag")
    void shouldSaveTag() {
        underTest.save(testTag);

        File file = getFileOfTestTag();

        assertTrue(file.exists(), "File should exist after saving tag");
    }

    @Test
    @DisplayName("Should not save a duplicate tag")
    void shouldNotSaveDuplicateTag() {
        underTest.save(testTag);

        assertThrows(JsonRepositoryException.class, () -> underTest.save(testTag));
    }

    @Test
    @DisplayName("Should find a tag by ID")
    void shouldFindTagById() {
        underTest.save(testTag);

        TagDTO tagFound = underTest.findById(testTag.id());

        assertEquals(testTag, tagFound, "Tag found should be equal to the saved tag");
    }

    @Test
    @DisplayName("Should not find a tag by non-existing ID")
    void shouldNotFindTagByNonExistingId() {
        Long nonExistringId = 999L;

        TagDTO tagFound = underTest.findById(nonExistringId);

        assertNull(tagFound, "Tag found should be null for non-existing ID");
    }

    @Test
    @DisplayName("Should delete a tag by ID")
    void shouldDeleteTagById() {
        underTest.save(testTag);

        underTest.deleteById(testTag.id());

        File file = getFileOfTestTag();

        assertFalse(file.exists(), "File should not exist after deleting tag");
    }

    @Test
    @DisplayName("Should not delete a non-existing tag")
    void shouldNotDeleteNonExistingTag() {
        Long nonExistingId = 999L;

        assertThrows(JsonRepositoryException.class, () -> underTest.deleteById(nonExistingId));
    }

    @Test
    @DisplayName("Should update an existing tag")
    void shouldUpdateExistingTag() {
        underTest.save(testTag);

        TagDTO updatedTag = TagDTOBuilder.getInstance().copyOf(testTag)
                .withName("Updated Tag")
                .build();

        underTest.update(updatedTag);

        TagDTO tagFound = underTest.findById(testTag.id());

        assertNotEquals(testTag, tagFound, "Updated tag should not be equal to the original tag");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @AfterEach
    void tearDown() {
        File dir = new File(DIRECTORY_PATH);
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        file.delete();
                    }
                }
            }
            dir.delete();
        }
    }

    private File getFileOfTestTag() {
        return new File(DIRECTORY_PATH + "/" + testTag.id() + ".json");
    }
}