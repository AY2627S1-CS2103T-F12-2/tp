package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Category;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code CategoryCommand}.
 */
public class CategoryCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToCategorize = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        CategoryCommand categoryCommand = new CategoryCommand(INDEX_FIRST_PERSON, Category.INVESTOR);

        Person editedPerson = new PersonBuilder(personToCategorize).withCategory(Category.INVESTOR).build();
        String expectedMessage = String.format(CategoryCommand.MESSAGE_SUCCESS,
                editedPerson.getName(), Category.INVESTOR);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToCategorize, editedPerson);

        assertCommandSuccess(categoryCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void constructor_nullArguments_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new CategoryCommand(null, Category.CLIENT));
        assertThrows(NullPointerException.class, () -> new CategoryCommand(INDEX_FIRST_PERSON, null));
    }

    @Test
    public void execute_alreadyInCategory_showsMessageWithoutChanges() {
        Person person = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        String expectedMessage = String.format(CategoryCommand.MESSAGE_ALREADY_IN_CATEGORY,
                person.getName(), person.getCategory());
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        assertCommandSuccess(new CategoryCommand(INDEX_FIRST_PERSON, person.getCategory()),
                model, expectedMessage, expectedModel);
        assertFalse(model.canUndoAddressBook());
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        CategoryCommand categoryCommand = new CategoryCommand(outOfBoundIndex, Category.INVESTOR);

        assertCommandFailure(categoryCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }


    @Test
    public void equals() {
        CategoryCommand categorizeFirstAsInvestor = new CategoryCommand(INDEX_FIRST_PERSON, Category.INVESTOR);
        CategoryCommand categorizeSecondAsInvestor = new CategoryCommand(INDEX_SECOND_PERSON, Category.INVESTOR);
        CategoryCommand categorizeFirstAsClient = new CategoryCommand(INDEX_FIRST_PERSON, Category.CLIENT);

        assertTrue(categorizeFirstAsInvestor.equals(categorizeFirstAsInvestor));
        assertFalse(categorizeFirstAsInvestor.equals(1));
        assertFalse(categorizeFirstAsInvestor.equals(null));
        assertFalse(categorizeFirstAsInvestor.equals(categorizeSecondAsInvestor));
        assertFalse(categorizeFirstAsInvestor.equals(categorizeFirstAsClient));
    }

    @Test
    public void toStringMethod() {
        CategoryCommand categoryCommand = new CategoryCommand(INDEX_FIRST_PERSON, Category.INVESTOR);
        String expected = CategoryCommand.class.getCanonicalName()
                + "{targetIndex=" + INDEX_FIRST_PERSON + ", targetCategory=" + Category.INVESTOR + "}";
        assertEquals(expected, categoryCommand.toString());
    }
}
