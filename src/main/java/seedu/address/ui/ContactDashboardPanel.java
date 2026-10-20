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

    public void refresh(ObservableList<Person> personList) {
        LocalDate today = LocalDate.now();
        LocalDate oneWeekAgo = today.minusDays(7);
        LocalDate oneWeekLater = today.plusDays(7);

        dueSoonPeople.clear();
        for (Person person : personList) {
            if (person.getFollowUpDate() == null) {
                continue;
            }
            LocalDate dueDate = person.getFollowUpDate().value;
            if (!dueDate.isBefore(oneWeekAgo) && !dueDate.isAfter(oneWeekLater)) {
                dueSoonPeople.add(person);
            }
        }
        totalContactsLabel.setText("Total contacts: " + dueSoonPeople.size());
    }

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
