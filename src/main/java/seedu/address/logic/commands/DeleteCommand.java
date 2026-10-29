package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Company;
import seedu.address.model.person.Email;
import seedu.address.model.person.FollowUpDate;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Role;

/**
 * Deletes a person identified using its displayed index from the address book.
 * Partial implementation: supports deleting by role as an additional code path.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person identified by the specified parameter used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer), n/NAME, p/PHONE, e/EMAIL, rl/ROLE, com/COMPANY,\n"
            + "or fd/FOLLOW_UP_DATE\n"
            + "Command does not support partial keywords. All keyword parameters must be complete.\n"
            + "Bulk delete can be used with com/COMPANY and rl/ROLE \n"
            + "Examples: " + COMMAND_WORD + " 1\n"
            + "          " + COMMAND_WORD + " p/98765432\n"
            + "          " + COMMAND_WORD + " e/johnd@example.com\n"
            + "          " + COMMAND_WORD + " com/Example Company\n"
            + "          " + COMMAND_WORD + " fd/2026-10-20";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted person: %1$s";
    public static final String MESSAGE_PHONE_NOT_FOUND = "No person found with phone number: %s";
    public static final String MESSAGE_EMAIL_NOT_FOUND = "No person found with email address: %s";
    public static final String MESSAGE_COMPANY_NOT_FOUND = "No person found with company: %s";
    public static final String MESSAGE_ROLE_NOT_FOUND = "No person found with role: %s";
    public static final String MESSAGE_NAME_NOT_FOUND = "No person found with name: %s";
    public static final String MESSAGE_CONTACT_NOT_IN_COMPANY_SCOPE =
            "This contact is not in the active delete preview.";
    public static final String MESSAGE_COMPANY_CONTACTS_FOUND = "%1$d person(s) found working at %2$s.\n"
            + "To delete one, use delete INDEX, delete p/PHONE, or delete e/EMAIL.\n"
            + "To remove all displayed contacts, enter delete all.\n"
            + "To return, enter list.";
    public static final String MESSAGE_ROLE_CONTACTS_FOUND = "%1$d person(s) found with role %2$s.\n"
            + "To delete one, use delete INDEX, delete p/PHONE, or delete e/EMAIL.\n"
            + "To remove all displayed contacts, enter delete all.\n"
            + "To return, enter list.";
    public static final String MESSAGE_FOLLOW_UP_DATE_NOT_FOUND = "No person found with follow-up date: %s";
    public static final String MESSAGE_DELETE_PERSONS_SUCCESS = "Deleted persons: %1$s";
    public static final String MESSAGE_DELETE_ALL_SUCCESS = "Deleted %d contact(s).";
    public static final String MESSAGE_DELETE_ALL_REQUIRES_COMPANY_PREVIEW =
            "Delete all is only available after a role or company preview.\n"
            + "To clear address book, enter clear.";
    public static final String MESSAGE_NO_CONTACTS_TO_DELETE = "There are no displayed contacts to delete.";

    private final Index targetIndex;
    private final String targetRole;
    private final FollowUpDate targetFollowUpDate;
    private final Phone targetPhone;
    private final Email targetEmail;
    private final Company targetCompany;
    private final Name targetName;
    private final boolean deleteAll;

    public DeleteCommand(Index targetIndex) {
        this(targetIndex, null, null, null, null, null, null, false);
    }

    public DeleteCommand(String targetRole) {
        this(null, targetRole, null, null, null, null, null, false);
    }

    public DeleteCommand(FollowUpDate targetFollowUpDate) {
        this(null, null, targetFollowUpDate, null, null, null, null, false);
    }

    public DeleteCommand(Phone targetPhone) {
        this(null, null, null, targetPhone, null, null, null, false);
    }

    public DeleteCommand(Email targetEmail) {
        this(null, null, null, null, targetEmail, null, null, false);
    }

    /** Previews all contacts at the exact company; this does not delete any contacts. */
    public DeleteCommand(Company targetCompany) {
        this(null, null, null, null, null, targetCompany, null, false);
    }

    public DeleteCommand(Name targetName) {
        this(null, null, null, null, null, null, targetName, false);
    }

    /** Deletes every contact in the currently displayed list. */
    public DeleteCommand() {
        this(null, null, null, null, null, null, null, true);
    }

    private DeleteCommand(Index targetIndex, String targetRole, FollowUpDate targetFollowUpDate,
                          Phone targetPhone, Email targetEmail, Company targetCompany, Name targetName,
                          boolean deleteAll) {
        this.targetIndex = targetIndex;
        this.targetRole = targetRole;
        this.targetFollowUpDate = targetFollowUpDate;
        this.targetPhone = targetPhone;
        this.targetEmail = targetEmail;
        this.targetCompany = targetCompany;
        this.targetName = targetName;
        this.deleteAll = deleteAll;
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
            model.commitAddressBook(COMMAND_WORD);
            String deletedPersons = personsToDelete.stream()
                    .map(Messages::format)
                    .collect(Collectors.joining("\n"));
            return new CommandResult(String.format(MESSAGE_DELETE_PERSONS_SUCCESS, deletedPersons));
        }

        if (deleteAll) {
            if (!model.hasDeleteScope()) {
                throw new CommandException(MESSAGE_DELETE_ALL_REQUIRES_COMPANY_PREVIEW);
            }
            List<Person> personsToDelete = List.copyOf(model.getFilteredPersonList());
            if (personsToDelete.isEmpty()) {
                throw new CommandException(MESSAGE_NO_CONTACTS_TO_DELETE);
            }
            personsToDelete.forEach(model::deletePerson);
            model.clearDeleteScope();
            model.commitAddressBook(COMMAND_WORD);
            return new CommandResult(String.format(MESSAGE_DELETE_ALL_SUCCESS, personsToDelete.size()));
        }

        if (targetPhone != null) {
            Person personToDelete = model.getUnfilteredPersonList().stream()
                    .filter(person -> targetPhone.equals(person.getPhone()))
                    .findFirst()
                    .orElseThrow(() -> new CommandException(String.format(MESSAGE_PHONE_NOT_FOUND, targetPhone)));
            ensureInActiveDeleteScope(model, personToDelete);
            model.deletePerson(personToDelete);
            model.removeFromDeleteScope(personToDelete);
            model.commitAddressBook(COMMAND_WORD);
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));
        }

        if (targetEmail != null) {
            Person personToDelete = model.getUnfilteredPersonList().stream()
                    .filter(person -> targetEmail.equals(person.getEmail()))
                    .findFirst()
                    .orElseThrow(() -> new CommandException(String.format(MESSAGE_EMAIL_NOT_FOUND, targetEmail)));
            ensureInActiveDeleteScope(model, personToDelete);
            model.deletePerson(personToDelete);
            model.removeFromDeleteScope(personToDelete);
            model.commitAddressBook(COMMAND_WORD);
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));
        }

        if (targetCompany != null) {
            List<Person> companyContacts = model.getUnfilteredPersonList().stream()
                    .filter(person -> targetCompany.equals(person.getCompany()))
                    .collect(Collectors.toList());
            model.setDeleteScope(companyContacts);
            model.updateFilteredPersonList(person -> targetCompany.equals(person.getCompany()));
            return new CommandResult(getPreviewMessage(model.getFilteredPersonList().size(),
                    MESSAGE_COMPANY_CONTACTS_FOUND, MESSAGE_COMPANY_NOT_FOUND, targetCompany));
        }

        if (targetName != null) {
            Person personToDelete = model.getUnfilteredPersonList().stream()
                    .filter(person -> targetName.equals(person.getName()))
                    .findFirst()
                    .orElseThrow(() -> new CommandException(String.format(MESSAGE_NAME_NOT_FOUND, targetName)));
            ensureInActiveDeleteScope(model, personToDelete);
            model.deletePerson(personToDelete);
            model.removeFromDeleteScope(personToDelete);
            model.commitAddressBook(COMMAND_WORD);
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));
        }

        if (targetRole != null) {
            Role role = new Role(targetRole);
            List<Person> roleContacts = model.getUnfilteredPersonList().stream()
                    .filter(person -> role.equals(person.getRole()))
                    .collect(Collectors.toList());
            model.setDeleteScope(roleContacts);
            model.updateFilteredPersonList(person -> role.equals(person.getRole()));
            return new CommandResult(getPreviewMessage(model.getFilteredPersonList().size(),
                    MESSAGE_ROLE_CONTACTS_FOUND, MESSAGE_ROLE_NOT_FOUND, role));
        }

        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.deletePerson(personToDelete);
        model.removeFromDeleteScope(personToDelete);
        model.commitAddressBook(COMMAND_WORD);
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));
    }

    private void ensureInActiveDeleteScope(Model model, Person person) throws CommandException {
        if (model.hasDeleteScope() && !model.getDeleteScope().contains(person)) {
            throw new CommandException(MESSAGE_CONTACT_NOT_IN_COMPANY_SCOPE);
        }
    }

    private String getPreviewMessage(int contactCount, String messageFormat, String noMatchFormat, Object criterion) {
        if (contactCount == 0) {
            return String.format(noMatchFormat, criterion);
        }
        return String.format(messageFormat, contactCount, criterion);
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
                && java.util.Objects.equals(targetFollowUpDate, otherDeleteCommand.targetFollowUpDate)
                && java.util.Objects.equals(targetPhone, otherDeleteCommand.targetPhone)
                && java.util.Objects.equals(targetEmail, otherDeleteCommand.targetEmail)
                && java.util.Objects.equals(targetCompany, otherDeleteCommand.targetCompany)
                && java.util.Objects.equals(targetName, otherDeleteCommand.targetName)
                && deleteAll == otherDeleteCommand.deleteAll;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("targetRole", targetRole);
        if (targetFollowUpDate != null) {
            builder.add("targetFollowUpDate", targetFollowUpDate);
        }
        if (targetPhone != null) {
            builder.add("targetPhone", targetPhone);
        }
        if (targetEmail != null) {
            builder.add("targetEmail", targetEmail);
        }
        if (targetCompany != null) {
            builder.add("targetCompany", targetCompany);
        }
        if (targetName != null) {
            builder.add("targetName", targetName);
        }
        if (deleteAll) {
            builder.add("deleteAll", true);
        }
        return builder.toString();
    }
}
