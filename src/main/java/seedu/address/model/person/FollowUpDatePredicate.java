package seedu.address.model.person;

import java.util.Objects;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests whether a person's follow-up date matches the requested date.
 */
public class FollowUpDatePredicate implements Predicate<Person> {
    private final FollowUpDate followUpDate;

    public FollowUpDatePredicate(FollowUpDate followUpDate) {
        this.followUpDate = followUpDate;
    }

    @Override
    public boolean test(Person person) {
        return followUpDate.equals(person.getFollowUpDate());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof FollowUpDatePredicate otherPredicate)) {
            return false;
        }

        return followUpDate.equals(otherPredicate.followUpDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(followUpDate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("followUpDate", followUpDate).toString();
    }
}
