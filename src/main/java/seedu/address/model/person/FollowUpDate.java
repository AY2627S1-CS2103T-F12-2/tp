package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Represents a Person's follow-up date in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidFollowUpDate(String)}
 */
public class FollowUpDate {

    public static final String MESSAGE_CONSTRAINTS =
            "Follow-up dates should be valid calendar dates in the format yyyy-MM-dd, e.g. 2026-10-20";

    /**
     * uuuu as Java STRICT uses uuuu instead of yyyy.
     */
    public static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);

    public final LocalDate value;

    /**
     * Constructs a {@code FollowUpDate}.
     *
     * @param date A valid follow-up date in the format yyyy-MM-dd.
     */
    public FollowUpDate(String date) {
        requireNonNull(date);
        date = date.trim();
        checkArgument(isValidFollowUpDate(date), MESSAGE_CONSTRAINTS);
        value = LocalDate.parse(date, FORMATTER);
    }

    /**
     * Returns true if a given string is a valid follow-up date.
     */
    public static boolean isValidFollowUpDate(String test) {
        requireNonNull(test);
        try {
            LocalDate.parse(test, FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return value.format(FORMATTER);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FollowUpDate otherFollowUpDate)) {
            return false;
        }

        return value.equals(otherFollowUpDate.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
