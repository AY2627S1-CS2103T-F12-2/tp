package seedu.address.ui;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.person.Category;
import seedu.address.model.person.Person;

/**
 * Dashboard panel that shows contacts with overdue or upcoming follow-up dates.
 */
public class ContactDashboardPanel extends UiPart<Region> {
    private static final String FXML = "ContactDashboardPanel.fxml";

    @FXML
    private Label totalContactsValue;

    @FXML
    private Label followUpCountValue;

    @FXML
    private VBox categoryStatsBox;

    @FXML
    private ListView<Person> peopleListView;

    private final ObservableList<Person> dashboardPeople;

    /**
     * Creates a dashboard panel for contacts that need follow-up attention.
     *
     * @param personList the complete list of contacts to summarize.
     */
    public ContactDashboardPanel(ObservableList<Person> personList) {
        super(FXML);
        dashboardPeople = FXCollections.observableArrayList();
        peopleListView.setItems(dashboardPeople);
        peopleListView.setCellFactory(listView -> new PersonListViewCell());
        refresh(personList);
    }

    /** Compatibility constructor for callers that previously supplied a dashboard back action. */
    @Deprecated
    public ContactDashboardPanel(ObservableList<Person> personList, Runnable ignoredBackAction) {
        this(personList);
    }

    /**
     * Returns contacts with overdue follow-up dates or follow-up dates within the next seven days.
     *
     * @param personList the complete list of contacts.
     * @return contacts requiring follow-up attention.
     */
    public static ObservableList<Person> filterFollowUpPeople(ObservableList<Person> personList) {
        ObservableList<Person> filtered = FXCollections.observableArrayList();
        LocalDate today = LocalDate.now();
        LocalDate oneWeekLater = today.plusDays(7);

        for (Person person : personList) {
            if (person.getFollowUpDate() == null) {
                continue;
            }
            LocalDate dueDate = person.getFollowUpDate().value;
            if (dueDate.isBefore(today) || !dueDate.isAfter(oneWeekLater)) {
                filtered.add(person);
            }
        }
        return filtered;
    }

    /**
     * Compatibility alias for callers using the old helper name.
     *
     * @param personList the complete list of contacts.
     * @return contacts requiring follow-up attention.
     */
    @Deprecated
    public static ObservableList<Person> filterDueSoonPeople(ObservableList<Person> personList) {
        return filterFollowUpPeople(personList);
    }

    /**
     * Refreshes the dashboard with contacts requiring follow-up attention.
     *
     * @param personList the complete list of contacts.
     */
    public void refresh(ObservableList<Person> personList) {
        dashboardPeople.setAll(filterFollowUpPeople(personList));
        totalContactsValue.setText(String.valueOf(personList.size()));
        followUpCountValue.setText(String.valueOf(dashboardPeople.size()));
        updateCategoryStats(personList);
    }

    /** Updates the category count rows shown in the overview. */
    private void updateCategoryStats(ObservableList<Person> personList) {
        Map<Category, Integer> categoryCounts = new EnumMap<>(Category.class);
        for (Category category : Category.values()) {
            categoryCounts.put(category, 0);
        }
        for (Person person : personList) {
            Category category = person.getCategory();
            categoryCounts.put(category, categoryCounts.get(category) + 1);
        }

        categoryStatsBox.getChildren().clear();
        for (Category category : Category.values()) {
            HBox row = new HBox(8);
            row.setAlignment(Pos.CENTER_LEFT);
            row.getStyleClass().add("overview-category-row");

            Label categoryName = new Label(category.name());
            categoryName.getStyleClass().add("overview-category-name");
            HBox.setHgrow(categoryName, Priority.ALWAYS);

            Label categoryCount = new Label(String.valueOf(categoryCounts.get(category)));
            categoryCount.getStyleClass().add("overview-category-count");
            row.getChildren().addAll(categoryName, categoryCount);
            categoryStatsBox.getChildren().add(row);
        }
    }

    /**
     * Returns contacts displayed on the dashboard.
     *
     * @return contacts requiring follow-up attention.
     */
    public ObservableList<Person> getDashboardPeople() {
        return dashboardPeople;
    }

    /**
     * Legacy alias for callers that used the old dashboard list accessor.
     *
     * @return contacts requiring follow-up attention.
     */
    @Deprecated
    public ObservableList<Person> getDueSoonPeople() {
        return dashboardPeople;
    }

    class PersonListViewCell extends javafx.scene.control.ListCell<Person> {
        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new DashboardPersonCard(person, getIndex() + 1).getRoot());
            }
        }
    }
}
