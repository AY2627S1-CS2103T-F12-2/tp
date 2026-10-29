package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonInCategoriesPredicateTest {

    @Test
    public void test_personInCategories_returnsExpectedResult() {
        PersonInCategoriesPredicate predicate =
                new PersonInCategoriesPredicate(List.of(Category.CLIENT, Category.INVESTOR));

        assertTrue(predicate.test(new PersonBuilder().withCategory(Category.CLIENT).build()));
        assertTrue(predicate.test(new PersonBuilder().withCategory(Category.INVESTOR).build()));
        assertFalse(predicate.test(new PersonBuilder().withCategory(Category.LAWYER).build()));
        assertFalse(new PersonInCategoriesPredicate(List.of())
                .test(new PersonBuilder().withCategory(Category.CLIENT).build()));
    }

    @Test
    public void equalsHashCodeAndToString() {
        List<Category> categories = List.of(Category.CLIENT, Category.INVESTOR);
        PersonInCategoriesPredicate predicate = new PersonInCategoriesPredicate(categories);

        assertTrue(predicate.equals(predicate));
        assertTrue(predicate.equals(new PersonInCategoriesPredicate(categories)));
        assertFalse(predicate.equals(new PersonInCategoriesPredicate(List.of(Category.CLIENT))));
        assertFalse(predicate.equals(null));
        assertFalse(predicate.equals(1));
        assertEquals(predicate.hashCode(), new PersonInCategoriesPredicate(categories).hashCode());
        assertEquals(PersonInCategoriesPredicate.class.getCanonicalName() + "{categories=" + categories + "}",
                predicate.toString());
    }
}
