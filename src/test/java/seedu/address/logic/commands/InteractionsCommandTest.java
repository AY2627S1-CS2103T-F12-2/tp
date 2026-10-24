package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Interaction;
import seedu.address.model.person.InteractionType;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/** Tests listing recorded interactions. */
public class InteractionsCommandTest {
    @Test
    public void execute_noInteractions_showsEmptyMessage() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        assertCommandSuccess(new InteractionsCommand(), model,
                InteractionsCommand.MESSAGE_NO_INTERACTIONS, expectedModel);
    }

    @Test
    public void execute_filteredList_showsAllInteractionsWithoutChangingModel() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Interaction aliceInteraction = new Interaction(ALICE.getName().fullName,
                "2026-10-20", InteractionType.MEETING, "Discussed proposal");
        Interaction bensonInteraction = new Interaction(BENSON.getName().fullName,
                "2026-10-21", InteractionType.CALL, "Followed up");
        Person updatedAlice = new PersonBuilder(ALICE).withInteractions(List.of(aliceInteraction)).build();
        Person updatedBenson = new PersonBuilder(BENSON).withInteractions(List.of(bensonInteraction)).build();
        model.setPerson(ALICE, updatedAlice);
        model.setPerson(BENSON, updatedBenson);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);
        String expectedMessage = String.format(InteractionsCommand.MESSAGE_SUCCESS,
                aliceInteraction + "\n" + bensonInteraction);

        assertCommandSuccess(new InteractionsCommand(), model, expectedMessage, expectedModel);
    }
}
