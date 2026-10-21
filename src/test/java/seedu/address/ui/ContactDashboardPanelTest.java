package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
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

    @BeforeAll
    public static void initJavaFx() {
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

        ObservableList<Person> filtered = ContactDashboardPanel.filterDueSoonPeople(people);

        assertEquals(3, filtered.size());
        assertEquals(3, filtered.stream()
                .filter(person -> person.getFollowUpDate() != null
                        && !person.getFollowUpDate().value.isBefore(LocalDate.now().minusDays(7))
                        && !person.getFollowUpDate().value.isAfter(LocalDate.now().plusDays(7)))
                .count());
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

        assertEquals("Total contacts: 3", totalContactsLabel.getText());
        assertEquals(3, panel.getDueSoonPeople().size());
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
