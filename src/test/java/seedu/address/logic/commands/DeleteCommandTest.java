package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.FollowUpDate;
import seedu.address.model.person.Person;
import seedu.address.model.person.Role;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);
        showNoPerson(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validRoleUnfilteredList_success() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        String targetRole = personToDelete.getRole().value;
        DeleteCommand deleteCommand = new DeleteCommand(targetRole);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidRoleUnfilteredList_throwsCommandException() {
        String missingRole = "Non Existent Role";
        DeleteCommand deleteCommand = new DeleteCommand(missingRole);

        assertCommandFailure(deleteCommand, model,
                String.format(DeleteCommand.MESSAGE_ROLE_NOT_FOUND, missingRole));
    }

    @Test
    public void execute_personWithNullRole_throwsCommandException() {
        Person basePerson = new PersonBuilder().withName("No Role Person").build();
        Person personWithNullRole = new Person(basePerson.getName(), basePerson.getPhone(), basePerson.getEmail(),
                basePerson.getAddress(), basePerson.getTags(), basePerson.getRemark(), basePerson.getCategory(),
                basePerson.getRole(), basePerson.getCompany(), basePerson.getBoss(), basePerson.getFollowUpDate()) {
            @Override
            public Role getRole() {
                return null;
            }
        };
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(personWithNullRole);
        Model modelWithNullRole = new ModelManager(addressBook, new UserPrefs());

        assertCommandFailure(new DeleteCommand("Unavailable Role"), modelWithNullRole,
                String.format(DeleteCommand.MESSAGE_ROLE_NOT_FOUND, "Unavailable Role"));
    }

    @Test
    public void execute_validFollowUpDateUnfilteredList_success() {
        Person firstMatch = new PersonBuilder().withName("Alice Tan").withFollowUpDate("2026-10-20").build();
        Person secondMatch = new PersonBuilder().withName("Bob Tan").withFollowUpDate("2026-10-20").build();
        Person nonMatch = new PersonBuilder().withName("Carol Tan").withFollowUpDate("2026-10-21").build();
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(firstMatch);
        addressBook.addPerson(secondMatch);
        addressBook.addPerson(nonMatch);
        Model actualModel = new ModelManager(addressBook, new UserPrefs());
        Model expectedModel = new ModelManager(addressBook, new UserPrefs());
        expectedModel.deletePerson(firstMatch);
        expectedModel.deletePerson(secondMatch);

        String deletedPersons = Messages.format(firstMatch) + "\n" + Messages.format(secondMatch);
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSONS_SUCCESS, deletedPersons);

        assertCommandSuccess(new DeleteCommand(new FollowUpDate("2026-10-20")), actualModel,
                expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidFollowUpDateUnfilteredList_throwsCommandException() {
        FollowUpDate missingDate = new FollowUpDate("2026-12-31");
        DeleteCommand deleteCommand = new DeleteCommand(missingDate);

        assertCommandFailure(deleteCommand, model,
                String.format(DeleteCommand.MESSAGE_FOLLOW_UP_DATE_NOT_FOUND, missingDate));
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new DeleteCommand(INDEX_FIRST_PERSON);
        DeleteCommand deleteSecondCommand = new DeleteCommand(INDEX_SECOND_PERSON);
        DeleteCommand deleteByRoleFirst = new DeleteCommand("Software Engineer");
        DeleteCommand deleteByRoleSecond = new DeleteCommand("Product Manager");
        DeleteCommand deleteByDateFirst = new DeleteCommand(new FollowUpDate("2026-10-20"));
        DeleteCommand deleteByDateSecond = new DeleteCommand(new FollowUpDate("2026-10-21"));

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(INDEX_FIRST_PERSON);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // same role value -> returns true
        DeleteCommand deleteByRoleFirstCopy = new DeleteCommand("Software Engineer");
        assertTrue(deleteByRoleFirst.equals(deleteByRoleFirstCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));

        // different role -> returns false
        assertFalse(deleteByRoleFirst.equals(deleteByRoleSecond));

        // same follow-up date -> returns true
        assertTrue(deleteByDateFirst.equals(new DeleteCommand(new FollowUpDate("2026-10-20"))));

        // different follow-up date -> returns false
        assertFalse(deleteByDateFirst.equals(deleteByDateSecond));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteCommand deleteCommand = new DeleteCommand(targetIndex);
        String expected = DeleteCommand.class.getCanonicalName()
                + "{targetIndex=" + targetIndex + ", targetRole=null}";
        assertEquals(expected, deleteCommand.toString());

        DeleteCommand deleteByDate = new DeleteCommand(new FollowUpDate("2026-10-20"));
        String expectedDateCommand = DeleteCommand.class.getCanonicalName()
                + "{targetIndex=null, targetRole=null, targetFollowUpDate=2026-10-20}";
        assertEquals(expectedDateCommand, deleteByDate.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }
}
