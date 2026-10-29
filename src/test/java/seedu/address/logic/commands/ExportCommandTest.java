package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Category;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class ExportCommandTest {

    private static final String HEADER =
            "Name,Phone,Email,Address,Role,Company,Boss,Follow-up date,Category,Tags,Remark";

    @TempDir
    public Path testFolder;

    private final Person amy = new PersonBuilder().withName("Amy Rose").withPhone("91234567")
            .withEmail("amy@ex.com").withAddress("Blk 1, Clementi Rd").withFollowUpDate("2026-11-05")
            .withTags("vip", "client").withCategory(Category.INVESTOR).withRemark("Likes golf").build();
    private final Person ben = new PersonBuilder().withName("Ben Tan").withPhone("91234568")
            .withEmail("ben@ex.com").withAddress("Blk 2").build();

    @Test
    public void constructor_nullArguments_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ExportCommand(null));
        assertThrows(NullPointerException.class, () -> new ExportCommand("clients", null));
    }

    @Test
    public void execute_displayedContacts_writesCsvFile() throws Exception {
        Model model = createModel(amy, ben);
        Path expectedPath = testFolder.resolve("clients.csv");

        CommandResult result = new ExportCommand("clients", testFolder).execute(model);

        assertEquals(String.format(ExportCommand.MESSAGE_SUCCESS, 2, expectedPath.toAbsolutePath()),
                result.getFeedbackToUser());
        assertEquals(List.of(
                HEADER,
                "Amy Rose,91234567,amy@ex.com,\"Blk 1, Clementi Rd\",Software Engineer,Acme Corporation,"
                        + "Jane Smith,2026-11-05,INVESTOR,client;vip,Likes golf",
                "Ben Tan,91234568,ben@ex.com,Blk 2,Software Engineer,Acme Corporation,"
                        + "Jane Smith,,UNCATEGORIZED,,"),
                Files.readAllLines(expectedPath));
    }

    @Test
    public void execute_filteredList_exportsOnlyDisplayedContacts() throws Exception {
        Model model = createModel(amy, ben);
        model.updateFilteredPersonList(person -> person.equals(ben));

        CommandResult result = new ExportCommand("filtered", testFolder).execute(model);

        Path expectedPath = testFolder.resolve("filtered.csv");
        assertEquals(String.format(ExportCommand.MESSAGE_SUCCESS, 1, expectedPath.toAbsolutePath()),
                result.getFeedbackToUser());
        List<String> lines = Files.readAllLines(expectedPath);
        assertEquals(2, lines.size());
        assertTrue(lines.get(1).startsWith("Ben Tan,"));
    }

    @Test
    public void execute_emptyList_writesHeaderOnly() throws Exception {
        Model model = createModel();

        CommandResult result = new ExportCommand("empty", testFolder).execute(model);

        Path expectedPath = testFolder.resolve("empty.csv");
        assertEquals(String.format(ExportCommand.MESSAGE_SUCCESS, 0, expectedPath.toAbsolutePath()),
                result.getFeedbackToUser());
        assertEquals(List.of(HEADER), Files.readAllLines(expectedPath));
    }

    @Test
    public void execute_existingFile_overwritesFile() throws Exception {
        Path existingFile = testFolder.resolve("clients.csv");
        Files.writeString(existingFile, "old,content\nmore,old,content\n");
        Model model = createModel(ben);

        new ExportCommand("clients", testFolder).execute(model);

        List<String> lines = Files.readAllLines(existingFile);
        assertEquals(HEADER, lines.get(0));
        assertEquals(2, lines.size());
    }

    @Test
    public void execute_doesNotModifyModelOrUndoHistory() throws Exception {
        Model model = createModel(amy, ben);
        AddressBook expectedAddressBook = new AddressBook(model.getAddressBook());

        new ExportCommand("clients", testFolder).execute(model);

        assertEquals(expectedAddressBook, model.getAddressBook());
        assertFalse(model.canUndoAddressBook());
    }

    @Test
    public void execute_unwritableLocation_throwsCommandException() throws IOException {
        // A regular file cannot be used as the export folder, so writing inside it fails
        Path notAFolder = testFolder.resolve("not-a-folder");
        Files.writeString(notAFolder, "");
        Model model = createModel(amy);

        CommandException exception = assertThrows(CommandException.class, () ->
                new ExportCommand("clients", notAFolder).execute(model));
        assertTrue(exception.getMessage().startsWith("Could not write export file"));
    }

    @Test
    public void equals() {
        ExportCommand clientsCommand = new ExportCommand("clients");

        // same object -> returns true
        assertTrue(clientsCommand.equals(clientsCommand));

        // same values -> returns true
        assertTrue(clientsCommand.equals(new ExportCommand("clients")));
        assertEquals(clientsCommand.hashCode(), new ExportCommand("clients").hashCode());

        // different file name -> returns false
        assertFalse(clientsCommand.equals(new ExportCommand("investors")));

        // different folder -> returns false
        assertFalse(clientsCommand.equals(new ExportCommand("clients", testFolder)));

        // null or different type -> returns false
        assertFalse(clientsCommand.equals(null));
        assertFalse(clientsCommand.equals(1));
    }

    @Test
    public void toStringMethod() {
        String expected = ExportCommand.class.getCanonicalName() + "{fileName=clients.csv}";
        assertEquals(expected, new ExportCommand("clients").toString());
    }

    private Model createModel(Person... persons) {
        AddressBook addressBook = new AddressBook();
        for (Person person : persons) {
            addressBook.addPerson(person);
        }
        return new ModelManager(addressBook, new UserPrefs());
    }
}
