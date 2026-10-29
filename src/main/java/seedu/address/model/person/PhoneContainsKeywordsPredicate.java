package seedu.address.model.person;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/** Tests that a {@code Person}'s phone number contains any of the given keywords. */
public class PhoneContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;

    public PhoneContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        return keywords.stream().anyMatch(keyword -> person.getPhone().value.contains(keyword));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof PhoneContainsKeywordsPredicate otherPredicate)) {
            return false;
        }
        return keywords.equals(otherPredicate.keywords);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
