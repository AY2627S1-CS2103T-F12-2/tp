package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class CategoryTest {

    @Test
    public void isValidCategory() {
        // null category
        assertThrows(NullPointerException.class, () -> Category.isValidCategory(null));

        // invalid categories
        assertFalse(Category.isValidCategory(""));
        assertFalse(Category.isValidCategory(" "));
        assertFalse(Category.isValidCategory("Investment"));

        // valid categories
        assertTrue(Category.isValidCategory("UNCATEGORIZED"));
        assertTrue(Category.isValidCategory("CLIENT"));
        assertTrue(Category.isValidCategory("INVESTOR"));
        assertTrue(Category.isValidCategory("LAWYER"));
        assertTrue(Category.isValidCategory("ACCOUNTANT"));
        assertTrue(Category.isValidCategory("CONSULTANT"));
        assertTrue(Category.isValidCategory("OTHER"));
        assertTrue(Category.isValidCategory("investor"));
    }

    @Test
    public void equals() {
        Category category = Category.INVESTOR;

        // same values -> returns true
        assertTrue(category.equals(Category.INVESTOR));

        // same object -> returns true
        assertTrue(category.equals(category));

        // null -> returns false
        assertFalse(category.equals(null));

        // different types -> returns false
        assertFalse(category.equals(20));

        // different values -> returns false
        assertFalse(category.equals(Category.CLIENT));
    }
}
