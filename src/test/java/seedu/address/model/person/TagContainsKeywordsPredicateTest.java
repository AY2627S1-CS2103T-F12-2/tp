package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class TagContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = List.of("friend");
        List<String> secondPredicateKeywordList = List.of("friend", "work");
        TagContainsKeywordsPredicate firstPredicate = new TagContainsKeywordsPredicate(firstPredicateKeywordList);
        TagContainsKeywordsPredicate secondPredicate = new TagContainsKeywordsPredicate(secondPredicateKeywordList);

        assertTrue(firstPredicate.equals(firstPredicate));
        assertTrue(firstPredicate.equals(new TagContainsKeywordsPredicate(firstPredicateKeywordList)));
        assertFalse(firstPredicate.equals(secondPredicate));
        assertFalse(firstPredicate.equals(null));
        assertFalse(firstPredicate.equals(1));
        assertEquals(firstPredicate.hashCode(), new TagContainsKeywordsPredicate(firstPredicateKeywordList).hashCode());
    }

    @Test
    public void test_tagContainsKeywords_returnsTrue() {
        assertTrue(new TagContainsKeywordsPredicate(List.of("friend"))
                .test(new PersonBuilder().withTags("friends", "work").build()));
        assertTrue(new TagContainsKeywordsPredicate(List.of("family", "WORK"))
                .test(new PersonBuilder().withTags("friends", "work").build()));
    }

    @Test
    public void test_tagDoesNotContainKeywords_returnsFalse() {
        assertFalse(new TagContainsKeywordsPredicate(List.of())
                .test(new PersonBuilder().withTags("friends").build()));
        assertFalse(new TagContainsKeywordsPredicate(List.of("family"))
                .test(new PersonBuilder().withTags("friends", "work").build()));
    }

    @Test
    public void test_tagContainsPartialKeywords_returnsTrue() {
        assertTrue(new TagContainsKeywordsPredicate(List.of("rien"))
                .test(new PersonBuilder().withTags("friends").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("friend");
        TagContainsKeywordsPredicate predicate = new TagContainsKeywordsPredicate(keywords);
        assertEquals(TagContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}",
                predicate.toString());
    }
}
