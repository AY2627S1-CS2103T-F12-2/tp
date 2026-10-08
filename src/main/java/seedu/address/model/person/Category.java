package seedu.address.model.person;

public enum Category {
    UNCATEGORIZED,
    ENTITY,
    ROLE,
    TIER,
    SECTOR;

    public static final String MESSAGE_CONSTRAINTS =
            "Category should be one of: UNCATEGORIZED, ENTITY, ROLE, TIER, SECTOR";

    public static boolean isValidCategory(String test) {
        try {
            Category.valueOf(test.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}