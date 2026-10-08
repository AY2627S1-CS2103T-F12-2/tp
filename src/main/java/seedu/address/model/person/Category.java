package seedu.address.model.person;

/**
 * Represents the category assigned to a person.
 */
public enum Category {
    UNCATEGORIZED,
    ENTITY,
    ROLE,
    TIER,
    SECTOR;

    public static final String MESSAGE_CONSTRAINTS =
            "Category should be one of: UNCATEGORIZED, ENTITY, ROLE, TIER, SECTOR";

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

}
