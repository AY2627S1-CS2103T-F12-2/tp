package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class RoleTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Role(null));
    }

    @Test
    public void constructor_invalidRole_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Role(""));
    }

    @Test
    public void isValidRole() {
        // null role
        assertThrows(NullPointerException.class, () -> Role.isValidRole(null));

        // invalid roles
        assertFalse(Role.isValidRole("")); // empty string
        assertFalse(Role.isValidRole(" ")); // spaces only
        assertFalse(Role.isValidRole("Software-Engineer")); // special character
        assertFalse(Role.isValidRole("Software@Engineer")); // special character
        assertFalse(Role.isValidRole("Software  Engineer")); // repeated spaces
        assertFalse(Role.isValidRole(" Software Engineer")); // leading space
        assertFalse(Role.isValidRole("Software Engineer ")); // trailing space

        // valid roles
        assertTrue(Role.isValidRole("Manager"));
        assertTrue(Role.isValidRole("Software Engineer"));
        assertTrue(Role.isValidRole("Level 2 Support")); // alphanumeric words
    }

    @Test
    public void constructor_trimsAndCondensesSpaces() {
        Role role = new Role("  Software   Engineer  ");

        assertTrue(role.value.equals("Software Engineer"));
    }

    @Test
    public void equals() {
        Role role = new Role("Software Engineer");

        // same values -> returns true
        assertTrue(role.equals(new Role("Software Engineer")));

        // same object -> returns true
        assertTrue(role.equals(role));

        // values with different casing -> returns true
        assertTrue(role.equals(new Role("software engineer")));

        // repeated spaces are condensed -> returns true
        assertTrue(role.equals(new Role("Software   Engineer")));

        // null -> returns false
        assertFalse(role.equals(null));

        // different types -> returns false
        assertFalse(role.equals(5.0f));

        // different values -> returns false
        assertFalse(role.equals(new Role("Project Manager")));
    }
}
