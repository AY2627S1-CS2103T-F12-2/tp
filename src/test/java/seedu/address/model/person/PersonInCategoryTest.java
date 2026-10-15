package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonInCategoryTest {

    @Test
    public void equals() {
        PersonInCategory investorPredicate = new PersonInCategory(Category.INVESTOR);
        PersonInCategory investorPredicateCopy = new PersonInCategory(Category.INVESTOR);
        PersonInCategory clientPredicate = new PersonInCategory(Category.CLIENT);

        assertTrue(investorPredicate.equals(investorPredicate));
        assertTrue(investorPredicate.equals(investorPredicateCopy));
        assertFalse(investorPredicate.equals(clientPredicate));
        assertFalse(investorPredicate.equals(null));
        assertFalse(investorPredicate.equals(1));
    }

    @Test
    public void test_personAssignedToCategory_returnsTrue() {
        Person person = new PersonBuilder().withCategory(Category.INVESTOR).build();
        PersonInCategory predicate = new PersonInCategory(Category.INVESTOR);

        assertTrue(predicate.test(person));
    }

    @Test
    public void test_personNotAssignedToCategory_returnsFalse() {
        Person person = new PersonBuilder().withCategory(Category.CLIENT).build();
        PersonInCategory predicate = new PersonInCategory(Category.INVESTOR);

        assertFalse(predicate.test(person));
    }

    @Test
    public void toStringMethod() {
        Category category = Category.INVESTOR;
        PersonInCategory personInCategory = new PersonInCategory(category);
        String expected = PersonInCategory.class.getCanonicalName() + "{category=" + category + "}";
        assertEquals(expected, personInCategory.toString());
    }
}
