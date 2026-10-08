package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class FollowUpDateTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new FollowUpDate(null));
    }

    @Test
    public void constructor_invalidFollowUpDate_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new FollowUpDate(""));
        assertThrows(IllegalArgumentException.class, () -> new FollowUpDate("2026-02-30"));
    }

    @Test
    public void isValidFollowUpDate() {
        // null date
        assertThrows(NullPointerException.class, () -> FollowUpDate.isValidFollowUpDate(null));

        // invalid dates
        assertFalse(FollowUpDate.isValidFollowUpDate("")); // empty string
        assertFalse(FollowUpDate.isValidFollowUpDate(" ")); // spaces only
        assertFalse(FollowUpDate.isValidFollowUpDate("tomorrow")); // non-date text
        assertFalse(FollowUpDate.isValidFollowUpDate("20-10-2026")); // wrong order
        assertFalse(FollowUpDate.isValidFollowUpDate("2026/10/20")); // wrong separator
        assertFalse(FollowUpDate.isValidFollowUpDate("2026-1-5")); // missing leading zeros
        assertFalse(FollowUpDate.isValidFollowUpDate("2026-13-01")); // invalid month
        assertFalse(FollowUpDate.isValidFollowUpDate("2026-04-31")); // invalid day for month
        assertFalse(FollowUpDate.isValidFollowUpDate("2026-02-29")); // not a leap year

        // valid dates
        assertTrue(FollowUpDate.isValidFollowUpDate("2026-10-20"));
        assertTrue(FollowUpDate.isValidFollowUpDate("2028-02-29")); // leap year
        assertTrue(FollowUpDate.isValidFollowUpDate("2020-01-01")); // past date
    }

    @Test
    public void constructor_trimsWhitespace() {
        FollowUpDate date = new FollowUpDate("  2026-10-20  ");

        assertEquals(LocalDate.of(2026, 10, 20), date.value);
    }

    @Test
    public void toStringMethod() {
        assertEquals("2026-01-05", new FollowUpDate("2026-01-05").toString());
    }

    @Test
    public void equals() {
        FollowUpDate date = new FollowUpDate("2026-10-20");

        // same values -> returns true
        assertTrue(date.equals(new FollowUpDate("2026-10-20")));

        // same object -> returns true
        assertTrue(date.equals(date));

        // null -> returns false
        assertFalse(date.equals(null));

        // different types -> returns false
        assertFalse(date.equals(5.0f));

        // different values -> returns false
        assertFalse(date.equals(new FollowUpDate("2026-10-21")));
    }
}
