package seedu.address.ui;

import java.time.LocalDate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;

/**
 * Dashboard panel that shows the total number of contacts and a separate list of people.
 */
public class ContactDashboardPanel extends UiPart<Region> {
    private static final String FXML = "ContactDashboardPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(ContactDashboardPanel.class);

    @FXML
    private Button backButton;

    @FXML
    private Label totalContactsLabel;

    @FXML
    private ListView<Person> peopleListView;

    private final ObservableList<Person> dueSoonPeople;

    /**
     * Creates a dashboard panel for the due-soon contacts.
     *
     * @param personList the full list of people to filter.
     * @param backAction action to run when returning to the main CLI view.
     */
    public ContactDashboardPanel(ObservableList<Person> personList, Runnable backAction) {
        super(FXML);
        dueSoonPeople = FXCollections.observableArrayList();
        peopleListView.setItems(dueSoonPeople);
        peopleListView.setCellFactory(listView -> new PersonListViewCell());
        refresh(personList);

        if (backAction != null) {
            backButton.setOnAction(event -> backAction.run());
        }
    }

    /**
     * Filters people whose follow-up date is within the last seven days or the next seven days.
     *
     * @param personList the current list of people.
     * @return a filtered observable list containing only relevant contacts.
     */
    public static ObservableList<Person> filterDueSoonPeople(ObservableList<Person> personList) {
        ObservableList<Person> filtered = FXCollections.observableArrayList();
        LocalDate today = LocalDate.now();
        LocalDate oneWeekAgo = today.minusDays(7);
        LocalDate oneWeekLater = today.plusDays(7);

        for (Person person : personList) {
            if (person.getFollowUpDate() == null) {
                continue;
            }
            LocalDate dueDate = person.getFollowUpDate().value;
            if (!dueDate.isBefore(oneWeekAgo) && !dueDate.isAfter(oneWeekLater)) {
                filtered.add(person);
            }
        }
        return filtered;
    }

    /**
     * Refreshes the dashboard with contacts whose follow-up dates are within the past or next seven days.
     *
     * @param personList the current list of people to filter.
     */
    public void refresh(ObservableList<Person> personList) {
        dueSoonPeople.clear();
        dueSoonPeople.addAll(filterDueSoonPeople(personList));
        totalContactsLabel.setText("Total contacts: " + dueSoonPeople.size());
    }

    /**
     * Returns the filtered list of due-soon contacts.
     *
     * @return the filtered list of due-soon contacts.
     */
    public ObservableList<Person> getDueSoonPeople() {
        return dueSoonPeople;
    }

    class PersonListViewCell extends javafx.scene.control.ListCell<Person> {
        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new PersonCard(person, getIndex() + 1).getRoot());
            }
        }
    }
}
