package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class RoleContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = List.of("Software");
        List<String> secondPredicateKeywordList = List.of("Software", "Engineer");
        RoleContainsKeywordsPredicate firstPredicate = new RoleContainsKeywordsPredicate(firstPredicateKeywordList);
        RoleContainsKeywordsPredicate secondPredicate = new RoleContainsKeywordsPredicate(secondPredicateKeywordList);

        assertTrue(firstPredicate.equals(firstPredicate));
        assertTrue(firstPredicate.equals(new RoleContainsKeywordsPredicate(firstPredicateKeywordList)));
        assertFalse(firstPredicate.equals(secondPredicate));
        assertFalse(firstPredicate.equals(null));
        assertFalse(firstPredicate.equals(1));
    }

    @Test
    public void test_roleContainsKeywords_returnsTrue() {
        assertTrue(new RoleContainsKeywordsPredicate(List.of("software"))
                .test(new PersonBuilder().withRole("Software Engineer").build()));
        assertTrue(new RoleContainsKeywordsPredicate(List.of("Manager", "ENGINEER"))
                .test(new PersonBuilder().withRole("Software Engineer").build()));
    }

    @Test
    public void test_roleDoesNotContainKeywords_returnsFalse() {
        assertFalse(new RoleContainsKeywordsPredicate(List.of())
                .test(new PersonBuilder().withRole("Software Engineer").build()));
        assertFalse(new RoleContainsKeywordsPredicate(List.of("Manager"))
                .test(new PersonBuilder().withRole("Software Engineer").build()));
    }

    @Test
    public void test_roleContainsPartialKeywords_returnsTrue() {
        assertTrue(new RoleContainsKeywordsPredicate(List.of("engi"))
                .test(new PersonBuilder().withRole("Software Engineer").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("Software");
        RoleContainsKeywordsPredicate predicate = new RoleContainsKeywordsPredicate(keywords);

        assertEquals(RoleContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}",
                predicate.toString());
    }
}
