package seedu.address.model;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@code AddressBook} that keeps a bounded history of address book states for undo and redo operations.
 */
public class VersionedAddressBook extends AddressBook {

    public static final int MAX_HISTORY_SIZE = 50;

    private final List<ReadOnlyAddressBook> addressBookStateList = new ArrayList<>();
    private int currentStatePointer;

    /**
     * Creates a versioned address book initialized with an empty address book state.
     */
    public VersionedAddressBook() {
        this(new AddressBook());
    }

    /**
     * Creates a versioned address book initialized with a copy of {@code initialState}.
     */
    public VersionedAddressBook(ReadOnlyAddressBook initialState) {
        super(initialState);
        addressBookStateList.add(new AddressBook(initialState));
        currentStatePointer = 0;
    }

    /**
     * Saves the current address book as the latest undoable state.
     * Any states that were available to redo are discarded.
     */
    public void commit() {
        removeStatesAfterCurrentPointer();
        addressBookStateList.add(new AddressBook(this));
        currentStatePointer = addressBookStateList.size() - 1;

        if (addressBookStateList.size() > MAX_HISTORY_SIZE) {
            addressBookStateList.remove(0);
            currentStatePointer--;
        }
    }

    /**
     * Restores the previous saved address book state.
     * Callers must check {@link #canUndo()} before invoking this method.
     */
    public void undo() {
        currentStatePointer--;
        resetData(addressBookStateList.get(currentStatePointer));
    }

    /**
     * Restores the next saved address book state.
     * Callers must check {@link #canRedo()} before invoking this method.
     */
    public void redo() {
        currentStatePointer++;
        resetData(addressBookStateList.get(currentStatePointer));
    }

    /**
     * Returns whether there is a previous saved state to restore.
     */
    public boolean canUndo() {
        return currentStatePointer > 0;
    }

    /**
     * Returns whether there is a later saved state to restore.
     */
    public boolean canRedo() {
        return currentStatePointer < addressBookStateList.size() - 1;
    }

    private void removeStatesAfterCurrentPointer() {
        addressBookStateList.subList(currentStatePointer + 1, addressBookStateList.size()).clear();
    }
}
