package seedu.address.model.person;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

import java.util.List;
import java.util.function.Predicate;

/**
 * Tests that a {@code Person}'s {@code Category} matches the given category.
 */
public class PersonInCategory implements Predicate<Person> {
    private final List<String> category;

    public PersonInCategory(List<String> category) {
        this.category = category;
    }

    @Override
    public boolean test(Person person) {
        return category.stream()
                .anyMatch(category -> StringUtil.containsWordIgnoreCase(person.getCategory().name(), category));
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
        return new ToStringBuilder(this).add("keywords", category).toString();
    }
}
