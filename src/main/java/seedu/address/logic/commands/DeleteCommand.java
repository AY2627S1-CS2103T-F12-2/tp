package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.FollowUpDate;
import seedu.address.model.person.Person;
import seedu.address.model.person.Role;

/**
 * Deletes a person identified using its displayed index from the address book.
 * Partial implementation: supports deleting by role as an additional code path.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer) or fd/FOLLOW_UP_DATE\n"
            + "Examples: " + COMMAND_WORD + " 1\n"
            + "          " + COMMAND_WORD + " fd/2026-10-20";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted person: %1$s";
    public static final String MESSAGE_ROLE_NOT_FOUND = "No person found with role: %s";
    public static final String MESSAGE_FOLLOW_UP_DATE_NOT_FOUND = "No person found with follow-up date: %s";
    public static final String MESSAGE_DELETE_PERSONS_SUCCESS = "Deleted persons: %1$s";

    private final Index targetIndex;
    private final String targetRole;
    private final FollowUpDate targetFollowUpDate;

    public DeleteCommand(Index targetIndex) {
        this(targetIndex, null, null);
    }

    public DeleteCommand(String targetRole) {
        this(null, targetRole, null);
    }

    public DeleteCommand(FollowUpDate targetFollowUpDate) {
        this(null, null, targetFollowUpDate);
    }

    private DeleteCommand(Index targetIndex, String targetRole, FollowUpDate targetFollowUpDate) {
        this.targetIndex = targetIndex;
        this.targetRole = targetRole;
        this.targetFollowUpDate = targetFollowUpDate;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (targetFollowUpDate != null) {
            List<Person> personsToDelete = model.getFilteredPersonList().stream()
                    .filter(person -> targetFollowUpDate.equals(person.getFollowUpDate()))
                    .collect(Collectors.toList());
            if (personsToDelete.isEmpty()) {
                throw new CommandException(String.format(MESSAGE_FOLLOW_UP_DATE_NOT_FOUND, targetFollowUpDate));
            }
            personsToDelete.forEach(model::deletePerson);
            String deletedPersons = personsToDelete.stream()
                    .map(Messages::format)
                    .collect(Collectors.joining("\n"));
            return new CommandResult(String.format(MESSAGE_DELETE_PERSONS_SUCCESS, deletedPersons));
        }

        if (targetRole != null) {
            List<Person> lastShownList = model.getFilteredPersonList();
            Person personToDelete = null;

            for (Person person : lastShownList) {
                if (person.getRole() != null && person.getRole().equals(new Role(targetRole))) {
                    personToDelete = person;
                    break;
                }
            }

            if (personToDelete == null) {
                throw new CommandException(String.format(MESSAGE_ROLE_NOT_FOUND, targetRole));
            }

            model.deletePerson(personToDelete);
            model.commitAddressBook(COMMAND_WORD);
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));
        }

        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.deletePerson(personToDelete);
        model.commitAddressBook(COMMAND_WORD);
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand otherDeleteCommand)) {
            return false;
        }

        return java.util.Objects.equals(targetIndex, otherDeleteCommand.targetIndex)
                && java.util.Objects.equals(targetRole, otherDeleteCommand.targetRole)
                && java.util.Objects.equals(targetFollowUpDate, otherDeleteCommand.targetFollowUpDate);
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("targetRole", targetRole);
        if (targetFollowUpDate != null) {
            builder.add("targetFollowUpDate", targetFollowUpDate);
        }
        return builder.toString();
    }
}
