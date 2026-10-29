package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INTERACTION_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INTERACTION_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INTERACTION_TYPE;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Interaction;
import seedu.address.model.person.InteractionType;
import seedu.address.model.person.Person;

/** Adds an interaction to an existing person's history. */
public class InteractionCommand extends Command {
    public static final String COMMAND_WORD = "interaction";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Logs an interaction with the person at INDEX.\n"
            + "Parameters: INDEX " + PREFIX_INTERACTION_DATE + "yyyy-MM-dd "
            + PREFIX_INTERACTION_TYPE + "TYPE [" + PREFIX_INTERACTION_NOTE + "NOTE]\n"
            + "Example: interaction 1 d/2026-10-20 ty/MEETING nt/Discussed proposal";
    public static final String MESSAGE_SUCCESS = "Logged interaction for: %1$s";

    private final Index index;
    private final String date;
    private final InteractionType type;
    private final String note;

    /** Creates a command to log an interaction for the indexed person. */
    public InteractionCommand(Index index, String date, InteractionType type, String note) {
        requireAllNonNull(index, date, type, note);
        this.index = index;
        this.date = date;
        this.type = type;
        this.note = note;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        Objects.requireNonNull(model);
        List<Person> shownPersons = model.getFilteredPersonList();
        if (index.getZeroBased() >= shownPersons.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person original = shownPersons.get(index.getZeroBased());
        if (!Interaction.isValidDate(date)) {
            throw new CommandException(Interaction.MESSAGE_CONSTRAINTS);
        }
        Interaction interaction = new Interaction(original.getName().fullName, date, type, note);
        List<Interaction> history = new ArrayList<>(original.getInteractions());
        history.add(interaction);
        Person updated = new Person(original.getName(), original.getPhone(), original.getEmail(),
                original.getAddress(), original.getTags(), original.getRemark(), original.getCategory(),
                original.getRole(), original.getCompany(), original.getBoss(), original.getFollowUpDate(), history);
        model.setPerson(original, updated);
        return new CommandResult(String.format(MESSAGE_SUCCESS, original.getName()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof InteractionCommand command)) {
            return false;
        }
        return index.equals(command.index) && date.equals(command.date)
                && type == command.type && note.equals(command.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, date, type, note);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("date", date)
                .add("type", type)
                .add("note", note)
                .toString();
    }
}
