package seedu.address.model.person;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/** Tests whether a {@code Person}'s follow-up date matches any of the given dates. */
public class FollowUpDatesPredicate implements Predicate<Person> {
    private final List<FollowUpDate> followUpDates;

    public FollowUpDatesPredicate(List<FollowUpDate> followUpDates) {
        this.followUpDates = followUpDates;
    }

    @Override
    public boolean test(Person person) {
        return person.getFollowUpDate() != null && followUpDates.contains(person.getFollowUpDate());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof FollowUpDatesPredicate otherPredicate)) {
            return false;
        }
        return followUpDates.equals(otherPredicate.followUpDates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(followUpDates);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("followUpDates", followUpDates).toString();
    }
}
