package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
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
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;
import seedu.address.testutil.PersonBuilder;

/** Contains unit tests for {@code RemarkCommand}. */
public class RemarkCommandTest {

    @Test
    public void execute_addRemark_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Person originalPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(originalPerson).withRemark("Likes swimming.").build();
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(originalPerson, editedPerson);
        String expectedMessage = String.format(RemarkCommand.MESSAGE_ADD_REMARK_SUCCESS,
                Messages.format(editedPerson));

        assertCommandSuccess(new RemarkCommand(INDEX_FIRST_PERSON, new Remark("Likes swimming.")), model,
                expectedMessage, expectedModel);
    }

    @Test
    public void execute_removeRemark_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Person originalPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(originalPerson).withRemark("").build();
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(originalPerson, editedPerson);
        String expectedMessage = String.format(RemarkCommand.MESSAGE_DELETE_REMARK_SUCCESS,
                Messages.format(editedPerson));

        assertCommandSuccess(new RemarkCommand(INDEX_FIRST_PERSON, new Remark("")), model,
                expectedMessage, expectedModel);
    }

    @Test
    public void execute_remarkWithFollowUpDate_preservesFollowUpDate() {
        Person originalPerson = new PersonBuilder().withName("Follow Up Contact")
                .withFollowUpDate("2026-10-20").build();
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(originalPerson);
        Model model = new ModelManager(addressBook, new UserPrefs());
        Person editedPerson = new PersonBuilder(originalPerson).withRemark("Call next week.").build();
        Model expectedModel = new ModelManager(new AddressBook(addressBook), new UserPrefs());
        expectedModel.setPerson(originalPerson, editedPerson);
        String expectedMessage = String.format(RemarkCommand.MESSAGE_ADD_REMARK_SUCCESS,
                Messages.format(editedPerson));

        assertCommandSuccess(new RemarkCommand(INDEX_FIRST_PERSON, new Remark("Call next week.")), model,
                expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndex_failure() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        assertCommandFailure(new RemarkCommand(outOfBoundIndex,
                        new Remark("A remark.")), model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        RemarkCommand standardCommand = new RemarkCommand(INDEX_FIRST_PERSON, new Remark("Likes skiing."));

        assertTrue(standardCommand.equals(new RemarkCommand(INDEX_FIRST_PERSON, new Remark("Likes skiing."))));
        assertTrue(standardCommand.equals(standardCommand));
        assertFalse(standardCommand.equals(null));
        assertFalse(standardCommand.equals(new ClearCommand()));
        assertFalse(standardCommand.equals(new RemarkCommand(INDEX_SECOND_PERSON, new Remark("Likes skiing."))));
        assertFalse(standardCommand.equals(new RemarkCommand(INDEX_FIRST_PERSON, new Remark("Likes cycling."))));
    }
}
