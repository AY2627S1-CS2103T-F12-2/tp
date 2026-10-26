package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BOB;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.BossContainsKeywordsPredicate;
import seedu.address.model.person.Category;
import seedu.address.model.person.CompanyContainsKeywordsPredicate;
import seedu.address.model.person.EmailContainsKeywordsPredicate;
import seedu.address.model.person.FollowUpDate;
import seedu.address.model.person.FollowUpDatesPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.PersonInCategoriesPredicate;
import seedu.address.model.person.PhoneContainsKeywordsPredicate;
import seedu.address.model.person.RoleContainsKeywordsPredicate;
import seedu.address.model.person.TagContainsKeywordsPredicate;

public class FindCommandParserTest {

    private final FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        FindCommand expectedRoleFindCommand =
                new FindCommand(new RoleContainsKeywordsPredicate(List.of("Engineer", "Manager")));
        assertParseSuccess(parser, "rl/Engineer rl/Manager", expectedRoleFindCommand);

        FindCommand expectedCompanyFindCommand =
                new FindCommand(new CompanyContainsKeywordsPredicate(List.of("Google", "Acme")));
        assertParseSuccess(parser, "com/Google com/Acme", expectedCompanyFindCommand);

        FindCommand expectedPhoneFindCommand =
                new FindCommand(new PhoneContainsKeywordsPredicate(List.of("1234", "5678")));
        assertParseSuccess(parser, "p/1234 p/5678", expectedPhoneFindCommand);

        FindCommand expectedEmailFindCommand =
                new FindCommand(new EmailContainsKeywordsPredicate(List.of("example", "work")));
        assertParseSuccess(parser, "e/example e/work", expectedEmailFindCommand);

        FindCommand expectedBossFindCommand =
                new FindCommand(new BossContainsKeywordsPredicate(List.of("Alice", "Bob")));
        assertParseSuccess(parser, "b/Alice b/Bob", expectedBossFindCommand);

        FindCommand expectedTagFindCommand =
                new FindCommand(new TagContainsKeywordsPredicate(List.of("friend", "important")));
        assertParseSuccess(parser, "t/friend t/important", expectedTagFindCommand);

        FindCommand expectedCategoryFindCommand =
                new FindCommand(new PersonInCategoriesPredicate(List.of(Category.CLIENT, Category.INVESTOR)));
        assertParseSuccess(parser, "c/client c/investor", expectedCategoryFindCommand);

        FindCommand expectedNameFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(List.of("Alice Bob")));
        assertParseSuccess(parser, "n/Alice Bob", expectedNameFindCommand);

        FindCommand expectedFollowUpDateFindCommand =
                new FindCommand(new FollowUpDatesPredicate(List.of(
                        new FollowUpDate("2026-10-20"), new FollowUpDate("2026-11-20"))));
        assertParseSuccess(parser, "fd/2026-10-20 fd/2026-11-20", expectedFollowUpDateFindCommand);
    }

    @Test
    public void parse_invalidFollowUpDate_throwsParseException() {
        assertParseFailure(parser, "fd/2026-02-30", FollowUpDate.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidCategory_throwsParseException() {
        assertParseFailure(parser, "c/invalid", Category.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "c/", FindCommand.MESSAGE_EMPTY_SEARCH_KEYWORD);
        assertParseFailure(parser, "c/client investor", Category.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_chainedDistinctPrefixes_filtersUsingAnd() throws Exception {
        FindCommand command = parser.parse("n/Amy p/111 e/amy com/Capital b/Alice t/friend c/uncategorized");
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(AMY);
        Model model = new ModelManager(addressBook, new UserPrefs());

        command.execute(model);

        assertEquals(List.of(AMY), model.getFilteredPersonList());
    }

    @Test
    public void parse_repeatedPrefix_filtersUsingOr() throws Exception {
        FindCommand command = parser.parse("n/Amy n/Bob");
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(AMY);
        addressBook.addPerson(BOB);
        Model model = new ModelManager(addressBook, new UserPrefs());

        command.execute(model);

        assertEquals(List.of(AMY, BOB), model.getFilteredPersonList());
    }

    @Test
    public void parse_missingPrefix_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "Alice Bob", expectedMessage);
        assertParseFailure(parser, "Alice n/Bob", expectedMessage);
    }

    @Test
    public void parse_multiWordSearchValue_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(List.of("Amy Rivera")));
        assertParseSuccess(parser, "n/Amy Rivera", expectedFindCommand);
    }

    @Test
    public void parse_emptySearchKeyword_throwsParseException() {
        assertParseFailure(parser, "n/", FindCommand.MESSAGE_EMPTY_SEARCH_KEYWORD);
        assertParseFailure(parser, "p/", FindCommand.MESSAGE_EMPTY_SEARCH_KEYWORD);
        assertParseFailure(parser, "e/", FindCommand.MESSAGE_EMPTY_SEARCH_KEYWORD);
        assertParseFailure(parser, "rl/", FindCommand.MESSAGE_EMPTY_SEARCH_KEYWORD);
        assertParseFailure(parser, "com/", FindCommand.MESSAGE_EMPTY_SEARCH_KEYWORD);
        assertParseFailure(parser, "b/", FindCommand.MESSAGE_EMPTY_SEARCH_KEYWORD);
        assertParseFailure(parser, "t/", FindCommand.MESSAGE_EMPTY_SEARCH_KEYWORD);
    }

}
