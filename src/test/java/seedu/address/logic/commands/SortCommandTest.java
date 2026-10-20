package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class SortCommandTest {

    private final Person earlyAlice = new PersonBuilder().withName("Alice Tan")
            .withFollowUpDate("2026-10-20").build();
    private final Person earlyBob = new PersonBuilder().withName("Bob Tan")
            .withFollowUpDate("2026-10-20").build();
    private final Person lateCarol = new PersonBuilder().withName("Carol Tan")
            .withFollowUpDate("2026-11-20").build();
    private final Person noDateAaron = new PersonBuilder().withName("Aaron Tan").build();
    private final Person noDateZoe = new PersonBuilder().withName("Zoe Tan").build();

    @Test
    public void execute_ascendingSort_ordersDatesNamesAndMissingDatesLast() {
        Model model = createModel(noDateZoe, lateCarol, earlyBob, noDateAaron, earlyAlice);

        CommandResult result = new SortCommand().execute(model);

        assertEquals("Contacts sorted by follow-up date (ascending).", result.getFeedbackToUser());
        assertEquals(List.of(earlyAlice, earlyBob, lateCarol, noDateAaron, noDateZoe),
                model.getAddressBook().getPersonList());
    }

    @Test
    public void execute_descendingSort_reversesDatesAndKeepsNamesAscendingOnTies() {
        Model model = createModel(noDateZoe, lateCarol, earlyBob, noDateAaron, earlyAlice);

        CommandResult result = new SortCommand(true).execute(model);

        assertEquals("Contacts sorted by follow-up date (descending).", result.getFeedbackToUser());
        assertEquals(List.of(lateCarol, earlyAlice, earlyBob, noDateAaron, noDateZoe),
                model.getAddressBook().getPersonList());
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new SortCommand().execute(null));
    }

    @Test
    public void equals() {
        SortCommand ascending = new SortCommand();
        SortCommand descending = new SortCommand(true);

        assertTrue(ascending.equals(ascending));
        assertTrue(ascending.equals(new SortCommand(false)));
        assertFalse(ascending.equals(descending));
        assertFalse(ascending.equals(null));
        assertFalse(ascending.equals(1));
        assertEquals(ascending.hashCode(), new SortCommand(false).hashCode());
        assertEquals(SortCommand.class.getCanonicalName() + "{descending=true}", descending.toString());
    }

    private Model createModel(Person... persons) {
        AddressBook addressBook = new AddressBook();
        for (Person person : persons) {
            addressBook.addPerson(person);
        }
        return new ModelManager(addressBook, new UserPrefs());
    }
}
