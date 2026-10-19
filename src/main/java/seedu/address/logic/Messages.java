package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command.";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format!\n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid.";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d person(s) listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Phone: ")
                .append(person.getPhone())
                .append("; Email: ")
                .append(person.getEmail())
                .append("; Address: ")
                .append(person.getAddress())
                .append("; Tags: ");
        person.getTags().forEach(builder::append);
        builder.append("; Role: ")
                .append(person.getRole())
                .append("; Company: ")
                .append(person.getCompany())
                .append("; Boss: ")
                .append(person.getBoss());
        if (person.getFollowUpDate() != null) {
            builder.append("; Follow-up date: ")
                    .append(person.getFollowUpDate());
        }
        builder.append("; Category: ")
                .append(person.getCategory());
        return builder.toString();
    }

    /** Formats the fields shown in the edit command's success message. */
    public static String formatEditedPerson(Person person) {
        String result = person.getName()
                + "; Phone: " + person.getPhone()
                + "; Email: " + person.getEmail()
                + "; Role: " + person.getRole()
                + "; Company: " + person.getCompany()
                + "; Boss: " + person.getBoss();
        if (person.getFollowUpDate() != null) {
            result += "; Follow-up date: " + person.getFollowUpDate();
        }
        return result;
    }

}
