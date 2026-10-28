package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.person.Person;

class ModelTest {

    @Test
    void commitAddressBook_withActionName_delegatesToCommitAddressBook() {
        TestModel model = new TestModel();

        model.commitAddressBook("add contact");

        assertEquals(1, model.commitCount);
    }

    @Test
    void deleteScopeDefaults_returnInactiveEmptyScope() {
        Model model = new TestModel();

        assertDoesNotThrow(() -> model.setDeleteScope(List.of()));
        assertFalse(model.hasDeleteScope());
        assertEquals(List.of(), model.getDeleteScope());
        assertDoesNotThrow(() -> model.removeFromDeleteScope(null));
        assertDoesNotThrow(model::clearDeleteScope);
    }

    private static class TestModel implements Model {
        private int commitCount;

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            return null;
        }

        @Override
        public GuiSettings getGuiSettings() {
            return null;
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook addressBook) {
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return null;
        }

        @Override
        public void commitAddressBook() {
            commitCount++;
        }

        @Override
        public void undoAddressBook() {
        }

        @Override
        public void redoAddressBook() {
        }

        @Override
        public boolean canUndoAddressBook() {
            return false;
        }

        @Override
        public boolean canRedoAddressBook() {
            return false;
        }

        @Override
        public boolean hasPerson(Person person) {
            return false;
        }

        @Override
        public void deletePerson(Person target) {
        }

        @Override
        public void addPerson(Person person) {
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return null;
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
        }

        @Override
        public ObservableList<Person> getUnfilteredPersonList() {
            return null;
        }
    }
}
