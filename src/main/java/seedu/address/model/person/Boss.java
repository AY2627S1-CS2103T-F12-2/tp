package seedu.address.model.person;

/**
 * Represents a person's boss in the address book.
 */
public class Boss extends Name {
    public static final String MESSAGE_CONSTRAINTS =
            "Boss names should only contain alphabetical characters and spaces, "
                    + "must contain first and last name, or be empty";

    public Boss(String name) {
        super(name, MESSAGE_CONSTRAINTS, true);
    }

    /**
     * Returns true if a given boss name is empty or a valid name.
     */
    public static boolean isValidBoss(String test) {
        return test.isEmpty() || Name.isValidName(test);
    }
}
