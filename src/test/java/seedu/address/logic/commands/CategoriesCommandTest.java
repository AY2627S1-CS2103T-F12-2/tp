package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for CategoryCommand.
 */
public class CategoriesCommandTest {

    private static final String EXPECTED_CATEGORIES =
            "UNCATEGORIZED : 7\n"
                    + "CLIENT : 0\n"
                    + "INVESTOR : 0\n"
                    + "LAWYER : 0\n"
                    + "ACCOUNTANT : 0\n"
                    + "CONSULTANT : 0\n"
                    + "OTHER : 0";

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_listsCategories_success() {
        String expectedMessage = String.format(CategoriesCommand.MESSAGE_SUCCESS, EXPECTED_CATEGORIES);
        assertCommandSuccess(new CategoriesCommand(), model, expectedMessage , expectedModel);
    }

    @Test
    public void execute_listIsFiltered_categoriesShowsEverything() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);

        String expectedMessage = String.format(CategoriesCommand.MESSAGE_SUCCESS, EXPECTED_CATEGORIES);
        assertCommandSuccess(new CategoriesCommand(), model, expectedMessage, expectedModel);
    }
}
