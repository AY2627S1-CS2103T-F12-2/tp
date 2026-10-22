package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.model.person.FollowUpDate;

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
        assertParseSuccess(parser, "fd/2026-10-20", new DeleteCommand(new FollowUpDate("2026-10-20")));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "a", expectedMessage);
        assertParseFailure(parser, "1 fd/2026-10-20", expectedMessage);
        assertParseFailure(parser, "fd/2026-02-30", FollowUpDate.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "fd/2026-10-20 fd/2026-10-21",
                Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_FOLLOW_UP_DATE));
    }
}
