package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    public static final String MESSAGE_CONSTRAINTS =
            "Names should only contain alphabetical characters and spaces, should not be blank "
                    + "and must contain first and last name";

    /*
     * A valid name consists of at least a first name and last name, separated by spaces.
     */
    public static final String VALIDATION_REGEX = "[A-Za-z]+( +[A-Za-z]+)+";

    public final String fullName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        this(name, MESSAGE_CONSTRAINTS);
    }

    /**
     * Constructs a {@code Name} with a custom validation message.
     *
     * @param name A valid name.
     * @param messageConstraints The message displayed when validation fails.
     */
    protected Name(String name, String messageConstraints) {
        name = name.trim();
        requireNonNull(name);
        checkArgument(isValidName(name), messageConstraints);
        fullName = name;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return test.trim().matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Name otherName)) {
            return false;
        }

        return fullName.equals(otherName.fullName);
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

}
