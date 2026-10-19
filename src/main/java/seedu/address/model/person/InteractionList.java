package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.ArrayList;
import java.util.List;

/** Represents an interaction list of a person. */
public class InteractionList {
    public static final String MESSAGE_CONSTRAINTS =
            "interaction list should only contain valid interactions";

    private final List<Interaction> interactions;

    /** Creates a list containing the supplied interactions. */
    public InteractionList(List<Interaction> interactions) {
        requireNonNull(interactions);
        for (Interaction interaction : interactions) {
            checkArgument(interaction != null, MESSAGE_CONSTRAINTS);
        }
        this.interactions = new ArrayList<>(interactions);
    }

    /** Appends an interaction to this list. */
    public void addInteraction(Interaction interaction) {
        requireNonNull(interaction);
        interactions.add(interaction);
    }

    public List<Interaction> getInteractions() {
        return List.copyOf(interactions);
    }

    @Override
    public String toString() {
        return interactions.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof InteractionList otherInteractionList)) {
            return false;
        }
        return interactions.equals(otherInteractionList.interactions);
    }

    @Override
    public int hashCode() {
        return interactions.hashCode();
    }
}
