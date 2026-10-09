package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Role;

/**
 * Deletes a person identified using its displayed index from the address book.
 * Partial implementation: supports deleting by role as an additional code path.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted person: %1$s";
    public static final String MESSAGE_ROLE_NOT_FOUND = "No person found with role: %s";

    private final Index targetIndex;
    private final String targetRole;

    public DeleteCommand(Index targetIndex) {
        this(targetIndex, null);
    }

    public DeleteCommand(String targetRole) {
        this(null, targetRole);
    }

    private DeleteCommand(Index targetIndex, String targetRole) {
        this.targetIndex = targetIndex;
        this.targetRole = targetRole;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (targetRole != null) {
            List<Person> lastShownList = model.getFilteredPersonList();
            Person personToDelete = null;

            for (Person person : lastShownList) {
                if (person.getRole() != null && person.getRole().equals(new Role(targetRole))) {
                    personToDelete = person;
                    break;
                }
            }

            if (personToDelete == null) {
                throw new CommandException(String.format(MESSAGE_ROLE_NOT_FOUND, targetRole));
            }

            model.deletePerson(personToDelete);
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));
        }

        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.deletePerson(personToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand otherDeleteCommand)) {
            return false;
        }

        return java.util.Objects.equals(targetIndex, otherDeleteCommand.targetIndex)
                && java.util.Objects.equals(targetRole, otherDeleteCommand.targetRole);
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        if (targetRole == null) {
            return builder.add("targetIndex", targetIndex).toString();
        }
        return builder.add("targetRole", targetRole).toString();
    }
}
