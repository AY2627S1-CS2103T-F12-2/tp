package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's company name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidCompanyName(String)}
 */
public class Company {

    public static final String MESSAGE_CONSTRAINTS =
            "Company names must be more than 2 characters long and contain only "
                    + "alphanumeric characters and single spaces.";

    public static final String VALIDATION_REGEX = "[\\p{Alnum}]+( [\\p{Alnum}]+)*";

    public final String value;

    /**
     * Constructs a {@code Company}.
     *
     * @param companyName A valid company name.
     */
    public Company(String companyName) {
        requireNonNull(companyName);
        companyName = companyName.trim().replaceAll(" +", " ");
        checkArgument(isValidCompanyName(companyName), MESSAGE_CONSTRAINTS);
        value = companyName;
    }

    /**
     * Returns true if a given string is a valid company name.
     */
    public static boolean isValidCompanyName(String test) {
        return test.length() > 2 && test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}