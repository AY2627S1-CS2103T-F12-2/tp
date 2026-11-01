package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.AddressBookParser;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Interaction;
import seedu.address.model.person.InteractionType;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Tests deletion of interactions and its history behaviour.
 */
public class DeleteInteractionCommandTest {
    @Test
    public void execute_filteredList_matchesDisplayedNumberAndPreservesOtherFields() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Person original = model.getFilteredPersonList().get(1);
        Interaction latest = new Interaction("2026-10-20", InteractionType.CALL, "Latest");
        Interaction earlier = new Interaction("2026-10-10", InteractionType.MEETING, "Earlier");
        Person target = new PersonBuilder(original).withInteractions(List.of(latest, earlier)).build();
        model.setPerson(original, target);
        model.updateFilteredPersonList(person -> person.isSamePerson(target));
        AddressBookParser parser = new AddressBookParser();
        assertEquals(String.format(InteractionsCommand.MESSAGE_PERSON_SUCCESS, target.getName(),
                "1. " + latest + "\n2. " + earlier),
                parser.parseCommand("interactions 1").execute(model).getFeedbackToUser());

        parser.parseCommand("interaction 1 del/1").execute(model);

        assertEquals(new PersonBuilder(target).withInteractions(List.of(earlier)).build(),
                model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_deleteInteraction_preservesOtherDataAndSupportsUndoRedo() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        new InteractionCommand(INDEX_FIRST_PERSON, "2026-10-10", InteractionType.CALL, "First").execute(model);
        new InteractionCommand(INDEX_FIRST_PERSON, "2026-10-20", InteractionType.MEETING, "Second").execute(model);
        Person original = model.getFilteredPersonList().get(0);
        List<Person> otherPersons = List.copyOf(model.getFilteredPersonList().subList(1,
                model.getFilteredPersonList().size()));

        CommandResult result = new DeleteInteractionCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON).execute(model);

        Person updated = model.getFilteredPersonList().get(0);
        assertEquals(List.of(original.getInteractions().get(1)), updated.getInteractions());
        assertEquals(String.format(DeleteInteractionCommand.MESSAGE_SUCCESS, 1, original.getName()),
                result.getFeedbackToUser());
        assertEquals(otherPersons, model.getFilteredPersonList().subList(1, model.getFilteredPersonList().size()));
        model.undoAddressBook();
        assertEquals(original, model.getFilteredPersonList().get(0));
        model.redoAddressBook();
        assertEquals(updated, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_invalidDeletion_doesNotChangeModelOrHistory() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        List<Person> original = List.copyOf(model.getFilteredPersonList());
        DeleteInteractionCommand delete = new DeleteInteractionCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON);
        CommandException exception = assertThrows(CommandException.class, () -> delete.execute(model));
        assertEquals(DeleteInteractionCommand.MESSAGE_INVALID_INTERACTION_INDEX, exception.getMessage());
        assertEquals(original, model.getFilteredPersonList());
        assertFalse(model.canUndoAddressBook());

        Index invalidPerson = Index.fromOneBased(original.size() + 1);
        assertThrows(CommandException.class, () ->
                new DeleteInteractionCommand(invalidPerson, INDEX_FIRST_PERSON).execute(model));
        new InteractionCommand(INDEX_FIRST_PERSON, "2026-10-20", InteractionType.CALL, "").execute(model);
        original = List.copyOf(model.getFilteredPersonList());
        assertThrows(CommandException.class, () ->
                new DeleteInteractionCommand(INDEX_FIRST_PERSON, Index.fromOneBased(2)).execute(model));
        assertEquals(original, model.getFilteredPersonList());
    }

}
