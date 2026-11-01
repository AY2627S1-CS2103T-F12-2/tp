package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Interaction;
import seedu.address.model.person.Person;

/**
 * Deletes an interaction from the selected person's history.
 */
public class DeleteInteractionCommand extends Command {
    public static final String MESSAGE_SUCCESS = "Deleted interaction %1$s with %2$s.";
    public static final String MESSAGE_INVALID_INTERACTION_INDEX = "The interaction index provided is invalid.";

    private final Index index;
    private final Index interactionIndex;

    /**
     * Creates a command to delete the indexed interaction from the indexed person.
     */
    public DeleteInteractionCommand(Index index, Index interactionIndex) {
        requireAllNonNull(index, interactionIndex);
        this.index = index;
        this.interactionIndex = interactionIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        Objects.requireNonNull(model);

        List<Person> shownPersons = model.getFilteredPersonList();
        if (index.getZeroBased() >= shownPersons.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person original = shownPersons.get(index.getZeroBased());
        List<Interaction> interactions = new ArrayList<>(original.getInteractions());
        if (interactionIndex.getZeroBased() >= interactions.size()) {
            throw new CommandException(MESSAGE_INVALID_INTERACTION_INDEX);
        }

        interactions.remove(interactionIndex.getZeroBased());
        Person updated = original.withInteractions(interactions);
        model.setPerson(original, updated);
        model.commitAddressBook(InteractionCommand.COMMAND_WORD);
        return new CommandResult(String.format(MESSAGE_SUCCESS, interactionIndex.getOneBased(), original.getName()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this || other instanceof DeleteInteractionCommand command
                && index.equals(command.index) && interactionIndex.equals(command.interactionIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, interactionIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("index", index).add("interactionIndex", interactionIndex).toString();
    }
}
