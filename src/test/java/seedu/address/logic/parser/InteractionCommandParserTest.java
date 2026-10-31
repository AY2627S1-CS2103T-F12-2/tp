package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.getErrorMessageForDuplicatePrefixes;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INTERACTION_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INTERACTION_DELETE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INTERACTION_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INTERACTION_TYPE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteInteractionCommand;
import seedu.address.logic.commands.InteractionCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Interaction;
import seedu.address.model.person.InteractionType;

/** Tests parsing of interaction commands. */
public class InteractionCommandParserTest {
    private final InteractionCommandParser parser = new InteractionCommandParser();

    @Test
    public void parse_deleteInteraction_createsCommand() throws ParseException {
        assertEquals(new DeleteInteractionCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON), parser.parse("1 del/1"));
    }

    @Test
    public void parse_invalidDeletion_throwsParseException() {
        for (String input : new String[] {"1 del/", "1 del/0", "1 del/-1", "1 del/abc", "0 del/1"}) {
            assertThrows(ParseException.class, () -> parser.parse(input));
        }
        assertParseFailure(parser, "1 del/1 del/2",
                getErrorMessageForDuplicatePrefixes(PREFIX_INTERACTION_DELETE));
        for (String field : new String[] {"d/2026-10-20", "ty/MEETING", "nt/"}) {
            assertParseFailure(parser, "1 del/1 " + field, InteractionCommandParser.MESSAGE_MIXED_OPERATIONS);
        }
    }

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
    public void parse_prefixesWithinNote_areInterpretedAsPrefixes() {
        assertParseFailure(parser, "1 d/2026-10-20 ty/MEETING nt/Discuss d/tomorrow",
                getErrorMessageForDuplicatePrefixes(PREFIX_INTERACTION_DATE));
        assertParseFailure(parser, "1 d/2026-10-20 ty/MEETING nt/Discuss ty/CALL",
                getErrorMessageForDuplicatePrefixes(PREFIX_INTERACTION_TYPE));
        assertParseFailure(parser, "1 d/2026-10-20 ty/MEETING nt/Discuss nt/details",
                getErrorMessageForDuplicatePrefixes(PREFIX_INTERACTION_NOTE));
        assertParseFailure(parser, "1 d/2026-10-20 ty/MEETING nt/Discuss del/1",
                InteractionCommandParser.MESSAGE_MIXED_OPERATIONS);
    }

    @Test
    public void parse_noteBeforeRequiredFields_createsCommand() throws ParseException {
        InteractionCommand expected = new InteractionCommand(INDEX_FIRST_PERSON,
                "2026-10-20", InteractionType.MEETING, "Discuss policy");
        assertEquals(expected, parser.parse("1 nt/Discuss policy d/2026-10-20 ty/MEETING"));
        assertEquals(expected, parser.parse("1 d/2026-10-20 nt/Discuss policy ty/MEETING"));
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
