package seedu.address;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.util.SampleDataUtil;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.Storage;
import seedu.address.storage.StorageManager;
import seedu.address.ui.Ui;
import seedu.address.ui.UiManager;

/**
 * Runs the application.
 */
public class MainApp extends Application {

    public static final String VERSION = "V1.4";

    private static final Logger logger = LogsCenter.getLogger(MainApp.class);
    private static final Path USER_PREFS_FILE_PATH = Paths.get("preferences.json");
    private static final Path ADDRESS_BOOK_FILE_PATH = Paths.get("data", "addressbook.json");

    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;

    private String addressBookRecoveryMessage;

    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing AddressBook ]===========================");
        super.init();

        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(USER_PREFS_FILE_PATH);
        UserPrefs userPrefs = initPrefs(userPrefsStorage);
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(ADDRESS_BOOK_FILE_PATH);
        storage = new StorageManager(addressBookStorage, userPrefsStorage);

        model = initModelManager(storage, userPrefs);

        logic = new LogicManager(model, storage);

        ui = new UiManager(logic, storage.getAddressBookFilePath(), addressBookRecoveryMessage);
    }

    /**
     * Returns a {@code ModelManager} with the data from {@code storage}'s address book and {@code userPrefs}. <br>
     * The data from the sample address book will be used instead if {@code storage}'s address book is not found,
     * or a new empty address book file will be created if loading fails.
     */
    private Model initModelManager(Storage storage, ReadOnlyUserPrefs userPrefs) {
        logger.info("Using data file : " + storage.getAddressBookFilePath());

        Optional<ReadOnlyAddressBook> addressBookOptional;
        ReadOnlyAddressBook initialData;
        try {
            addressBookOptional = storage.readAddressBook();
            if (addressBookOptional.isEmpty()) {
                logger.info("Creating a new data file " + storage.getAddressBookFilePath()
                        + " populated with a sample AddressBook.");
            }
            initialData = addressBookOptional.orElseGet(SampleDataUtil::getSampleAddressBook);
        } catch (DataLoadingException | NullPointerException e) {
            logger.warning("Data file at " + storage.getAddressBookFilePath() + " could not be loaded."
                    + " Will be starting with an empty AddressBook.");
            initialData = new AddressBook();
            addressBookRecoveryMessage = resetCorruptedAddressBook(storage);
        }

        return new ModelManager(initialData, userPrefs);
    }

    /**
     * Replaces invalid address book data with a fresh empty address book.
     *
     * @return a message suitable for displaying to the user after startup
     */
    private String resetCorruptedAddressBook(Storage storage) {
        Path filePath = storage.getAddressBookFilePath();
        Path backupPath;

        try {
            backupPath = backupCorruptedAddressBook(filePath);
        } catch (IOException e) {
            logger.warning("Could not preserve corrupted data file: " + StringUtil.getDetails(e));
            return "Error: The address book file was corrupted or contained invalid data. "
                    + "The invalid file was preserved, but a new file could not be created.";
        }

        try {
            storage.saveAddressBook(new AddressBook());
            String message = "Error: The address book file was corrupted or contained invalid data. "
                    + "A new empty address book has been created.";
            if (backupPath != null) {
                message += " The invalid file was preserved as " + backupPath + ".";
            }
            return message;
        } catch (IOException e) {
            logger.warning("Could not create a new address book file: " + StringUtil.getDetails(e));
            restoreCorruptedAddressBook(filePath, backupPath);
            return "Error: The address book file was corrupted or contained invalid data. "
                    + "The app started with an empty address book, but could not create a new file.";
        }
    }

    /**
     * Moves the corrupted file to a unique backup path before it is replaced.
     */
    private Path backupCorruptedAddressBook(Path filePath) throws IOException {
        if (!Files.exists(filePath)) {
            return null;
        }

        Path backupPath = filePath.resolveSibling(filePath.getFileName() + ".corrupted");
        int suffix = 1;
        while (Files.exists(backupPath)) {
            backupPath = filePath.resolveSibling(filePath.getFileName() + ".corrupted." + suffix);
            suffix++;
        }
        Files.move(filePath, backupPath);
        return backupPath;
    }

    /**
     * Restores the corrupted file if creating its replacement fails.
     */
    private void restoreCorruptedAddressBook(Path filePath, Path backupPath) {
        if (backupPath == null || !Files.exists(backupPath)) {
            return;
        }

        try {
            Files.move(backupPath, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.warning("Could not restore corrupted data file: " + StringUtil.getDetails(e));
        }
    }

    /**
     * Returns a {@code UserPrefs} using the file at {@code storage}'s user prefs file path,
     * or a new {@code UserPrefs} with default configuration if errors occur when
     * reading from the file.
     */
    protected UserPrefs initPrefs(JsonUserPrefsStorage storage) {
        Path prefsFilePath = storage.getUserPrefsFilePath();
        logger.info("Using preference file : " + prefsFilePath);

        UserPrefs initializedPrefs;
        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            if (prefsOptional.isEmpty()) {
                logger.info("Creating new preference file " + prefsFilePath);
            }
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataLoadingException e) {
            logger.warning("Preference file at " + prefsFilePath + " could not be loaded."
                    + " Using default preferences.");
            initializedPrefs = new UserPrefs();
        }

        //Update prefs file in case it was missing to begin with or there are new/unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException e) {
            logger.warning("Failed to save preference file : " + StringUtil.getDetails(e));
        }

        return initializedPrefs;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting AddressBook " + MainApp.VERSION);
        ui.start(primaryStage);
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping AddressBook ] =============================");
        try {
            storage.saveUserPrefs(model.getUserPrefs());
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
    }
}
