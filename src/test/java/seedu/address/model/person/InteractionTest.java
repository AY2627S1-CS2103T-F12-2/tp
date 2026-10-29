package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class InteractionTest {

    @Test
    public void constructor_anyNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new Interaction(null, "2026-10-20", InteractionType.CALL, "Note"));
        assertThrows(NullPointerException.class, () ->
                new Interaction("Peter Tan", null, InteractionType.CALL, "Note"));
        assertThrows(NullPointerException.class, () ->
                new Interaction("Peter Tan", "2026-10-20", null, "Note"));
        assertThrows(NullPointerException.class, () ->
                new Interaction("Peter Tan", "2026-10-20", InteractionType.CALL, null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Interaction("", "2026-10-20", InteractionType.CALL, "Note"));
        assertThrows(IllegalArgumentException.class, () ->
                new Interaction("invalid-name", "2026-10-20", InteractionType.CALL, "Note"));
    }

    @Test
    public void isValidDate() {
        // null date
        assertThrows(NullPointerException.class, () -> Interaction.isValidDate(null));

        // blank date
        assertFalse(Interaction.isValidDate("")); // empty string
        assertFalse(Interaction.isValidDate(" ")); // spaces only

        // invalid dates
        assertFalse(Interaction.isValidDate("2026-13-45")); // invalid month and day
        assertFalse(Interaction.isValidDate("-0001-01-01"));
        assertFalse(Interaction.isValidDate("+10000-01-01"));
        assertFalse(Interaction.isValidDate("2025-02-29"));
        assertTrue(Interaction.isValidDate("2024-02-29"));
        assertTrue(Interaction.isValidDate("0000-01-01"));
        assertTrue(Interaction.isValidDate("9999-12-31"));
        assertTrue(Interaction.isValidDate("2026-10-20"));
    }


    @Test
    public void equals() {
        Interaction interaction =
                new Interaction("Alice Pauline", "2026-10-20", InteractionType.CALL, "Discussed proposal");

        // same object -> returns true
        assertTrue(interaction.equals(interaction));

        // same values -> returns true
        assertTrue(interaction.equals(
                new Interaction("Alice Pauline", "2026-10-20", InteractionType.CALL, "Discussed proposal")));

        // different name -> returns false
        assertFalse(interaction.equals(
                new Interaction("Bob Ross", "2026-10-20", InteractionType.CALL, "Discussed proposal")));

        // different date -> returns false
        assertFalse(interaction.equals(
                new Interaction("Alice Pauline", "2026-10-21", InteractionType.CALL, "Discussed proposal")));

        // different type -> returns false
        assertFalse(interaction.equals(
                new Interaction("Alice Pauline", "2026-10-20", InteractionType.EMAIL, "Discussed proposal")));

        // different note -> returns false
        assertFalse(interaction.equals(
                new Interaction("Alice Pauline", "2026-10-20", InteractionType.CALL, "Different note")));

        // null -> returns false
        assertFalse(interaction.equals(null));

        // different type of object -> returns false
        assertFalse(interaction.equals("interaction"));
    }

    @Test
    public void toStringMethod() {
        Interaction interaction =
                new Interaction("Alice Pauline", "2026-10-20", InteractionType.CALL, "Discussed proposal");

        String expected = "Interaction with: Alice Pauline, Date: 2026-10-20, "
                + "Type: CALL, Note: Discussed proposal";

        assertEquals(expected, interaction.toString());
    }
}
