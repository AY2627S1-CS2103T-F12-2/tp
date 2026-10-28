package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Category;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class CategoryCaseVariantTest {
    private final Person first = new PersonBuilder().withName("Alice Tan").build();
    private final Person second = new PersonBuilder(first).withName("alice tan").build();

    @Test
    public void execute_caseVariantNames_updatesSelectedContactAndSupportsHistory() throws CommandException {
        Model model = createModel();
        new CategoryCommand(Index.fromOneBased(2), Category.CLIENT).execute(model);
        assertSelectedContactUpdated(model);

        new UndoCommand().execute(model);
        assertSame(first, model.getAddressBook().getPersonList().get(0));
        assertSame(second, model.getAddressBook().getPersonList().get(1));

        new RedoCommand().execute(model);
        assertSelectedContactUpdated(model);
    }

    @Test
    public void execute_filteredCaseVariant_updatesDisplayedContact() throws CommandException {
        Model model = createModel();
        model.updateFilteredPersonList(person -> person == second);
        new CategoryCommand(Index.fromOneBased(1), Category.CLIENT).execute(model);
        assertSelectedContactUpdated(model);
    }

    private Model createModel() {
        AddressBook addressBook = new AddressBook();
        addressBook.setPersons(List.of(first, second));
        return new ModelManager(addressBook, new UserPrefs());
    }

    private void assertSelectedContactUpdated(Model model) {
        List<Person> people = model.getAddressBook().getPersonList();
        assertEquals(2, people.size());
        assertSame(first, people.get(0));
        assertEquals("alice tan", people.get(1).getName().fullName);
        assertEquals(Category.CLIENT, people.get(1).getCategory());
    }
}
