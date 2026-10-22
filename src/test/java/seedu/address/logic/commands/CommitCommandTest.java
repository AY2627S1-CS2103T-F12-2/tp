package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;

public class CommitCommandTest {

    @Test
    public void execute_savesCurrentAddressBookAsUndoCheckpoint() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new CommitCommand(), model, CommitCommand.MESSAGE_SUCCESS, expectedModel);
        org.junit.jupiter.api.Assertions.assertTrue(model.canUndoAddressBook());
    }
}
