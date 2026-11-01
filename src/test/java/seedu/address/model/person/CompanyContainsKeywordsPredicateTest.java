package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class CompanyContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = List.of("Acme");
        List<String> secondPredicateKeywordList = List.of("Acme", "Corporation");
        CompanyContainsKeywordsPredicate firstPredicate =
                new CompanyContainsKeywordsPredicate(firstPredicateKeywordList);
        CompanyContainsKeywordsPredicate secondPredicate =
                new CompanyContainsKeywordsPredicate(secondPredicateKeywordList);

        assertTrue(firstPredicate.equals(firstPredicate));
        assertTrue(firstPredicate.equals(new CompanyContainsKeywordsPredicate(firstPredicateKeywordList)));
        assertFalse(firstPredicate.equals(secondPredicate));
        assertFalse(firstPredicate.equals(null));
        assertFalse(firstPredicate.equals(1));
    }

    @Test
    public void test_companyContainsKeywords_returnsTrue() {
        assertTrue(new CompanyContainsKeywordsPredicate(List.of("acme"))
                .test(new PersonBuilder().withCompany("Acme Corporation").build()));
        assertTrue(new CompanyContainsKeywordsPredicate(List.of("Other", "CORPORATION"))
                .test(new PersonBuilder().withCompany("Acme Corporation").build()));
    }

    @Test
    public void test_companyDoesNotContainKeywords_returnsFalse() {
        assertFalse(new CompanyContainsKeywordsPredicate(List.of())
                .test(new PersonBuilder().withCompany("Acme Corporation").build()));
        assertFalse(new CompanyContainsKeywordsPredicate(List.of("Other"))
                .test(new PersonBuilder().withCompany("Acme Corporation").build()));
    }

    @Test
    public void test_companyContainsPartialKeywords_returnsTrue() {
        assertTrue(new CompanyContainsKeywordsPredicate(List.of("corp"))
                .test(new PersonBuilder().withCompany("Acme Corporation").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("Acme");
        CompanyContainsKeywordsPredicate predicate = new CompanyContainsKeywordsPredicate(keywords);

        assertEquals(CompanyContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}",
                predicate.toString());
    }
}
