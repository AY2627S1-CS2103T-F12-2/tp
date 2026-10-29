package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ExportCommand;

public class ExportCommandParserTest {

    private final ExportCommandParser parser = new ExportCommandParser();

    @Test
    public void parse_noArguments_returnsDefaultFileName() {
        assertParseSuccess(parser, "", new ExportCommand(ExportCommandParser.DEFAULT_FILE_NAME));
        assertParseSuccess(parser, "   ", new ExportCommand(ExportCommandParser.DEFAULT_FILE_NAME));
    }

    @Test
    public void parse_validFileName_returnsExportCommand() {
        assertParseSuccess(parser, " fn/clients", new ExportCommand("clients"));
        assertParseSuccess(parser, " fn/  clients  ", new ExportCommand("clients"));
        assertParseSuccess(parser, " fn/Q4_report-2026", new ExportCommand("Q4_report-2026"));
    }

    @Test
    public void parse_fileNameWithCsvExtension_stripsExtension() {
        assertParseSuccess(parser, " fn/clients.csv", new ExportCommand("clients"));
        assertParseSuccess(parser, " fn/clients.CSV", new ExportCommand("clients"));
    }

    @Test
    public void parse_invalidFileName_throwsParseException() {
        String expectedMessage = ExportCommandParser.MESSAGE_INVALID_FILE_NAME;
        assertParseFailure(parser, " fn/", expectedMessage);
        assertParseFailure(parser, " fn/.csv", expectedMessage);
        assertParseFailure(parser, " fn/my clients", expectedMessage);
        assertParseFailure(parser, " fn/../secret", expectedMessage);
        assertParseFailure(parser, " fn/a/b", expectedMessage);
        assertParseFailure(parser, " fn/a:b", expectedMessage);
        assertParseFailure(parser, " fn/clients.txt", expectedMessage);
    }

    @Test
    public void parse_invalidFormat_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "clients", expectedMessage);
        assertParseFailure(parser, " fn/a fn/b", expectedMessage);
    }
}
