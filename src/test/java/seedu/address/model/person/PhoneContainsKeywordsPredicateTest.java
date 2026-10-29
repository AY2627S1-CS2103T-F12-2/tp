package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PhoneContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = List.of("1234");
        List<String> secondPredicateKeywordList = List.of("1234", "5678");
        PhoneContainsKeywordsPredicate firstPredicate = new PhoneContainsKeywordsPredicate(firstPredicateKeywordList);
        PhoneContainsKeywordsPredicate secondPredicate = new PhoneContainsKeywordsPredicate(secondPredicateKeywordList);

        assertTrue(firstPredicate.equals(firstPredicate));
        assertTrue(firstPredicate.equals(new PhoneContainsKeywordsPredicate(firstPredicateKeywordList)));
        assertFalse(firstPredicate.equals(secondPredicate));
        assertFalse(firstPredicate.equals(null));
        assertFalse(firstPredicate.equals(1));
        assertEquals(firstPredicate.hashCode(),
                new PhoneContainsKeywordsPredicate(firstPredicateKeywordList).hashCode());
    }

    @Test
    public void test_phoneContainsKeywords_returnsTrue() {
        assertTrue(new PhoneContainsKeywordsPredicate(List.of("1234"))
                .test(new PersonBuilder().withPhone("12345678").build()));
        assertTrue(new PhoneContainsKeywordsPredicate(List.of("9999", "5678"))
                .test(new PersonBuilder().withPhone("12345678").build()));
    }

    @Test
    public void test_phoneDoesNotContainKeywords_returnsFalse() {
        assertFalse(new PhoneContainsKeywordsPredicate(List.of())
                .test(new PersonBuilder().withPhone("12345678").build()));
        assertFalse(new PhoneContainsKeywordsPredicate(List.of("9999"))
                .test(new PersonBuilder().withPhone("12345678").build()));
    }

    @Test
    public void test_phoneContainsPartialKeywords_returnsTrue() {
        assertTrue(new PhoneContainsKeywordsPredicate(List.of("34"))
                .test(new PersonBuilder().withPhone("12345678").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("1234");
        PhoneContainsKeywordsPredicate predicate = new PhoneContainsKeywordsPredicate(keywords);
        assertEquals(PhoneContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}",
                predicate.toString());
    }
}
