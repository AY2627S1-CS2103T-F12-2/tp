package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.InteractionsCommand;

public class InteractionsCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, InteractionsCommand.MESSAGE_USAGE);

    private InteractionsCommandParser parser = new InteractionsCommandParser();

    @Test
    public void parse_emptyArg_returnsListAllCommand() {
        assertParseSuccess(parser, "", new InteractionsCommand());
        // whitespace only
        assertParseSuccess(parser, "   \t\n ", new InteractionsCommand());
    }

    @Test
    public void parse_validIndex_returnsIndexedCommand() {
        assertParseSuccess(parser, "1", new InteractionsCommand(INDEX_FIRST_PERSON));
        // surrounding whitespace
        assertParseSuccess(parser, " \n 1 \t", new InteractionsCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_nonNumericIndex_throwsParseException() {
        assertParseFailure(parser, "abc", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "1a", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_nonPositiveIndex_throwsParseException() {
        assertParseFailure(parser, "0", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "-1", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "+1", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_multipleArgs_throwsParseException() {
        assertParseFailure(parser, "1 2", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "1 n/Alice", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_indexOverflow_throwsParseException() {
        assertParseFailure(parser, "2147483648", MESSAGE_INVALID_FORMAT);
    }
}
