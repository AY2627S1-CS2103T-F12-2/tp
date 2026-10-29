package seedu.address.model.person;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/** Tests whether a {@code Person} belongs to any of the given categories. */
public class PersonInCategoriesPredicate implements Predicate<Person> {
    private final List<Category> categories;

    public PersonInCategoriesPredicate(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public boolean test(Person person) {
        return categories.contains(person.getCategory());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof PersonInCategoriesPredicate otherPredicate)) {
            return false;
        }
        return categories.equals(otherPredicate.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categories);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("categories", categories).toString();
    }
}
