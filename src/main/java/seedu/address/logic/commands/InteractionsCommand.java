package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Lists recorded interactions of all contacts, or of the contact at the given index in the displayed list.
 */
public class InteractionsCommand extends Command {
    public static final String COMMAND_WORD = "interactions";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists recorded interactions of all contacts, or of the contact identified by the index number "
            + "used in the displayed person list.\n"
            + "Parameters: [INDEX] (must be a positive integer)\n"
            + "Examples: " + COMMAND_WORD + "\n"
            + "          " + COMMAND_WORD + " 1";
    public static final String MESSAGE_SUCCESS = "Listed all interactions:\n%1$s";
    public static final String MESSAGE_PERSON_SUCCESS = "Listed interactions with %1$s:\n%2$s";
    public static final String MESSAGE_NO_INTERACTIONS = "No interactions recorded.";
    public static final String MESSAGE_PERSON_NO_INTERACTIONS = "No interactions recorded with %1$s.";

    // null means interactions of all contacts are listed
    private final Index index;

    /**
     * Creates an InteractionsCommand that lists the interactions of all contacts.
     */
    public InteractionsCommand() {
        this.index = null;
    }

    /**
     * Creates an InteractionsCommand that lists the interactions of the contact at {@code index}.
     */
    public InteractionsCommand(Index index) {
        requireNonNull(index);
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (index == null) {
            return listAllInteractions(model);
        }

        List<Person> lastShownList = model.getFilteredPersonList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person target = lastShownList.get(index.getZeroBased());
        if (target.getInteractions().isEmpty()) {
            return new CommandResult(String.format(MESSAGE_PERSON_NO_INTERACTIONS, target.getName()));
        }
        String formattedInteractions = target.getInteractions().stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));
        return new CommandResult(String.format(MESSAGE_PERSON_SUCCESS, target.getName(), formattedInteractions));
    }

    private CommandResult listAllInteractions(Model model) {
        String formattedInteractions = model.getUnfilteredPersonList().stream()
                .flatMap(person -> person.getInteractions().stream()
                        .map(interaction -> "Interaction with: " + person.getName() + ", " + interaction))
                .collect(Collectors.joining("\n"));

        if (formattedInteractions.isEmpty()) {
            return new CommandResult(MESSAGE_NO_INTERACTIONS);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, formattedInteractions));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof InteractionsCommand otherInteractionsCommand)) {
            return false;
        }

        return Objects.equals(index, otherInteractionsCommand.index);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(index);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .toString();
    }
}
