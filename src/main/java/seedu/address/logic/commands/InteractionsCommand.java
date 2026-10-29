package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.stream.Collectors;

import seedu.address.model.Model;
import seedu.address.model.person.Interaction;
import seedu.address.model.person.Person;

/** Lists all recorded interactions. */
public class InteractionsCommand extends Command {
    public static final String COMMAND_WORD = "interactions";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists all recorded interactions.\n"
            + "Example: " + COMMAND_WORD;
    public static final String MESSAGE_SUCCESS = "Listed all interactions:\n%1$s";
    public static final String MESSAGE_NO_INTERACTIONS = "No interactions recorded.";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        String formattedInteractions = model.getUnfilteredPersonList().stream()
                .flatMap((Person person) -> person.getInteractions().stream())
                .map(Interaction::toString)
                .collect(Collectors.joining("\n"));

        if (formattedInteractions.isEmpty()) {
            return new CommandResult(MESSAGE_NO_INTERACTIONS);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, formattedInteractions));
    }
}
