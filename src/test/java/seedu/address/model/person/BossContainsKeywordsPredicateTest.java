package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class BossContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = List.of("Alice");
        List<String> secondPredicateKeywordList = List.of("Alice", "Bob");
        BossContainsKeywordsPredicate firstPredicate = new BossContainsKeywordsPredicate(firstPredicateKeywordList);
        BossContainsKeywordsPredicate secondPredicate = new BossContainsKeywordsPredicate(secondPredicateKeywordList);

        assertTrue(firstPredicate.equals(firstPredicate));
        assertTrue(firstPredicate.equals(new BossContainsKeywordsPredicate(firstPredicateKeywordList)));
        assertFalse(firstPredicate.equals(secondPredicate));
        assertFalse(firstPredicate.equals(null));
        assertFalse(firstPredicate.equals(1));
        assertEquals(firstPredicate.hashCode(),
                new BossContainsKeywordsPredicate(firstPredicateKeywordList).hashCode());
    }

    @Test
    public void test_bossContainsKeywords_returnsTrue() {
        assertTrue(new BossContainsKeywordsPredicate(List.of("alice"))
                .test(new PersonBuilder().withBoss("Alice Smith").build()));
        assertTrue(new BossContainsKeywordsPredicate(List.of("Bob", "SMITH"))
                .test(new PersonBuilder().withBoss("Alice Smith").build()));
    }

    @Test
    public void test_bossDoesNotContainKeywords_returnsFalse() {
        assertFalse(new BossContainsKeywordsPredicate(List.of())
                .test(new PersonBuilder().withBoss("Alice Smith").build()));
        assertFalse(new BossContainsKeywordsPredicate(List.of("Bob"))
                .test(new PersonBuilder().withBoss("Alice Smith").build()));
    }

    @Test
    public void test_bossContainsPartialKeywords_returnsTrue() {
        assertTrue(new BossContainsKeywordsPredicate(List.of("lic"))
                .test(new PersonBuilder().withBoss("Alice Smith").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("Alice");
        BossContainsKeywordsPredicate predicate = new BossContainsKeywordsPredicate(keywords);
        assertEquals(BossContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}",
                predicate.toString());
    }
}
