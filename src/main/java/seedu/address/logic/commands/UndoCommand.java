package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Restores the address book state before the most recent saved change.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Reverts the most recent saved contact-list change.\n"
            + "Example: " + COMMAND_WORD;
    public static final String MESSAGE_SUCCESS = "Undid previous action. Previous contact-list state restored.";
    public static final String MESSAGE_NO_UNDOABLE_ACTIONS = "There are no earlier changes to undo.";
    private static final Logger logger = LogsCenter.getLogger(UndoCommand.class);

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (!model.canUndoAddressBook()) {
            logger.warning("Undo unavailable: no earlier history state exists.");
            throw new CommandException(MESSAGE_NO_UNDOABLE_ACTIONS);
        }

        model.undoAddressBook();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
