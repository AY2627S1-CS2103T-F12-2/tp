package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Restores the contact-list state that was most recently undone.
 */
public class RedoCommand extends Command {

    public static final String COMMAND_WORD = "redo";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Reapplies the contact-list change most recently undone.\n"
            + "Example: " + COMMAND_WORD;
    public static final String MESSAGE_SUCCESS = "Redid previously undone action. Contact-list state restored.";
    public static final String MESSAGE_NO_REDOABLE_ACTIONS = "There are no later changes to redo.";
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (!model.canRedoAddressBook()) {
            throw new CommandException(MESSAGE_NO_REDOABLE_ACTIONS);
        }

        model.redoAddressBook();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
