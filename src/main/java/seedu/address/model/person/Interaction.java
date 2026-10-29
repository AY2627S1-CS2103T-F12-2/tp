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
            "Interaction requires a valid name, date (yyyy-MM-dd), type, and non-null note.";

    /**
     * uuuu as Java STRICT uses uuuu instead of yyyy.
     */
    public static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);

    private final Name name;
    private final LocalDate date;
    private final InteractionType type;
    private final String note;

    /** Creates an interaction with a person on the given date. */
    public Interaction(String name, String date, InteractionType type, String note) {
        requireAllNonNull(name, date, type, note);

        String trimmedDate = date.trim();

        checkArgument(Name.isValidName(name), Name.MESSAGE_CONSTRAINTS);
        checkArgument(isValidDate(trimmedDate), MESSAGE_CONSTRAINTS);

        this.name = new Name(name);
        this.date = LocalDate.parse(trimmedDate, FORMATTER);
        this.type = type;
        this.note = note;
    }

    public Name getName() {
        return name;
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
                .append("Interaction with: ")
                .append(name)
                .append(", Date: ")
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

        return name.equals(otherInteraction.name)
                && date.equals(otherInteraction.date)
                && type.equals(otherInteraction.type)
                && note.equals(otherInteraction.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, date, type, note);
    }
}


