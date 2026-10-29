package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.InteractionCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Interaction;
import seedu.address.model.person.InteractionType;

/** Tests parsing of interaction commands. */
public class InteractionCommandParserTest {
    private final InteractionCommandParser parser = new InteractionCommandParser();

    @Test
    public void parse_validInput_createsCommand() throws ParseException {
        InteractionCommand expected = new InteractionCommand(INDEX_FIRST_PERSON,
                "2026-10-20", InteractionType.MEETING, "Discussed proposal");
        assertEquals(expected, parser.parse("1 d/2026-10-20 ty/meeting nt/Discussed proposal"));
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
    public void parse_optionalNote_createsCommand() throws ParseException {
        InteractionCommand expected = new InteractionCommand(INDEX_FIRST_PERSON,
                "2026-10-20", InteractionType.MEETING, "");
        assertEquals(expected, parser.parse("1 d/2026-10-20 ty/MEETING"));
        assertEquals(expected, parser.parse("1 d/2026-10-20 ty/MEETING nt/   "));
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
}
