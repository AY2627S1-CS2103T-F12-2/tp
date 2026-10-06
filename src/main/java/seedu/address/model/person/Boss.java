package seedu.address.model.person;

/**
 * Represents a person's boss in the address book.
 */
public class Boss extends Name {
    public static final String MESSAGE_CONSTRAINTS =
            "Boss names should only contain alphabetical characters and spaces, "
                    + "should not be blank and must contain first and last name";

    public Boss(String name) {
        super(name, MESSAGE_CONSTRAINTS);
    }
}
