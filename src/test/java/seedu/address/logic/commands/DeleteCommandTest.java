package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Company;
import seedu.address.model.person.Email;
import seedu.address.model.person.FollowUpDate;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Role;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);
        showNoPerson(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validPhone_deletesExactMatchFromUnfilteredList() {
        Person personToDelete = model.getUnfilteredPersonList().get(0);
        showPersonAtIndex(model, INDEX_SECOND_PERSON);
        DeleteCommand deleteCommand = new DeleteCommand(personToDelete.getPhone());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validEmail_deletesExactMatch() {
        Person personToDelete = model.getUnfilteredPersonList().get(0);
        DeleteCommand deleteCommand = new DeleteCommand(personToDelete.getEmail());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_missingPhoneOrEmail_throwsCommandException() {
        assertCommandFailure(new DeleteCommand(new Phone("11111111")), model,
                String.format(DeleteCommand.MESSAGE_PHONE_NOT_FOUND, "11111111"));
        assertCommandFailure(new DeleteCommand(new Email("missing@example.com")), model,
                String.format(DeleteCommand.MESSAGE_EMAIL_NOT_FOUND, "missing@example.com"));
    }

    @Test
    public void execute_companyPreview_displaysExactCompanyContactsWithoutDeleting() {
        Company company = model.getUnfilteredPersonList().get(0).getCompany();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updateFilteredPersonList(person -> company.equals(person.getCompany()));
        String expectedMessage = String.format(DeleteCommand.MESSAGE_COMPANY_CONTACTS_FOUND,
                expectedModel.getFilteredPersonList().size(), company);

        assertCommandSuccess(new DeleteCommand(company), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_missingCompany_displaysNoContacts() {
        Company missingCompany = new Company("Missing Company");
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updateFilteredPersonList(person -> missingCompany.equals(person.getCompany()));

        assertCommandSuccess(new DeleteCommand(missingCompany), model,
                String.format(DeleteCommand.MESSAGE_COMPANY_NOT_FOUND, missingCompany), expectedModel);
    }

    @Test
    public void execute_namePreview_displaysExactNameContactsAndSetsDeleteScope() {
        Name name = model.getUnfilteredPersonList().get(0).getName();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updateFilteredPersonList(person -> name.equals(person.getName()));

        assertCommandSuccess(new DeleteCommand(name), model,
                String.format(DeleteCommand.MESSAGE_NAME_CONTACTS_FOUND,
                        expectedModel.getFilteredPersonList().size(), name), expectedModel);
        assertTrue(model.hasDeleteScope());
        assertEquals(expectedModel.getFilteredPersonList(), model.getDeleteScope());
    }

    @Test
    public void execute_missingName_displaysNoContactsAndSetsEmptyDeleteScope() {
        Name missingName = new Name("Missing Person");
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updateFilteredPersonList(person -> missingName.equals(person.getName()));

        assertCommandSuccess(new DeleteCommand(missingName), model,
                String.format(DeleteCommand.MESSAGE_NAME_NOT_FOUND, missingName), expectedModel);
        assertTrue(model.hasDeleteScope());
        assertTrue(model.getDeleteScope().isEmpty());
    }

    @Test
    public void execute_phoneOutsideCompanyPreview_throwsCommandException() {
        Company company = model.getUnfilteredPersonList().get(0).getCompany();
        Person personOutsideCompany = new PersonBuilder().withName("Outside Company Person")
                .withPhone("81111111").withEmail("outside@example.com").withCompany("Other Capital").build();
        model.addPerson(personOutsideCompany);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updateFilteredPersonList(person -> company.equals(person.getCompany()));

        assertCommandSuccess(new DeleteCommand(company), model,
                String.format(DeleteCommand.MESSAGE_COMPANY_CONTACTS_FOUND,
                        expectedModel.getFilteredPersonList().size(), company), expectedModel);
        assertCommandFailure(new DeleteCommand(personOutsideCompany.getPhone()), model,
                DeleteCommand.MESSAGE_CONTACT_NOT_IN_COMPANY_SCOPE);
    }

    @Test
    public void execute_deleteAllFilteredList_deletesEveryDisplayedContact() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Person personToDelete = model.getFilteredPersonList().get(0);
        model.setDeleteScope(java.util.List.of(personToDelete));
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);
        showNoPerson(expectedModel);

        assertCommandSuccess(new DeleteCommand(), model,
                String.format(DeleteCommand.MESSAGE_DELETE_ALL_SUCCESS, 1), expectedModel);
    }

    @Test
    public void execute_deleteAllWithoutCompanyPreview_throwsCommandException() {
        assertCommandFailure(new DeleteCommand(), model, DeleteCommand.MESSAGE_DELETE_ALL_REQUIRES_COMPANY_PREVIEW);
    }

    @Test
    public void execute_deleteAllWithNoDisplayedContacts_throwsCommandException() {
        model.setDeleteScope(java.util.List.of());
        showNoPerson(model);

        assertCommandFailure(new DeleteCommand(), model, DeleteCommand.MESSAGE_NO_CONTACTS_TO_DELETE);
    }

    @Test
    public void execute_validRoleUnfilteredList_displaysRolePreview() {
        Role targetRole = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()).getRole();
        DeleteCommand deleteCommand = new DeleteCommand(targetRole.value);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updateFilteredPersonList(person -> targetRole.equals(person.getRole()));

        assertCommandSuccess(deleteCommand, model, String.format(DeleteCommand.MESSAGE_ROLE_CONTACTS_FOUND,
                expectedModel.getFilteredPersonList().size(), targetRole), expectedModel);
    }

    @Test
    public void execute_missingRole_displaysNoContacts() {
        String missingRole = "Non Existent Role";
        DeleteCommand deleteCommand = new DeleteCommand(missingRole);
        Role role = new Role(missingRole);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updateFilteredPersonList(person -> role.equals(person.getRole()));

        assertCommandSuccess(deleteCommand, model,
                String.format(DeleteCommand.MESSAGE_ROLE_NOT_FOUND, role), expectedModel);
    }

    @Test
    public void execute_personWithNullRole_displaysNoContacts() {
        Person basePerson = new PersonBuilder().withName("No Role Person").build();
        Person personWithNullRole = new Person(basePerson.getName(), basePerson.getPhone(), basePerson.getEmail(),
                basePerson.getAddress(), basePerson.getTags(), basePerson.getRemark(), basePerson.getCategory(),
                basePerson.getRole(), basePerson.getCompany(), basePerson.getBoss(), basePerson.getFollowUpDate()) {
            @Override
            public Role getRole() {
                return null;
            }
        };
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(personWithNullRole);
        Model modelWithNullRole = new ModelManager(addressBook, new UserPrefs());

        Role unavailableRole = new Role("Unavailable Role");
        Model expectedModel = new ModelManager(addressBook, new UserPrefs());
        expectedModel.updateFilteredPersonList(person -> unavailableRole.equals(person.getRole()));
        assertCommandSuccess(new DeleteCommand("Unavailable Role"), modelWithNullRole,
                String.format(DeleteCommand.MESSAGE_ROLE_NOT_FOUND, unavailableRole), expectedModel);
    }

    @Test
    public void execute_validFollowUpDateUnfilteredList_success() {
        Person firstMatch = new PersonBuilder().withName("Alice Tan").withFollowUpDate("2026-10-20").build();
        Person secondMatch = new PersonBuilder().withName("Bob Tan").withFollowUpDate("2026-10-20").build();
        Person nonMatch = new PersonBuilder().withName("Carol Tan").withFollowUpDate("2026-10-21").build();
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(firstMatch);
        addressBook.addPerson(secondMatch);
        addressBook.addPerson(nonMatch);
        Model actualModel = new ModelManager(addressBook, new UserPrefs());
        Model expectedModel = new ModelManager(addressBook, new UserPrefs());
        expectedModel.deletePerson(firstMatch);
        expectedModel.deletePerson(secondMatch);

        String deletedPersons = Messages.format(firstMatch) + "\n" + Messages.format(secondMatch);
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSONS_SUCCESS, deletedPersons);

        assertCommandSuccess(new DeleteCommand(new FollowUpDate("2026-10-20")), actualModel,
                expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidFollowUpDateUnfilteredList_throwsCommandException() {
        FollowUpDate missingDate = new FollowUpDate("2026-12-31");
        DeleteCommand deleteCommand = new DeleteCommand(missingDate);

        assertCommandFailure(deleteCommand, model,
                String.format(DeleteCommand.MESSAGE_FOLLOW_UP_DATE_NOT_FOUND, missingDate));
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new DeleteCommand(INDEX_FIRST_PERSON);
        DeleteCommand deleteSecondCommand = new DeleteCommand(INDEX_SECOND_PERSON);
        DeleteCommand deleteByRoleFirst = new DeleteCommand("Software Engineer");
        DeleteCommand deleteByRoleSecond = new DeleteCommand("Product Manager");
        DeleteCommand deleteByDateFirst = new DeleteCommand(new FollowUpDate("2026-10-20"));
        DeleteCommand deleteByDateSecond = new DeleteCommand(new FollowUpDate("2026-10-21"));

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(INDEX_FIRST_PERSON);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // same role value -> returns true
        DeleteCommand deleteByRoleFirstCopy = new DeleteCommand("Software Engineer");
        assertTrue(deleteByRoleFirst.equals(deleteByRoleFirstCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));

        // different role -> returns false
        assertFalse(deleteByRoleFirst.equals(deleteByRoleSecond));

        // same follow-up date -> returns true
        assertTrue(deleteByDateFirst.equals(new DeleteCommand(new FollowUpDate("2026-10-20"))));

        // different follow-up date -> returns false
        assertFalse(deleteByDateFirst.equals(deleteByDateSecond));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteCommand deleteCommand = new DeleteCommand(targetIndex);
        String expected = DeleteCommand.class.getCanonicalName()
                + "{targetIndex=" + targetIndex + ", targetRole=null}";
        assertEquals(expected, deleteCommand.toString());

        DeleteCommand deleteByDate = new DeleteCommand(new FollowUpDate("2026-10-20"));
        String expectedDateCommand = DeleteCommand.class.getCanonicalName()
                + "{targetIndex=null, targetRole=null, targetFollowUpDate=2026-10-20}";
        assertEquals(expectedDateCommand, deleteByDate.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }
}
