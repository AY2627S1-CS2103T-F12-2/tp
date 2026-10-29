package seedu.address.ui;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

/**
 * A compact dashboard-specific card for a contact.
 */
public class DashboardPersonCard extends UiPart<Region> {
    private static final String FXML = "DashboardPersonCard.fxml";
    private static final String OVERDUE_STYLE_CLASS = "dashboard-overdue-date";
    private static final String UPCOMING_STYLE_CLASS = "dashboard-upcoming-date";

    @FXML
    private Label id;
    @FXML
    private Label name;
    @FXML
    private Label company;
    @FXML
    private Label role;
    @FXML
    private Label followUpDate;
    @FXML
    private Label category;

    /** Creates a dashboard card for the given contact. */
    public DashboardPersonCard(Person person, int displayedIndex) {
        super(FXML);
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        company.setText("Company: " + person.getCompany().value);
        role.setText("Role: " + person.getRole().value);
        category.setText(person.getCategory().name());

        if (person.getFollowUpDate() == null) {
            followUpDate.setText("Follow-up: not set");
        } else {
            followUpDate.setText("Follow-up: " + person.getFollowUpDate());
            if (isOverdue(person, LocalDate.now())) {
                followUpDate.getStyleClass().add(OVERDUE_STYLE_CLASS);
            } else if (isWithinNextWeek(person, LocalDate.now())) {
                followUpDate.getStyleClass().add(UPCOMING_STYLE_CLASS);
            }
        }
    }

    /** Returns whether the contact's follow-up date is before the supplied date. */
    static boolean isOverdue(Person person, LocalDate today) {
        return person.getFollowUpDate() != null && person.getFollowUpDate().value.isBefore(today);
    }

    /** Returns whether the contact's follow-up date is within the next seven days. */
    static boolean isWithinNextWeek(Person person, LocalDate today) {
        return person.getFollowUpDate() != null
                && !person.getFollowUpDate().value.isBefore(today)
                && !person.getFollowUpDate().value.isAfter(today.plusDays(7));
    }

    /** Returns whether this card's follow-up label has the overdue style. */
    boolean hasOverdueStyle() {
        return followUpDate.getStyleClass().contains(OVERDUE_STYLE_CLASS);
    }

    /** Returns whether this card's follow-up label has the upcoming style. */
    boolean hasUpcomingStyle() {
        return followUpDate.getStyleClass().contains(UPCOMING_STYLE_CLASS);
    }
}
