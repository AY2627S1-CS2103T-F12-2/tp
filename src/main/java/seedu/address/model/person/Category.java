package seedu.address.model.person;

/**
 * Represents the category assigned to a person.
 */
public enum Category {
    UNCATEGORIZED,
    CLIENT,
    INVESTOR,
    LAWYER,
    ACCOUNTANT,
    CONSULTANT,
    OTHER;

    public static final String MESSAGE_CONSTRAINTS =
            "Category should be one of: UNCATEGORIZED, CLIENT, INVESTOR, LAWYER, ACCOUNTANT";

    /**
     * Returns true if the given string matches a valid category.
     */
    public static boolean isValidCategory(String test) {
        try {
            Category.valueOf(test.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return name();
    }

}
