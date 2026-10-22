package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Interaction;
import seedu.address.model.person.InteractionType;

public class JsonAdaptedInteractionTest {

    private static final Interaction VALID_INTERACTION =
            new Interaction("Alice Pauline", "2026-10-20", InteractionType.CALL, "Discussed proposal");

    @Test
    public void toModelType_validInteraction_returnsInteraction() throws Exception {
        assertEquals(VALID_INTERACTION, new JsonAdaptedInteraction(VALID_INTERACTION).toModelType());
    }

    @Test
    public void toModelType_invalidDate_throwsIllegalValueException() {
        JsonAdaptedInteraction interaction =
                new JsonAdaptedInteraction("Alice Pauline", "2026-02-30", "CALL", "Discussed proposal");

        assertThrows(IllegalValueException.class, Interaction.MESSAGE_CONSTRAINTS, interaction::toModelType);
    }

    @Test
    public void toModelType_invalidType_throwsIllegalValueException() {
        JsonAdaptedInteraction interaction =
                new JsonAdaptedInteraction("Alice Pauline", "2026-10-20", "TEXT", "Discussed proposal");

        assertThrows(IllegalValueException.class, InteractionType.MESSAGE_CONSTRAINTS, interaction::toModelType);
    }
}
