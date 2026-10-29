package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Objects;

/**
 * Represents an interaction object.
 */
public class Interaction {
    public static final String MESSAGE_CONSTRAINTS =
            "Interaction requires a valid date (yyyy-MM-dd), type, and non-null note.";
    public static final String MESSAGE_INVALID_DATE =
            "Interaction date must be a valid calendar date in yyyy-MM-dd format, e.g. 2026-10-20.";

    /**
     * uuuu as Java STRICT uses uuuu instead of yyyy.
     */
    public static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);

    private final LocalDate date;
    private final InteractionType type;
    private final String note;

    /** Creates an interaction with a person on the given date. */
    public Interaction(String date, InteractionType type, String note) {
        requireAllNonNull(date, type, note);

        String trimmedDate = date.trim();

        checkArgument(isValidDate(trimmedDate), MESSAGE_INVALID_DATE);

        this.date = LocalDate.parse(trimmedDate, FORMATTER);
        this.type = type;
        this.note = note;
    }

    public LocalDate getDate() {
        return date;
    }

    public InteractionType getType() {
        return type;
    }

    public String getNote() {
        return note;
    }


    /**
     * Returns true if a given string is a valid interaction date.
     */
    public static boolean isValidDate(String date) {
        requireNonNull(date);
        if (!date.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")) {
            return false;
        }

        try {
            LocalDate.parse(date, FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }


    @Override
    public String toString() {
        return new StringBuilder()
                .append("Date: ")
                .append(date.format(FORMATTER))
                .append(", Type: ")
                .append(type)
                .append(", Note: ")
                .append(note)
                .toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Interaction otherInteraction)) {
            return false;
        }

        return date.equals(otherInteraction.date)
                && type.equals(otherInteraction.type)
                && note.equals(otherInteraction.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, type, note);
    }
}


