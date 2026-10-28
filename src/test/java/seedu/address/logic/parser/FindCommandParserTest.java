package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
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
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(List.of("Alice", "Bob")));
        assertParseSuccess(parser, "Alice Bob", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Alice \n \t Bob  \t", expectedFindCommand);

        FindCommand expectedRoleFindCommand =
                new FindCommand(new RoleContainsKeywordsPredicate(List.of("Engineer")));
        assertParseSuccess(parser, "rl/Engineer", expectedRoleFindCommand);

        FindCommand expectedCompanyFindCommand =
                new FindCommand(new CompanyContainsKeywordsPredicate(List.of("Google")));
        assertParseSuccess(parser, "com/Google", expectedCompanyFindCommand);

        FindCommand expectedNameFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(List.of("Alice")));
        assertParseSuccess(parser, "n/Alice", expectedNameFindCommand);

        FindCommand expectedFollowUpDateFindCommand =
                new FindCommand(new FollowUpDatePredicate(new FollowUpDate("2026-10-20")));
        assertParseSuccess(parser, "fd/2026-10-20", expectedFollowUpDateFindCommand);
    }

    @Test
    public void parse_invalidFollowUpDate_throwsParseException() {
        assertParseFailure(parser, "fd/2026-02-30", FollowUpDate.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_unsupportedPrefix_throwsParseException() {
        assertParseFailure(parser, "c/CLIENT",
                String.format(FindCommand.MESSAGE_UNSUPPORTED_PREFIX, "c/"));
    }

}
