package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class FollowUpDatePredicateTest {

    @Test
    public void test_followUpDateMatches_returnsTrue() {
        FollowUpDatePredicate predicate = new FollowUpDatePredicate(new FollowUpDate("2026-10-20"));

        assertTrue(predicate.test(new PersonBuilder().withFollowUpDate("2026-10-20").build()));
        assertFalse(predicate.test(new PersonBuilder().withFollowUpDate("2026-10-21").build()));
        assertFalse(predicate.test(new PersonBuilder().build()));
    }

    @Test
    public void equals() {
        FollowUpDatePredicate predicate = new FollowUpDatePredicate(new FollowUpDate("2026-10-20"));
        FollowUpDatePredicate differentPredicate = new FollowUpDatePredicate(new FollowUpDate("2026-10-21"));

        assertTrue(predicate.equals(predicate));
        assertTrue(predicate.equals(new FollowUpDatePredicate(new FollowUpDate("2026-10-20"))));
        assertFalse(predicate.equals(differentPredicate));
        assertFalse(predicate.equals(null));
        assertFalse(predicate.equals(1));
        assertEquals(predicate.hashCode(),
                new FollowUpDatePredicate(new FollowUpDate("2026-10-20")).hashCode());
        assertEquals(FollowUpDatePredicate.class.getCanonicalName()
                + "{followUpDate=2026-10-20}", predicate.toString());
    }
}
