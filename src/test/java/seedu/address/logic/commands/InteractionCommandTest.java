package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

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
}
