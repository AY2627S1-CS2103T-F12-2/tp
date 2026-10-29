package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class EmailContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = List.of("example");
        List<String> secondPredicateKeywordList = List.of("example", "work");
        EmailContainsKeywordsPredicate firstPredicate = new EmailContainsKeywordsPredicate(firstPredicateKeywordList);
        EmailContainsKeywordsPredicate secondPredicate = new EmailContainsKeywordsPredicate(secondPredicateKeywordList);

        assertTrue(firstPredicate.equals(firstPredicate));
        assertTrue(firstPredicate.equals(new EmailContainsKeywordsPredicate(firstPredicateKeywordList)));
        assertFalse(firstPredicate.equals(secondPredicate));
        assertFalse(firstPredicate.equals(null));
        assertFalse(firstPredicate.equals(1));
        assertEquals(firstPredicate.hashCode(),
                new EmailContainsKeywordsPredicate(firstPredicateKeywordList).hashCode());
    }

    @Test
    public void test_emailContainsKeywords_returnsTrue() {
        assertTrue(new EmailContainsKeywordsPredicate(List.of("example"))
                .test(new PersonBuilder().withEmail("amy@example.com").build()));
        assertTrue(new EmailContainsKeywordsPredicate(List.of("WORK", "AMY"))
                .test(new PersonBuilder().withEmail("amy@example.com").build()));
    }

    @Test
    public void test_emailDoesNotContainKeywords_returnsFalse() {
        assertFalse(new EmailContainsKeywordsPredicate(List.of())
                .test(new PersonBuilder().withEmail("amy@example.com").build()));
        assertFalse(new EmailContainsKeywordsPredicate(List.of("bob"))
                .test(new PersonBuilder().withEmail("amy@example.com").build()));
    }

    @Test
    public void test_emailContainsPartialKeywords_returnsTrue() {
        assertTrue(new EmailContainsKeywordsPredicate(List.of("amy"))
                .test(new PersonBuilder().withEmail("amy@example.com").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("example");
        EmailContainsKeywordsPredicate predicate = new EmailContainsKeywordsPredicate(keywords);
        assertEquals(EmailContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}",
                predicate.toString());
    }
}
