package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/** Sorts contacts by their follow-up date. */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sorts contacts by follow-up date.\n"
            + "Parameters: fd/[asc|desc], if no fd/ defaults to ascending. \n"
            + "Examples: " + COMMAND_WORD + "\n"
            + "          " + COMMAND_WORD + " fd/desc";
    public static final String MESSAGE_SUCCESS = "Contacts sorted by follow-up date (%1$s).";

    private final boolean descending;

    public SortCommand() {
        this(false);
    }

    public SortCommand(boolean descending) {
        this.descending = descending;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        AddressBook sortedAddressBook = new AddressBook(model.getAddressBook());
        List<Person> sortedPersons = new ArrayList<>(sortedAddressBook.getPersonList());
        sortedPersons.sort(followUpDateComparator());
        sortedAddressBook.setPersons(sortedPersons);
        model.setAddressBook(sortedAddressBook);
        model.commitAddressBook(COMMAND_WORD);

        return new CommandResult(String.format(MESSAGE_SUCCESS, descending ? "descending" : "ascending"));
    }

    private Comparator<Person> followUpDateComparator() {
        return (first, second) -> {
            if (first.getFollowUpDate() == null && second.getFollowUpDate() == null) {
                return compareNames(first, second);
            }
            if (first.getFollowUpDate() == null) {
                return 1;
            }
            if (second.getFollowUpDate() == null) {
                return -1;
            }
            int dateComparison = first.getFollowUpDate().value.compareTo(second.getFollowUpDate().value);
            if (dateComparison != 0) {
                return descending ? -dateComparison : dateComparison;
            }
            return compareNames(first, second);
        };
    }

    /* Helper for sort comparator*/
    private int compareNames(Person first, Person second) {
        int nameComparison = first.getName().fullName.compareToIgnoreCase(second.getName().fullName);
        return descending ? -nameComparison : nameComparison;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof SortCommand otherSortCommand)) {
            return false;
        }
        return descending == otherSortCommand.descending;
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(descending);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("descending", descending).toString();
    }
}
