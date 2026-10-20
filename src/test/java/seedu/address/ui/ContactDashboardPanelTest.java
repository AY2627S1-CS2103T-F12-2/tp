package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class ContactDashboardPanelTest {

    @Test
    public void refresh_filtersContactsDueWithinOneWeek() {
        try {
            Platform.startup(() -> { });
        } catch (IllegalStateException e) {
            // JavaFX toolkit already initialized.
        }

        ObservableList<Person> people = FXCollections.observableArrayList();

        people.add(createPerson("Alice Tan", LocalDate.now().plusDays(2)));
        people.add(createPerson("Bob Lee", LocalDate.now().plusDays(10)));
        people.add(createPerson("Charlie Lim", LocalDate.now().minusDays(1)));
        people.add(createPerson("Diana Ong", LocalDate.now().plusDays(7)));
        people.add(createPerson("Eve Wong", null));

        ContactDashboardPanel panel = new ContactDashboardPanel(people, () -> { });
        panel.refresh(people);

        assertNotNull(panel);
        assertEquals(3, panel.getDueSoonPeople().size());
        assertEquals(3, panel.getDueSoonPeople().stream()
                .filter(person -> person.getFollowUpDate() != null
                        && !person.getFollowUpDate().value.isBefore(LocalDate.now().minusDays(7))
                        && !person.getFollowUpDate().value.isAfter(LocalDate.now().plusDays(7)))
                .count());
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
}
