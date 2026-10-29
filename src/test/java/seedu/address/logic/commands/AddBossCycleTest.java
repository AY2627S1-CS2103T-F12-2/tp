package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class AddBossCycleTest {

    @Test
    public void execute_selfBoss_rejected() {
        Person person = aliceWithBoss("alice tan");
        assertCycleRejected(person);
    }

    @Test
    public void execute_twoPersonCycle_rejected() {
        Person bob = new PersonBuilder().withName("Bob Lim").withBoss("Alice Tan")
                .withPhone("82345678").withEmail("bob@example.com").build();
        assertCycleRejected(aliceWithBoss("Bob Lim"), bob);
    }

    @Test
    public void execute_indirectCycle_rejected() {
        Person bob = new PersonBuilder().withName("Bob Lim").withBoss("Carol Tan")
                .withPhone("82345678").withEmail("bob@example.com").build();
        Person carol = new PersonBuilder().withName("Carol Tan").withBoss("ALICE TAN")
                .withPhone("83456789").withEmail("carol@example.com").build();
        assertCycleRejected(aliceWithBoss("Bob Lim"), bob, carol);
    }

    @Test
    public void execute_emptyOrExternalBoss_success() throws CommandException {
        for (String boss : new String[]{"", "External Boss"}) {
            Model model = new ModelManager();
            Person person = aliceWithBoss(boss);
            new AddCommand(person).execute(model);
            assertEquals(List.of(person), model.getAddressBook().getPersonList());
            assertTrue(model.canUndoAddressBook());
        }
    }

    @Test
    public void execute_acyclicChain_success() throws CommandException {
        Person bob = new PersonBuilder().withName("Bob Lim").withBoss("")
                .withPhone("82345678").withEmail("bob@example.com").build();
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(bob);
        Model model = new ModelManager(addressBook, new UserPrefs());
        new AddCommand(aliceWithBoss("Bob Lim")).execute(model);
        assertEquals(2, model.getAddressBook().getPersonList().size());
    }

    private void assertCycleRejected(Person candidate, Person... existing) {
        AddressBook addressBook = new AddressBook();
        addressBook.setPersons(List.of(existing));
        Model model = new ModelManager(addressBook, new UserPrefs());
        model.commitAddressBook();
        model.undoAddressBook();
        model.updateFilteredPersonList(person -> false);

        assertThrows(CommandException.class, AddCommand.MESSAGE_BOSS_CYCLE, () ->
                new AddCommand(candidate).execute(model));

        assertEquals(addressBook, model.getAddressBook());
        assertTrue(model.getFilteredPersonList().isEmpty());
        assertTrue(model.canRedoAddressBook());
    }

    private Person aliceWithBoss(String boss) {
        return new PersonBuilder().withName("Alice Tan").withBoss(boss)
                .withPhone("81234567").withEmail("alice@example.com").build();
    }
}
