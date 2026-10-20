package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.InteractionType;
import seedu.address.model.person.Person;

/** Tests that interactions are added to the selected person. */
public class InteractionCommandTest {
    @Test
    public void execute_validIndex_addsInteraction() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Person original = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        InteractionCommand command = new InteractionCommand(INDEX_FIRST_PERSON,
                "2026-10-20", InteractionType.MEETING, "Discussed proposal");

        command.execute(model);

        Person updated = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        assertEquals(original.getInteractions().size() + 1, updated.getInteractions().size());
        assertEquals("Discussed proposal", updated.getInteractions().getLast().getNote());
    }


    @Test
    public void execute_invalidDate_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        InteractionCommand command = new InteractionCommand(INDEX_FIRST_PERSON,
                "invalid-date", InteractionType.MEETING, "Discussed proposal");

        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Index invalidIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        InteractionCommand command = new InteractionCommand(invalidIndex,
                "2026-10-20", InteractionType.MEETING, "Discussed proposal");

        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void toStringMethod() {
        InteractionCommand command = new InteractionCommand(INDEX_FIRST_PERSON,
                "2026-10-20", InteractionType.MEETING, "Discussed proposal");
        String expectedString = InteractionCommand.class.getCanonicalName()
                + "{index=" + INDEX_FIRST_PERSON + ", date=2026-10-20, "
                + "type=MEETING, note=Discussed proposal}";
        assertEquals(expectedString, command.toString());
    }


    @Test
    public void equals() {
        InteractionCommand command1 = new InteractionCommand(INDEX_FIRST_PERSON,
                "2026-10-20", InteractionType.MEETING, "Discussed proposal");
        InteractionCommand command2 = new InteractionCommand(INDEX_FIRST_PERSON,
                "2026-10-20", InteractionType.MEETING, "Discussed proposal");
        InteractionCommand command3 = new InteractionCommand(INDEX_FIRST_PERSON,
                "2026-10-21", InteractionType.MEETING, "Discussed proposal");

        // same object -> returns true
        assertEquals(command1, command1);

        // same values -> returns true
        assertEquals(command1, command2);

        // different values -> returns false
        assertNotEquals(command1, command3);
    }

}
