package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Category;
import seedu.address.model.person.Remark;
import seedu.address.testutil.EditPersonDescriptorBuilder;

/**
 * Verifies that every data-changing command records one address book history state.
 */
public class HistoryCommitCommandTest {

    @Test
    public void delete_successfulExecution_commitsOnce() throws Exception {
        CommitTrackingModel model = modelWithTypicalPersons();

        new DeleteCommand(INDEX_FIRST_PERSON).execute(model);

        assertEquals(1, model.commitCount);
    }

    @Test
    public void edit_successfulExecution_commitsOnce() throws Exception {
        CommitTrackingModel model = modelWithTypicalPersons();
        EditCommand.EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withPhone("81112222").build();

        new EditCommand(INDEX_FIRST_PERSON, descriptor).execute(model);

        assertEquals(1, model.commitCount);
    }

    @Test
    public void clear_successfulExecution_commitsOnce() throws Exception {
        CommitTrackingModel model = modelWithTypicalPersons();

        new ClearCommand().execute(model);

        assertEquals(1, model.commitCount);
    }

    @Test
    public void remark_successfulExecution_commitsOnce() throws Exception {
        CommitTrackingModel model = modelWithTypicalPersons();

        new RemarkCommand(INDEX_FIRST_PERSON, new Remark("Prefers email communication.")).execute(model);

        assertEquals(1, model.commitCount);
    }

    @Test
    public void category_successfulExecution_commitsOnce() throws Exception {
        CommitTrackingModel model = modelWithTypicalPersons();

        new CategoryCommand(INDEX_FIRST_PERSON, Category.CLIENT).execute(model);

        assertEquals(1, model.commitCount);
    }

    private CommitTrackingModel modelWithTypicalPersons() {
        return new CommitTrackingModel();
    }

    private static class CommitTrackingModel extends ModelManager {
        private int commitCount;

        CommitTrackingModel() {
            super(getTypicalAddressBook(), new UserPrefs());
        }

        @Override
        public void commitAddressBook() {
            super.commitAddressBook();
            commitCount++;
        }

        @Override
        public void commitAddressBook(String actionName) {
            super.commitAddressBook(actionName);
            commitCount++;
        }
    }
}
