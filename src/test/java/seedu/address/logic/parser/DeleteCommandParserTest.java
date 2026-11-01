package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.model.person.Company;
import seedu.address.model.person.Email;
import seedu.address.model.person.FollowUpDate;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the DeleteCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the DeleteCommand, and therefore we test only one of them.
 * The path variation for those two cases occurs inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class DeleteCommandParserTest {

    private DeleteCommandParser parser = new DeleteCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteCommand() {
        assertParseSuccess(parser, "1", new DeleteCommand(INDEX_FIRST_PERSON));
        assertParseSuccess(parser, "all", new DeleteCommand());
        assertParseSuccess(parser, "n/Alice Tan", new DeleteCommand(new Name("Alice Tan")));
        assertParseSuccess(parser, "p/98765432", new DeleteCommand(new Phone("98765432")));
        assertParseSuccess(parser, "e/johnd@example.com", new DeleteCommand(new Email("johnd@example.com")));
        assertParseSuccess(parser, "com/Example Company", new DeleteCommand(new Company("Example Company")));
        assertParseSuccess(parser, "com/Test  Capital", new DeleteCommand(new Company("Test Capital")));
        assertParseSuccess(parser, "rl/Analyst", new DeleteCommand("Analyst"));
        assertParseSuccess(parser, "rl/software  engineer", new DeleteCommand("software engineer"));
        assertParseSuccess(parser, "fd/2026-10-20", new DeleteCommand(new FollowUpDate("2026-10-20")));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "a", expectedMessage);
        assertParseFailure(parser, "1 fd/2026-10-20", expectedMessage);
        assertParseFailure(parser, "p/98765432 e/johnd@example.com", expectedMessage);
        assertParseFailure(parser, "com/Example Company p/98765432", expectedMessage);
        assertParseFailure(parser, "p/not-a-number", Phone.MESSAGE_NUMBERS_ONLY);
        assertParseFailure(parser, "p/1234567", Phone.MESSAGE_LENGTH);
        assertParseFailure(parser, "e/not-an-email", Email.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "fd/2026-02-30", FollowUpDate.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "fd/2026-10-20 fd/2026-10-21",
                Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_FOLLOW_UP_DATE));
    }

    @Test
    public void parse_invalidNumericIndex_throwsIndexError() {
        assertParseFailure(parser, "0", ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "-1", ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "2147483648", ParserUtil.MESSAGE_INVALID_INDEX);
    }
}
