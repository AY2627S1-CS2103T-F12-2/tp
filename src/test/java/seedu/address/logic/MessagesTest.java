package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Category;
import seedu.address.testutil.PersonBuilder;

public class MessagesTest {

    @Test
    public void format_personWithAndWithoutFollowUpDate_includesExpectedFields() {
        String withoutDate = Messages.format(new PersonBuilder().withName("Alice Tan").build());
        String withDate = Messages.format(new PersonBuilder().withName("Bob Tan")
                .withFollowUpDate("2026-10-20").build());

        assertTrue(withoutDate.contains("Category: UNCATEGORIZED"));
        assertTrue(withDate.contains("Follow-up date: 2026-10-20"));
        assertTrue(withDate.contains("Category: UNCATEGORIZED"));
    }

    @Test
    public void formatEditedPerson_withAndWithoutFollowUpDate_includesExpectedFields() {
        String withoutDate = Messages.formatEditedPerson(new PersonBuilder().withName("Alice Tan")
                .withAddress("321 Clementi Ave 2")
                .withTags("client", "priority")
                .withRemark("Prefers email")
                .withCategory(Category.CLIENT)
                .build());
        String withDate = Messages.formatEditedPerson(new PersonBuilder().withName("Bob Tan")
                .withFollowUpDate("2026-10-20").build());

        assertTrue(withoutDate.contains("Boss: Jane Smith"));
        assertTrue(withoutDate.contains("Address: 321 Clementi Ave 2"));
        assertTrue(withoutDate.contains("Tags: "));
        assertTrue(withoutDate.contains("[client]"));
        assertTrue(withoutDate.contains("[priority]"));
        assertTrue(withoutDate.contains("Remark: Prefers email"));
        assertTrue(withoutDate.contains("Category: CLIENT"));
        assertTrue(withoutDate.contains("Follow-up date: not set"));
        assertTrue(withoutDate.contains("Interactions: []"));
        assertTrue(withDate.contains("Follow-up date: 2026-10-20"));
    }
}
