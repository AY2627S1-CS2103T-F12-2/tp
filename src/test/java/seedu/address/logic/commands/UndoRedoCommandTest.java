package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;

public class UndoRedoCommandTest {

    @Test
    public void undo_noEarlierState_throwsCommandException() {
        assertCommandFailure(new UndoCommand(), new ModelManager(), UndoCommand.MESSAGE_NO_UNDOABLE_ACTIONS);
    }

    @Test
    public void undo_previousState_restoresItAndEnablesRedo() {
        Model model = new ModelManager();
        model.addPerson(ALICE);
        model.commitAddressBook();

        assertCommandSuccess(new UndoCommand(), model, UndoCommand.MESSAGE_SUCCESS, new ModelManager());
        assertTrue(model.canRedoAddressBook());
    }

    @Test
    public void redo_noLaterState_throwsCommandException() {
        assertCommandFailure(new RedoCommand(), new ModelManager(), RedoCommand.MESSAGE_NO_REDOABLE_ACTIONS);
    }

    @Test
    public void redo_laterState_restoresIt() {
        Model model = new ModelManager();
        model.addPerson(ALICE);
        model.commitAddressBook();
        model.undoAddressBook();

        Model expectedModel = new ModelManager();
        expectedModel.addPerson(ALICE);
        assertCommandSuccess(new RedoCommand(), model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
