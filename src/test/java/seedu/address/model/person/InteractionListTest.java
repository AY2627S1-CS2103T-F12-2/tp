package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class InteractionListTest {

    private static final Interaction CALL =
            new Interaction("Alice Pauline", "2026-10-20", InteractionType.CALL, "Discussed proposal");
    private static final Interaction MEETING =
            new Interaction("Bob Ross", "2026-10-21", InteractionType.MEETING, "Project update");

    @Test
    public void constructor_invalidList_throwsException() {
        assertThrows(NullPointerException.class, () -> new InteractionList(null));
        List<Interaction> interactionsWithNull = new ArrayList<>();
        interactionsWithNull.add(CALL);
        interactionsWithNull.add(null);
        assertThrows(IllegalArgumentException.class, () -> new InteractionList(interactionsWithNull));
    }

    @Test
    public void addInteraction_validInteraction_addsInteraction() {
        InteractionList interactionList = new InteractionList(new ArrayList<>());

        interactionList.addInteraction(CALL);

        assertEquals(List.of(CALL), interactionList.getInteractions());
    }

    @Test
    public void equals() {
        InteractionList interactionList = new InteractionList(List.of(CALL));

        assertTrue(interactionList.equals(interactionList));
        assertTrue(interactionList.equals(new InteractionList(List.of(CALL))));
        assertFalse(interactionList.equals(new InteractionList(List.of(MEETING))));
        assertFalse(interactionList.equals(null));
        assertFalse(interactionList.equals(List.of(CALL)));
    }

    @Test
    public void toStringMethod() {
        InteractionList interactionList = new InteractionList(List.of(CALL, MEETING));

        assertEquals(List.of(CALL, MEETING).toString(), interactionList.toString());
    }
}
