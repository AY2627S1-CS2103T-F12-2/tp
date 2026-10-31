package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INTERACTION_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INTERACTION_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INTERACTION_TYPE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.InteractionCommand;
import seedu.address.model.person.Interaction;
import seedu.address.model.person.InteractionType;

/** Tests parsing of interaction commands. */
public class InteractionCommandParserTest {
    private final InteractionCommandParser parser = new InteractionCommandParser();

    @Test
    public void parse_validInput_createsCommand() {
        InteractionCommand expected = new InteractionCommand(INDEX_FIRST_PERSON,
                "2026-10-20", InteractionType.MEETING, "Discussed proposal");
        assertParseSuccess(parser, "1 d/2026-10-20 ty/meeting nt/Discussed proposal", expected);
    }

    @Test
    public void parse_invalidDate_throwsParseException() {
        assertParseFailure(parser, "1 d/2026-02-30 ty/MEETING nt/Discussed proposal",
                Interaction.MESSAGE_INVALID_DATE);
        assertParseFailure(parser, "1 d/ ty/MEETING nt/Discussed proposal",
                Interaction.MESSAGE_INVALID_DATE);
        assertParseFailure(parser, "1 d/-0001-01-01 ty/MEETING",
                Interaction.MESSAGE_INVALID_DATE);
        assertParseFailure(parser, "1 d/+10000-01-01 ty/MEETING",
                Interaction.MESSAGE_INVALID_DATE);
    }

    @Test
    public void parse_optionalNote_createsCommand() {
        InteractionCommand expected = new InteractionCommand(INDEX_FIRST_PERSON,
                "2026-10-20", InteractionType.MEETING, "");
        assertParseSuccess(parser, "1 d/2026-10-20 ty/MEETING", expected);
        assertParseSuccess(parser, "1 d/2026-10-20 ty/MEETING nt/   ", expected);
    }

    @Test
    public void parse_invalidType_showsAllowedTypes() {
        assertParseFailure(parser, "1 d/2026-10-20 ty/TEXT nt/Hello", InteractionType.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1 d/2026-10-20 ty/ nt/Hello", InteractionType.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_missingDate_showsUsage() {
        assertParseFailure(parser, "1 ty/MEETING",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, InteractionCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingRequiredFields_showsUsage() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, InteractionCommand.MESSAGE_USAGE);

        assertParseFailure(parser, "1 d/2026-10-20", expectedMessage);
        assertParseFailure(parser, "1 nt/Discussed proposal", expectedMessage);
    }

    @Test
    public void parse_duplicatePrefixes_throwsParseException() {
        String validArguments = "1 d/2026-10-20 ty/MEETING nt/Discussed proposal";

        assertParseFailure(parser, validArguments + " d/2026-10-21",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_INTERACTION_DATE));
        assertParseFailure(parser, validArguments + " ty/CALL",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_INTERACTION_TYPE));
        assertParseFailure(parser, validArguments + " nt/Followed up",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_INTERACTION_NOTE));
        assertParseFailure(parser, validArguments + " d/2026-10-21 ty/CALL",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_INTERACTION_DATE, PREFIX_INTERACTION_TYPE));
    }

    @Test
    public void parse_invalidIndex_throwsRelevantParseException() {
        String arguments = " d/2026-10-20 ty/MEETING";
        String usageMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, InteractionCommand.MESSAGE_USAGE);

        assertParseFailure(parser, "0" + arguments, ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "-1" + arguments, ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "2147483648" + arguments, ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "+1" + arguments, usageMessage);
        assertParseFailure(parser, "one" + arguments, usageMessage);
        assertParseFailure(parser, "1 2" + arguments, usageMessage);
    }
}
