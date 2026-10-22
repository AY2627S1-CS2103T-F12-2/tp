package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

class VersionedAddressBookTest {

    @Test
    void initialState_cannotUndoOrRedo() {
        VersionedAddressBook addressBook = new VersionedAddressBook();

        assertFalse(addressBook.canUndo());
        assertFalse(addressBook.canRedo());
    }

    @Test
    void commit_thenUndo_restoresPreviousState() {
        VersionedAddressBook addressBook = new VersionedAddressBook();
        addressBook.addPerson(ALICE);
        addressBook.commit();

        addressBook.undo();

        assertEquals(0, addressBook.getPersonList().size());
        assertFalse(addressBook.canUndo());
        assertTrue(addressBook.canRedo());
    }

    @Test
    void undo_thenRedo_restoresLaterState() {
        VersionedAddressBook addressBook = new VersionedAddressBook();
        addressBook.addPerson(ALICE);
        addressBook.commit();
        addressBook.undo();

        addressBook.redo();

        assertEquals(1, addressBook.getPersonList().size());
        assertEquals(ALICE, addressBook.getPersonList().get(0));
        assertTrue(addressBook.canUndo());
        assertFalse(addressBook.canRedo());
    }

    @Test
    void commitAfterUndo_discardsRedoStates() {
        VersionedAddressBook addressBook = new VersionedAddressBook();
        addressBook.addPerson(ALICE);
        addressBook.commit();
        addressBook.addPerson(BENSON);
        addressBook.commit();
        addressBook.undo();

        addressBook.addPerson(BENSON);
        addressBook.commit();

        assertFalse(addressBook.canRedo());
        assertEquals(2, addressBook.getPersonList().size());
    }

    @Test
    void commit_whenHistoryExceedsLimit_discardsOldestStates() {
        VersionedAddressBook addressBook = new VersionedAddressBook();
        for (int i = 0; i <= VersionedAddressBook.MAX_HISTORY_SIZE; i++) {
            addressBook.addPerson(new PersonBuilder().withName("Person " + "A".repeat(i + 1)).build());
            addressBook.commit();
        }

        for (int i = 0; i < VersionedAddressBook.MAX_HISTORY_SIZE - 1; i++) {
            addressBook.undo();
        }

        assertEquals(2, addressBook.getPersonList().size());
        assertFalse(addressBook.canUndo());
    }
}
