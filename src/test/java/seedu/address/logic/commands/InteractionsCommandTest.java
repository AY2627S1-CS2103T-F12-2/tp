package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
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
        Interaction aliceInteraction = new Interaction("2026-10-20", InteractionType.MEETING, "Discussed proposal");
        Interaction bensonInteraction = new Interaction("2026-10-21", InteractionType.CALL, "Followed up");
        Person updatedAlice = new PersonBuilder(ALICE).withInteractions(List.of(aliceInteraction)).build();
        Person updatedBenson = new PersonBuilder(BENSON).withInteractions(List.of(bensonInteraction)).build();
        model.setPerson(ALICE, updatedAlice);
        model.setPerson(BENSON, updatedBenson);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);
        String expectedMessage = String.format(InteractionsCommand.MESSAGE_SUCCESS,
                "Interaction with: " + ALICE.getName() + ", " + aliceInteraction
                        + "\nInteraction with: " + BENSON.getName() + ", " + bensonInteraction);

        assertCommandSuccess(new InteractionsCommand(), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexWithInteractions_showsOnlyThatPersonsInteractions() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Interaction first = new Interaction("2026-10-20", InteractionType.MEETING, "Discussed proposal");
        Interaction second = new Interaction("2026-10-22", InteractionType.EMAIL, "Sent term sheet");
        Interaction bensonInteraction = new Interaction("2026-10-21", InteractionType.CALL, "Followed up");
        model.setPerson(ALICE, new PersonBuilder(ALICE).withInteractions(List.of(first, second)).build());
        model.setPerson(BENSON, new PersonBuilder(BENSON).withInteractions(List.of(bensonInteraction)).build());
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        String expectedMessage = String.format(InteractionsCommand.MESSAGE_PERSON_SUCCESS,
                ALICE.getName(), first + "\n" + second);

        assertCommandSuccess(new InteractionsCommand(INDEX_FIRST_PERSON), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexNoInteractions_showsPersonEmptyMessage() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        String expectedMessage = String.format(InteractionsCommand.MESSAGE_PERSON_NO_INTERACTIONS, ALICE.getName());

        assertCommandSuccess(new InteractionsCommand(INDEX_FIRST_PERSON), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexFilteredList_usesDisplayedList() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Interaction bensonInteraction = new Interaction("2026-10-21", InteractionType.CALL, "Followed up");
        model.setPerson(BENSON, new PersonBuilder(BENSON).withInteractions(List.of(bensonInteraction)).build());
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        // only Benson is displayed, so index 1 refers to Benson rather than Alice
        showPersonAtIndex(model, INDEX_SECOND_PERSON);
        showPersonAtIndex(expectedModel, INDEX_SECOND_PERSON);

        String expectedMessage = String.format(InteractionsCommand.MESSAGE_PERSON_SUCCESS,
                BENSON.getName(), bensonInteraction);

        assertCommandSuccess(new InteractionsCommand(INDEX_FIRST_PERSON), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        assertCommandFailure(new InteractionsCommand(outOfBoundIndex), model,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        // index 2 exists in the address book but not in the filtered list
        assertCommandFailure(new InteractionsCommand(INDEX_SECOND_PERSON), model,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        InteractionsCommand listAll = new InteractionsCommand();
        InteractionsCommand firstPerson = new InteractionsCommand(INDEX_FIRST_PERSON);

        // same object -> returns true
        assertTrue(firstPerson.equals(firstPerson));

        // same values -> returns true
        assertTrue(listAll.equals(new InteractionsCommand()));
        assertTrue(firstPerson.equals(new InteractionsCommand(INDEX_FIRST_PERSON)));

        // different index -> returns false
        assertFalse(firstPerson.equals(new InteractionsCommand(INDEX_SECOND_PERSON)));

        // index vs no index -> returns false
        assertFalse(listAll.equals(firstPerson));
        assertFalse(firstPerson.equals(listAll));

        // null / different type -> returns false
        assertFalse(firstPerson.equals(null));
        assertFalse(firstPerson.equals(1));
    }

    @Test
    public void toStringMethod() {
        InteractionsCommand command = new InteractionsCommand(INDEX_FIRST_PERSON);
        String expected = InteractionsCommand.class.getCanonicalName() + "{index=" + INDEX_FIRST_PERSON + "}";
        assertEquals(expected, command.toString());
    }
}
