package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;

/**
 * Saves the current address book as a manual undo checkpoint.
 */
public class CommitCommand extends Command {

    public static final String COMMAND_WORD = "commit";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Saves a copy of the current addressbook.\n"
            + "Example: " + COMMAND_WORD;
    public static final String MESSAGE_SUCCESS = "Saved current version of ContactFind successfully";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.commitAddressBook(COMMAND_WORD);
        assert model.canUndoAddressBook();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
