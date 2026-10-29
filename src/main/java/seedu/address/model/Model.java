package seedu.address.model;

import java.util.List;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.person.Person;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluates to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Saves the current address book state for a future undo operation.
     */
    void commitAddressBook();

    /**
     * Saves the current address book state with a safe name for the action that created it.
     *
     * @param actionName non-sensitive action name for history logging
     */
    default void commitAddressBook(String actionName) {
        commitAddressBook();
    }

    /**
     * Restores the previous address book state.
     */
    void undoAddressBook();

    /**
     * Restores the next address book state.
     */
    void redoAddressBook();

    /**
     * Returns whether an older address book state is available.
     */
    boolean canUndoAddressBook();

    /**
     * Returns whether a newer address book state is available.
     */
    boolean canRedoAddressBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    void setPerson(Person target, Person editedPerson);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Returns an unmodifiable view of the unfiltered person list.
     */
    ObservableList<Person> getUnfilteredPersonList();

    /** Records the contacts shown by the active delete preview. */
    default void setDeleteScope(List<Person> persons) {}

    /** Returns whether a delete preview is active. */
    default boolean hasDeleteScope() {
        return false;
    }

    /** Returns the contacts in the active delete preview. */
    default List<Person> getDeleteScope() {
        return List.of();
    }

    /** Removes a deleted contact from the active delete preview. */
    default void removeFromDeleteScope(Person person) {}

    /** Clears the active delete preview. */
    default void clearDeleteScope() {}
}
