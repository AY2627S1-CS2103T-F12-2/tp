package seedu.address.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
/**
 * An {@code AddressBook} that keeps a bounded history of address book states for undo and redo operations.
 */
public class VersionedAddressBook extends AddressBook {

    public static final int MAX_HISTORY_SIZE = 50;
    private static final String INITIAL_ACTION_NAME = "initial state";
    private static final String UNSPECIFIED_ACTION_NAME = "unspecified action";
    private static final Logger logger = LogsCenter.getLogger(VersionedAddressBook.class);

    private final List<HistoryEntry> addressBookStateList = new ArrayList<>();
    private int currentStatePointer;
    private long nextVersionNumber = 1;

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
        addressBookStateList.add(new HistoryEntry(new AddressBook(initialState), INITIAL_ACTION_NAME, 0));
        currentStatePointer = 0;
    }

    /**
     * Saves the current address book as the latest undoable state.
     * Any states that were available to redo are discarded.
     */
    public void commit() {
        commit(UNSPECIFIED_ACTION_NAME);
    }

    /**
     * Saves the current address book as the latest undoable state and records the action that created it.
     * Any states that were available to redo are discarded.
     *
     * @param actionName safe, non-sensitive name of the action that changed the address book
     */
    public void commit(String actionName) {
        int discardedRedoStates = removeStatesAfterCurrentPointer();
        if (discardedRedoStates > 0) {
            logger.info("History branch discarded: action=" + actionName
                    + ", discardedRedoStates=" + discardedRedoStates);
        }

        HistoryEntry newEntry = new HistoryEntry(new AddressBook(this), actionName, nextVersionNumber++);
        addressBookStateList.add(newEntry);
        currentStatePointer = addressBookStateList.size() - 1;

        if (addressBookStateList.size() > MAX_HISTORY_SIZE) {
            addressBookStateList.remove(0);
            currentStatePointer--;
            logger.warning("History limit reached: discardedOldestState=true, retainedStates=" + MAX_HISTORY_SIZE);
        }

        logger.info("History commit: action=" + actionName + ", version=" + newEntry.versionNumber
                + ", retainedStates=" + addressBookStateList.size() + ", maxStates=" + MAX_HISTORY_SIZE);
    }

    /**
     * Restores the previous saved address book state.
     * Callers must check {@link #canUndo()} before invoking this method.
     */
    public void undo() {
        HistoryEntry currentEntry = addressBookStateList.get(currentStatePointer);
        currentStatePointer--;
        HistoryEntry restoredEntry = addressBookStateList.get(currentStatePointer);
        resetData(restoredEntry.state);
        logger.info("History undo: undoneAction=" + currentEntry.actionName + ", fromVersion="
                + currentEntry.versionNumber + ", toVersion=" + restoredEntry.versionNumber);
    }

    /**
     * Restores the next saved address book state.
     * Callers must check {@link #canRedo()} before invoking this method.
     */
    public void redo() {
        HistoryEntry previousEntry = addressBookStateList.get(currentStatePointer);
        currentStatePointer++;
        HistoryEntry restoredEntry = addressBookStateList.get(currentStatePointer);
        resetData(restoredEntry.state);
        logger.info("History redo: redoneAction=" + restoredEntry.actionName + ", fromVersion="
                + previousEntry.versionNumber + ", toVersion=" + restoredEntry.versionNumber);
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

    private int removeStatesAfterCurrentPointer() {
        int numberOfStatesToRemove = addressBookStateList.size() - currentStatePointer - 1;
        addressBookStateList.subList(currentStatePointer + 1, addressBookStateList.size()).clear();
        return numberOfStatesToRemove;
    }

    /**
     * A saved address book snapshot and the safe action name that produced it.
     */
    private static class HistoryEntry {
        private final ReadOnlyAddressBook state;
        private final String actionName;
        private final long versionNumber;

        HistoryEntry(ReadOnlyAddressBook state, String actionName, long versionNumber) {
            this.state = state;
            this.actionName = actionName;
            this.versionNumber = versionNumber;
        }
    }
}
