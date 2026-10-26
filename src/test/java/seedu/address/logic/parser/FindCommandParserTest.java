package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMPANY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FOLLOW_UP_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalPersons.AMY;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.FindCommand;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.CompanyContainsKeywordsPredicate;
import seedu.address.model.person.FollowUpDate;
import seedu.address.model.person.FollowUpDatePredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.RoleContainsKeywordsPredicate;

public class FindCommandParserTest {

    private final FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        FindCommand expectedRoleFindCommand =
                new FindCommand(new RoleContainsKeywordsPredicate(List.of("Engineer")));
        assertParseSuccess(parser, "rl/Engineer", expectedRoleFindCommand);

        FindCommand expectedCompanyFindCommand =
                new FindCommand(new CompanyContainsKeywordsPredicate(List.of("Google")));
        assertParseSuccess(parser, "com/Google", expectedCompanyFindCommand);

        FindCommand expectedNameFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(List.of("Alice", "Bob")));
        assertParseSuccess(parser, "n/Alice Bob", expectedNameFindCommand);

        FindCommand expectedFollowUpDateFindCommand =
                new FindCommand(new FollowUpDatePredicate(new FollowUpDate("2026-10-20")));
        assertParseSuccess(parser, "fd/2026-10-20", expectedFollowUpDateFindCommand);
    }

    @Test
    public void parse_invalidFollowUpDate_throwsParseException() {
        assertParseFailure(parser, "fd/2026-02-30", FollowUpDate.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_chainedDistinctPrefixes_filtersUsingAnd() throws Exception {
        FindCommand command = parser.parse("n/Amy com/Capital");
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(AMY);
        Model model = new ModelManager(addressBook, new UserPrefs());

        command.execute(model);

        assertEquals(List.of(AMY), model.getFilteredPersonList());
    }

    @Test
    public void parse_missingPrefix_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "Alice Bob", expectedMessage);
        assertParseFailure(parser, "Alice n/Bob", expectedMessage);
    }

    @Test
    public void parse_repeatedPrefix_throwsParseException() {
        assertParseFailure(parser, "n/Charlotte n/David",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));
        assertParseFailure(parser, "rl/Engineer rl/Manager",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ROLE));
        assertParseFailure(parser, "com/Acme com/Globex",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_COMPANY));
        assertParseFailure(parser, "fd/2026-10-20 fd/2026-11-20",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_FOLLOW_UP_DATE));
    }

    @Test
    public void parse_multipleRepeatedPrefixes_throwsParseException() {
        assertParseFailure(parser, "n/Charlotte rl/Engineer n/David rl/Manager",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME, PREFIX_ROLE));
        assertParseFailure(parser, "n/Charlotte rl/Engineer com/Acme fd/2026-10-20 "
                        + "n/David rl/Manager com/Globex fd/2026-11-20",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME, PREFIX_ROLE, PREFIX_COMPANY,
                        PREFIX_FOLLOW_UP_DATE));
    }

}
