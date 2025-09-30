package com.jorgedelarosa.aimiddleware.domain.actor;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class OutfitTest {

    @Test
    void create_shouldGenerateIdAndSetFields() {
        Outfit outfit = Outfit.create("Casual", "Jeans and T-shirt");

        assertNotNull(outfit.getId(), "Expected a generated id");
        assertEquals("Casual", outfit.getName());
        assertEquals("Jeans and T-shirt", outfit.getDescription());
    }

    @Test
    void restore_shouldUseProvidedIdAndSetFields() {
        UUID id = UUID.randomUUID();
        Outfit outfit = Outfit.restore(id, "Formal", "Suit and tie");

        assertEquals(id, outfit.getId());
        assertEquals("Formal", outfit.getName());
        assertEquals("Suit and tie", outfit.getDescription());
    }

    @Test
    void gettersAndSetters_shouldWork() {
        Outfit outfit = Outfit.create("Original", "Original description");

        outfit.setName("Updated");
        outfit.setDescription("Updated description");

        assertEquals("Updated", outfit.getName());
        assertEquals("Updated description", outfit.getDescription());
    }

    @Test
    void validate_shouldSucceedForValidFields() {
        Outfit outfit = Outfit.create("Sporty", "Tracksuit");
        assertTrue(outfit.validate());
    }

    @Test
    void validate_shouldFailWhenNameEmpty() {
        Outfit outfit = Outfit.create("Valid", "Valid description");
        outfit.setName("");

        RuntimeException ex = assertThrows(RuntimeException.class, outfit::validate);
        assertTrue(ex.getMessage().contains("shouldn't be empty"));
    }

    @Test
    void validate_shouldFailWhenDescriptionNull() {
        Outfit outfit = Outfit.create("Valid", "Valid description");
        outfit.setDescription(null);

        RuntimeException ex = assertThrows(RuntimeException.class, outfit::validate);
        assertTrue(ex.getMessage().contains("shouldn't be empty"));
    }

    @Test
    void validate_shouldFailWhenDescriptionEmpty() {
        Outfit outfit = Outfit.create("Valid", "Valid description");
        outfit.setDescription("");

        RuntimeException ex = assertThrows(RuntimeException.class, outfit::validate);
        assertTrue(ex.getMessage().contains("shouldn't be empty"));
    }
}
