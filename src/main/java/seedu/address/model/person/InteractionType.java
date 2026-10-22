package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.Locale;

/**
 * Represents the type of an interaction.
 */
public enum InteractionType {
    MEETING,
    CALL,
    EMAIL,
    PRESENTATION;

    public static final String MESSAGE_CONSTRAINTS =
            "Interaction type should be one of: MEETING, CALL, EMAIL, PRESENTATION";

    /**
     * Returns true if the given string matches a valid interaction type.
     */
    public static boolean isValidInteractionType(String test) {
        requireNonNull(test);
        try {
            InteractionType.valueOf(test.toUpperCase(Locale.ROOT));
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
