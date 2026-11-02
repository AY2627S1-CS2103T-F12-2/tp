package seedu.address;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.Storage;
import seedu.address.storage.StorageManager;

public class MainAppTest {

    @TempDir
    public Path testFolder;

    @Test
    public void initModelManager_invalidJson_createsEmptyAddressBookFile() throws Exception {
        Path addressBookPath = testFolder.resolve("addressbook.json");
        Files.writeString(addressBookPath, "not valid json");
        Path existingBackupPath = testFolder.resolve("addressbook.json.corrupted");
        Files.writeString(existingBackupPath, "previous backup");
        Storage storage = createStorage(addressBookPath);

        Model model = invokeInitModelManager(new MainApp(), storage);

        assertTrue(model.getUnfilteredPersonList().isEmpty());
        assertEmptyAddressBookFile(storage);
        Path backupPath = testFolder.resolve("addressbook.json.corrupted.1");
        assertTrue(Files.exists(backupPath));
        assertEquals("not valid json", Files.readString(backupPath));
        assertEquals("previous backup", Files.readString(existingBackupPath));
    }

    @Test
    public void initModelManager_nullPersonsList_createsEmptyAddressBookFile() throws Exception {
        Path addressBookPath = testFolder.resolve("addressbook.json");
        Files.writeString(addressBookPath, "{}");
        Storage storage = createStorage(addressBookPath);

        Model model = invokeInitModelManager(new MainApp(), storage);

        assertTrue(model.getUnfilteredPersonList().isEmpty());
        assertEmptyAddressBookFile(storage);
        Path backupPath = testFolder.resolve("addressbook.json.corrupted");
        assertTrue(Files.exists(backupPath));
        assertEquals("{}", Files.readString(backupPath));
    }

    @Test
    public void initModelManager_replacementFails_reportsFailure() throws Exception {
        Path addressBookPath = testFolder.resolve("addressbook.json");
        Files.writeString(addressBookPath, "not valid json");
        Storage storage = new Storage() {
            @Override
            public Path getUserPrefsFilePath() {
                return testFolder.resolve("preferences.json");
            }

            @Override
            public Optional<UserPrefs> readUserPrefs() {
                return Optional.empty();
            }

            @Override
            public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) {
                // No preference storage is needed for this test.
            }

            @Override
            public Path getAddressBookFilePath() {
                return addressBookPath;
            }

            @Override
            public Optional<ReadOnlyAddressBook> readAddressBook() {
                throw new NullPointerException("invalid address book data");
            }

            @Override
            public void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
                throw new IOException("address book is not writable");
            }
        };
        MainApp mainApp = new MainApp();

        Model model = invokeInitModelManager(mainApp, storage);

        assertTrue(model.getUnfilteredPersonList().isEmpty());
        assertEquals("Error: The address book file was corrupted or contained invalid data. "
                + "The app started with an empty address book, but could not create a new file.",
                getRecoveryMessage(mainApp));
        assertTrue(Files.exists(addressBookPath));
        assertEquals("not valid json", Files.readString(addressBookPath));
        assertFalse(Files.exists(testFolder.resolve("addressbook.json.corrupted")));
    }

    @Test
    public void initModelManager_replacementAndRestoreFail_preservesBackup() throws Exception {
        Path addressBookPath = testFolder.resolve("addressbook.json");
        Files.writeString(addressBookPath, "not valid json");
        Storage storage = new Storage() {
            @Override
            public Path getUserPrefsFilePath() {
                return testFolder.resolve("preferences.json");
            }

            @Override
            public Optional<UserPrefs> readUserPrefs() {
                return Optional.empty();
            }

            @Override
            public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) {
                // No preference storage is needed for this test.
            }

            @Override
            public Path getAddressBookFilePath() {
                return addressBookPath;
            }

            @Override
            public Optional<ReadOnlyAddressBook> readAddressBook() {
                throw new NullPointerException("invalid address book data");
            }

            @Override
            public void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
                Files.createDirectory(addressBookPath);
                Files.writeString(addressBookPath.resolve("partial-write"), "partial data");
                throw new IOException("address book is not writable");
            }
        };
        MainApp mainApp = new MainApp();

        Model model = invokeInitModelManager(mainApp, storage);

        assertTrue(model.getUnfilteredPersonList().isEmpty());
        assertTrue(Files.exists(testFolder.resolve("addressbook.json.corrupted")));
        assertEquals("not valid json", Files.readString(testFolder.resolve("addressbook.json.corrupted")));
    }

    @Test
    public void initModelManager_backupFails_preservesInvalidFile() throws Exception {
        Path dataFolder = testFolder.resolve("readOnlyData");
        Files.createDirectory(dataFolder);
        Path addressBookPath = dataFolder.resolve("addressbook.json");
        Files.writeString(addressBookPath, "not valid json");

        assumeTrue(Files.getFileAttributeView(dataFolder, PosixFileAttributeView.class) != null,
                "POSIX permissions are required for this test");
        Set<PosixFilePermission> originalPermissions = Files.getPosixFilePermissions(dataFolder);
        try {
            Files.setPosixFilePermissions(dataFolder,
                    Set.of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_EXECUTE));
            MainApp mainApp = new MainApp();
            Model model = invokeInitModelManager(mainApp, createStorage(addressBookPath));

            assertTrue(model.getUnfilteredPersonList().isEmpty());
            assertEquals("Error: The address book file was corrupted or contained invalid data. "
                    + "The invalid file was preserved, but a new file could not be created.",
                    getRecoveryMessage(mainApp));
            assertEquals("not valid json", Files.readString(addressBookPath));
        } finally {
            Files.setPosixFilePermissions(dataFolder, originalPermissions);
        }
    }

    private Storage createStorage(Path addressBookPath) {
        return new StorageManager(new JsonAddressBookStorage(addressBookPath),
                new JsonUserPrefsStorage(testFolder.resolve("preferences.json")));
    }

    private void assertEmptyAddressBookFile(Storage storage) throws DataLoadingException {
        Optional<ReadOnlyAddressBook> readAddressBook = storage.readAddressBook();
        assertTrue(readAddressBook.isPresent());
        assertTrue(readAddressBook.get().getPersonList().isEmpty());
    }

    private Model invokeInitModelManager(MainApp mainApp, Storage storage) throws Exception {
        Method method = MainApp.class.getDeclaredMethod("initModelManager", Storage.class,
                ReadOnlyUserPrefs.class);
        method.setAccessible(true);
        try {
            return (Model) method.invoke(mainApp, storage, new UserPrefs());
        } catch (InvocationTargetException exception) {
            throw (Exception) exception.getCause();
        }
    }

    private String getRecoveryMessage(MainApp mainApp) throws ReflectiveOperationException {
        Field field = MainApp.class.getDeclaredField("addressBookRecoveryMessage");
        field.setAccessible(true);
        return (String) field.get(mainApp);
    }
}
