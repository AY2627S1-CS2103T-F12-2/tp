package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SortCommand;

public class SortCommandParserTest {

    private final SortCommandParser parser = new SortCommandParser();

    @Test
    public void parse_validArguments_returnsSortCommand() {
        assertParseSuccess(parser, "", new SortCommand());
        assertParseSuccess(parser, "fd/", new SortCommand());
        assertParseSuccess(parser, "fd/ ASC ", new SortCommand());
        assertParseSuccess(parser, "fd/desc", new SortCommand(true));
    }

    @Test
    public void parse_invalidArguments_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "unexpected", expectedMessage);
        assertParseFailure(parser, "fd/up", expectedMessage);
        assertParseFailure(parser, "fd/asc fd/desc", expectedMessage);
    }
}
