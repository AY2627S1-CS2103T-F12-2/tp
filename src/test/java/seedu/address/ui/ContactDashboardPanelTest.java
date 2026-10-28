package seedu.address.ui;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Boss;
import seedu.address.model.person.Category;
import seedu.address.model.person.Company;
import seedu.address.model.person.Email;
import seedu.address.model.person.FollowUpDate;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;
import seedu.address.model.person.Role;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.Assert;

public class ContactDashboardPanelTest {

    @BeforeAll
    public static void initJavaFx() {
        Logger.getLogger(MainWindow.class.getName()).setLevel(Level.WARNING);
        Platform.startup(() -> {});
    }

    @Test
    public void refresh_filtersContactsDueWithinOneWeek() {
        ObservableList<Person> people = FXCollections.observableArrayList();

        people.add(createPerson("Alice Tan", LocalDate.now().plusDays(2)));
        people.add(createPerson("Bob Lee", LocalDate.now().plusDays(10)));
        people.add(createPerson("Charlie Lim", LocalDate.now().minusDays(1)));
        people.add(createPerson("Diana Ong", LocalDate.now().plusDays(7)));
        people.add(createPerson("Eve Wong", null));
        people.add(createPerson("Frank Goh", LocalDate.now().minusDays(8)));

        ObservableList<Person> filtered = ContactDashboardPanel.filterDueSoonPeople(people);

        assert filtered.size() == 3;
        assert filtered.stream()
                .filter(person -> person.getFollowUpDate() != null
                        && !person.getFollowUpDate().value.isBefore(LocalDate.now().minusDays(7))
                        && !person.getFollowUpDate().value.isAfter(LocalDate.now().plusDays(7)))
                .count() == 3;
    }

    @Test
    public void refresh_updatesTotalContactsLabel() throws Exception {
        ObservableList<Person> people = FXCollections.observableArrayList();
        people.add(createPerson("Alice Tan", LocalDate.now().plusDays(2)));
        people.add(createPerson("Bob Lee", LocalDate.now().plusDays(10)));
        people.add(createPerson("Charlie Lim", LocalDate.now().minusDays(1)));
        people.add(createPerson("Diana Ong", LocalDate.now().plusDays(7)));
        people.add(createPerson("Eve Wong", null));

        ContactDashboardPanel panel = new ContactDashboardPanel(people, () -> {});
        Label totalContactsLabel = getField(panel, "totalContactsLabel", Label.class);

        assert totalContactsLabel.getText().equals("Total contacts: 3");
        assert panel.getDueSoonPeople().size() == 3;
    }

    @Test
    public void constructor_nullBackActionAndCellFactory_areHandled() throws Exception {
        ObservableList<Person> people = FXCollections.observableArrayList();
        Person person = createPerson("Alice Tan", LocalDate.now());
        people.add(person);

        runOnFxThread(() -> {
            ContactDashboardPanel panel = new ContactDashboardPanel(people, null);
            ListView<Person> peopleListView = getField(panel, "peopleListView", ListView.class);
            ListCell<Person> cell = peopleListView.getCellFactory().call(peopleListView);

            invoke(cell, "updateItem", Person.class, null, boolean.class, true);
            assert cell.getGraphic() == null;
            invoke(cell, "updateItem", Person.class, null, boolean.class, false);
            assert cell.getGraphic() == null;
            invoke(cell, "updateItem", Person.class, person, boolean.class, false);
            assert cell.getGraphic() != null;
        });
    }

    @Test
    public void mainWindow_behaviors_areCovered() throws Exception {
        StubLogic logic = new StubLogic(FXCollections.observableArrayList(),
                new GuiSettings(900, 700, 10, 20));

        runOnFxThread(() -> {
            MainWindow mainWindow = createMainWindow(logic);

            assert mainWindow.getPrimaryStage().getWidth() == 900;
            assert mainWindow.getPrimaryStage().getHeight() == 700;
            assert mainWindow.getPrimaryStage().getX() == 10;
            assert mainWindow.getPrimaryStage().getY() == 20;

            invoke(mainWindow, "setWindowDefaultSize", GuiSettings.class, new GuiSettings());
            mainWindow.fillInnerParts();
            assert mainWindow.getPersonListPanel() != null;
            mainWindow.show();
            assert mainWindow.getPrimaryStage().isShowing();

            invoke(mainWindow, "handleDashboard");

            assert isVisible(mainWindow, "dashboardPlaceholder");
            assert !isVisible(mainWindow, "personListPanelPlaceholder");
            assert !isManaged(mainWindow, "commandBoxPlaceholder");

            ContactDashboardPanel dashboard = getField(mainWindow, "contactDashboardPanel",
                    ContactDashboardPanel.class);
            Button backButton = getField(dashboard, "backButton", Button.class);
            backButton.getOnAction().handle(new ActionEvent(backButton, null));

            assert !isVisible(mainWindow, "dashboardPlaceholder");
            assert isManaged(mainWindow, "personListPanelPlaceholder");

            invoke(mainWindow, "handleToggleDashboard");
            assert isVisible(mainWindow, "dashboardPlaceholder");
            invoke(mainWindow, "handleToggleDashboard");
            assert !isVisible(mainWindow, "dashboardPlaceholder");

            ResultDisplay resultDisplay = getField(mainWindow, "resultDisplay", ResultDisplay.class);
            TextArea resultTextArea = getField(resultDisplay, "resultDisplay", TextArea.class);

            logic.nextResult = new CommandResult("normal result");
            assert invoke(mainWindow, "executeCommand", String.class, "list")
                    .equals(logic.nextResult);
            assert resultTextArea.getText().equals("normal result");

            logic.nextResult = new CommandResult("help result", true, false);
            invoke(mainWindow, "executeCommand", String.class, "help");
            HelpWindow helpWindow = getField(mainWindow, "helpWindow", HelpWindow.class);
            assert helpWindow.isShowing();
            invoke(mainWindow, "executeCommand", String.class, "help again");

            logic.commandException = new CommandException("command failure");
            Assert.assertThrows(CommandException.class, () ->
                    invoke(mainWindow, "executeCommand", String.class, "bad command"));
            assert resultTextArea.getText().equals("command failure");

            logic.parseException = new ParseException("parse failure");
            Assert.assertThrows(ParseException.class, () ->
                    invoke(mainWindow, "executeCommand", String.class, "bad syntax"));
            assert resultTextArea.getText().equals("parse failure");

            logic.nextResult = new CommandResult("exit result", false, true);
            invoke(mainWindow, "executeCommand", String.class, "exit");
            assert !mainWindow.getPrimaryStage().isShowing();

            MenuItem helpMenuItem = getField(mainWindow, "helpMenuItem", MenuItem.class);
            AtomicBoolean actionInvoked = new AtomicBoolean(false);
            helpMenuItem.setOnAction(event -> actionInvoked.set(true));

            KeyEvent matchingKey = keyPressed(KeyCode.F1);
            Event.fireEvent(resultTextArea, matchingKey);
            assert actionInvoked.get();
            assert !helpWindow.isShowing();

            KeyEvent nonMatchingKey = keyPressed(KeyCode.F2);
            Event.fireEvent(resultTextArea, nonMatchingKey);
            assert !nonMatchingKey.isConsumed();
            assert !helpWindow.isShowing();

            closeWindow(mainWindow);
        });
    }

    private static MainWindow createMainWindow(Logic logic) {
        return new MainWindow(new Stage(), logic, Path.of("build/test-addressbook.json"));
    }

    private static KeyEvent keyPressed(KeyCode keyCode) {
        return new KeyEvent(KeyEvent.KEY_PRESSED, keyCode.getChar(), keyCode.getChar(), keyCode,
                false, false, false, false);
    }

    private static boolean isVisible(MainWindow mainWindow, String fieldName) throws Exception {
        return getField(mainWindow, fieldName, javafx.scene.layout.StackPane.class).isVisible();
    }

    private static boolean isManaged(MainWindow mainWindow, String fieldName) throws Exception {
        return getField(mainWindow, fieldName, javafx.scene.layout.StackPane.class).isManaged();
    }

    private static void closeWindow(MainWindow mainWindow) throws Exception {
        HelpWindow helpWindow = getField(mainWindow, "helpWindow", HelpWindow.class);
        helpWindow.hide();
        mainWindow.getPrimaryStage().hide();
    }

    private static Object invoke(Object target, String methodName, Class<?> parameterType, Object argument)
            throws Exception {
        Method method = target.getClass().getDeclaredMethod(methodName, parameterType);
        method.setAccessible(true);
        try {
            return method.invoke(target, argument);
        } catch (InvocationTargetException exception) {
            throw (Exception) exception.getCause();
        }
    }

    private static Object invoke(Object target, String methodName, Class<?> firstParameterType, Object firstArgument,
            Class<?> secondParameterType, Object secondArgument) throws Exception {
        Method method = target.getClass().getDeclaredMethod(methodName, firstParameterType, secondParameterType);
        method.setAccessible(true);
        try {
            return method.invoke(target, firstArgument, secondArgument);
        } catch (InvocationTargetException exception) {
            throw (Exception) exception.getCause();
        }
    }

    private static void invoke(Object target, String methodName) throws Exception {
        Method method = target.getClass().getDeclaredMethod(methodName);
        method.setAccessible(true);
        try {
            method.invoke(target);
        } catch (InvocationTargetException exception) {
            throw (Exception) exception.getCause();
        }
    }

    private static void runOnFxThread(ThrowingRunnable action) throws Exception {
        if (Platform.isFxApplicationThread()) {
            action.run();
            return;
        }

        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> failure = new AtomicReference<>();
        Platform.runLater(() -> {
            try {
                action.run();
            } catch (Throwable throwable) {
                failure.set(throwable);
            } finally {
                latch.countDown();
            }
        });
        assert latch.await(30, TimeUnit.SECONDS);
        Throwable throwable = failure.get();
        if (throwable instanceof Exception exception) {
            throw exception;
        }
        if (throwable instanceof Error error) {
            throw error;
        }
        if (throwable != null) {
            throw new RuntimeException(throwable);
        }
    }

    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws Exception;
    }

    private static class StubLogic implements Logic {
        private final ObservableList<Person> people;
        private GuiSettings guiSettings;
        private GuiSettings savedGuiSettings;
        private CommandResult nextResult = new CommandResult("result");
        private CommandException commandException;
        private ParseException parseException;

        StubLogic(ObservableList<Person> people, GuiSettings guiSettings) {
            this.people = people;
            this.guiSettings = guiSettings;
        }

        @Override
        public CommandResult execute(String commandText) throws CommandException, ParseException {
            if (commandException != null) {
                CommandException exception = commandException;
                commandException = null;
                throw exception;
            }
            if (parseException != null) {
                ParseException exception = parseException;
                parseException = null;
                throw exception;
            }
            return nextResult;
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return people;
        }

        @Override
        public GuiSettings getGuiSettings() {
            return guiSettings;
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            savedGuiSettings = guiSettings;
            this.guiSettings = guiSettings;
        }
    }

    private Person createPerson(String name, LocalDate followUpDate) {
        return new Person(
                new Name(name),
                new Phone("12345678"),
                new Email("test@example.com"),
                new Address("123 Test Street"),
                Set.of(new Tag("friend")),
                new Remark(""),
                Category.CLIENT,
                new Role("role"),
                new Company("company"),
                new Boss("Boss Tan"),
                followUpDate == null ? null : new FollowUpDate(followUpDate.toString())
        );
    }

    @SuppressWarnings("unchecked")
    private static <T> T getField(Object target, String fieldName, Class<T> type) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(target);
    }
}
