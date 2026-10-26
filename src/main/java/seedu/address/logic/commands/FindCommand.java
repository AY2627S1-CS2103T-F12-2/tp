package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Finds and lists all persons in the address book whose details match the given search criteria.
 * Keyword matching is case insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";
    public static final String MESSAGE_EMPTY_SEARCH_KEYWORD =
            "A search keyword is required after each search prefix.";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose details match the specified "
            + "keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: one or more of n/KEYWORDS, rl/KEYWORDS, com/KEYWORDS, fd/FOLLOW_UP_DATE\n"
            + "Note: A search prefix is required. Different search prefixes can be chained and are combined with AND, "
            + "but each prefix can be used only once.\n"
            + "To search for multiple values in one field, use one prefix followed by space-separated keywords; "
            + "for example, find n/Kurz Elle Kunz.\n"
            + "Examples: " + COMMAND_WORD + " n/alice\n"
            + "         " + COMMAND_WORD + " rl/engineer\n"
            + "         " + COMMAND_WORD + " com/google";

    private final Predicate<Person> predicate;

    public FindCommand(Predicate<Person> predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand otherFindCommand)) {
            return false;
        }

        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public int hashCode() {
        return predicate.hashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
