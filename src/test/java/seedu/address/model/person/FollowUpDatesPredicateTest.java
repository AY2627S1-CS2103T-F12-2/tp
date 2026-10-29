package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class FollowUpDatesPredicateTest {

    @Test
    public void test_followUpDatesMatch_returnsExpectedResult() {
        List<FollowUpDate> dates = List.of(new FollowUpDate("2026-10-20"), new FollowUpDate("2026-11-20"));
        FollowUpDatesPredicate predicate = new FollowUpDatesPredicate(dates);

        assertTrue(predicate.test(new PersonBuilder().withFollowUpDate("2026-10-20").build()));
        assertTrue(predicate.test(new PersonBuilder().withFollowUpDate("2026-11-20").build()));
        assertFalse(predicate.test(new PersonBuilder().withFollowUpDate("2026-12-20").build()));
        assertFalse(predicate.test(new PersonBuilder().build()));
    }

    @Test
    public void equalsHashCodeAndToString() {
        List<FollowUpDate> dates = List.of(new FollowUpDate("2026-10-20"), new FollowUpDate("2026-11-20"));
        FollowUpDatesPredicate predicate = new FollowUpDatesPredicate(dates);

        assertTrue(predicate.equals(predicate));
        assertTrue(predicate.equals(new FollowUpDatesPredicate(dates)));
        assertFalse(predicate.equals(new FollowUpDatesPredicate(List.of(new FollowUpDate("2026-12-20")))));
        assertFalse(predicate.equals(null));
        assertFalse(predicate.equals(1));
        assertEquals(predicate.hashCode(), new FollowUpDatesPredicate(dates).hashCode());
        assertEquals(FollowUpDatesPredicate.class.getCanonicalName() + "{followUpDates=" + dates + "}",
                predicate.toString());
    }
}
