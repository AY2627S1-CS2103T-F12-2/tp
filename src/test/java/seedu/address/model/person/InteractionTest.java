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
                new Interaction(null, InteractionType.CALL, "Note"));
        assertThrows(NullPointerException.class, () ->
                new Interaction("2026-10-20", null, "Note"));
        assertThrows(NullPointerException.class, () ->
                new Interaction("2026-10-20", InteractionType.CALL, null));
    }

    @Test
    public void constructor_invalidDate_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Interaction("", InteractionType.CALL, "Note"));
        assertThrows(IllegalArgumentException.class, () ->
                new Interaction("2026-02-30", InteractionType.CALL, "Note"));
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
                new Interaction("2026-10-20", InteractionType.CALL, "Discussed proposal");

        // same object -> returns true
        assertTrue(interaction.equals(interaction));
        assertEquals(interaction.hashCode(),
                new Interaction("2026-10-20", InteractionType.CALL, "Discussed proposal").hashCode());

        // same values -> returns true
        assertTrue(interaction.equals(
                new Interaction("2026-10-20", InteractionType.CALL, "Discussed proposal")));

        // different date -> returns false
        assertFalse(interaction.equals(
                new Interaction("2026-10-21", InteractionType.CALL, "Discussed proposal")));

        // different type -> returns false
        assertFalse(interaction.equals(
                new Interaction("2026-10-20", InteractionType.EMAIL, "Discussed proposal")));

        // different note -> returns false
        assertFalse(interaction.equals(
                new Interaction("2026-10-20", InteractionType.CALL, "Different note")));

        // null -> returns false
        assertFalse(interaction.equals(null));

        // different type of object -> returns false
        assertFalse(interaction.equals("interaction"));
    }

    @Test
    public void toStringMethod() {
        Interaction interaction =
                new Interaction("2026-10-20", InteractionType.CALL, "Discussed proposal");

        String expected = "Date: 2026-10-20, "
                + "Type: CALL, Note: Discussed proposal";

        assertEquals(expected, interaction.toString());
    }
}
