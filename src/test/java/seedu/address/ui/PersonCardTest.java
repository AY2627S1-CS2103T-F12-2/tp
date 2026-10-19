package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class PersonCardTest {

    @BeforeAll
    public static void initializeJavaFxToolkit() {
        try {
            Platform.startup(() -> { });
            Platform.setImplicitExit(false);
        } catch (IllegalStateException e) {
            // The toolkit may already be initialized by another UI test.
        }
    }

    @Test
    public void constructor_withoutFollowUpDate_loadsPersonCard() {
        Person person = new PersonBuilder().withName("Alice Tan").build();
        PersonCard personCard = new PersonCard(person, 1);

        assertEquals(person, personCard.person);
        assertNotNull(personCard.getRoot());
    }

    @Test
    public void constructor_withFollowUpDate_loadsPersonCard() {
        Person person = new PersonBuilder().withName("Bob Tan")
                .withFollowUpDate("2026-10-20").build();
        PersonCard personCard = new PersonCard(person, 2);

        assertEquals(person, personCard.person);
        assertNotNull(personCard.getRoot());
    }
}
