package seedu.address.model.person;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;


/**
 * Tests that a {@code Person}'s {@code Category} matches the given category.
 */
public class PersonInCategory implements Predicate<Person> {
    private final Category category;

    public PersonInCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean test(Person person) {
        return person.getCategory().equals(category);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonInCategory otherPersonInCategoryPredicate)) {
            return false;
        }

        return category.equals(otherPersonInCategoryPredicate.category);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("category", category).toString();
    }
}
