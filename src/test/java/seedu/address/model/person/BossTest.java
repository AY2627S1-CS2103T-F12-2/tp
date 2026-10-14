package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class BossTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Boss(null));
    }

    @Test
    public void constructor_invalidBoss_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Boss("Jane"));
    }

    @Test
    public void isValidBoss() {
        // null boss
        assertThrows(NullPointerException.class, () -> Boss.isValidBoss(null));

        // invalid boss names
        assertFalse(Boss.isValidBoss("Jane")); // missing last name
        assertFalse(Boss.isValidBoss("Jane2 Smith")); // digits not allowed
        assertFalse(Boss.isValidBoss("Jane-Smith")); // special character not allowed

        // valid boss names
        assertTrue(Boss.isValidBoss("")); // optional boss
        assertTrue(Boss.isValidBoss("Jane Smith"));
        assertTrue(Boss.isValidBoss("Jane Mary Smith"));
    }

    @Test
    public void constructor_trimsSpaces() {
        Boss boss = new Boss("  Jane Smith  ");

        assertTrue(boss.fullName.equals("Jane Smith"));
    }
}
