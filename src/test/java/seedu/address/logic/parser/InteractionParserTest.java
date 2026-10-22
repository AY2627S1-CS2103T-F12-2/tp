package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.InteractionCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.InteractionType;

/** Tests parsing of interaction commands. */
public class InteractionParserTest {
    private final InteractionParser parser = new InteractionParser();

    @Test
    public void parse_validInput_createsCommand() throws ParseException {
        InteractionCommand expected = new InteractionCommand(INDEX_FIRST_PERSON,
                "2026-10-20", InteractionType.MEETING, "Discussed proposal");
        assertEquals(expected, parser.parse("1 d/2026-10-20 ty/meeting nt/Discussed proposal"));
    }

    @Test
    public void parse_invalidDate_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("1 d/2026-02-30 ty/MEETING nt/Discussed proposal"));
    }

    @Test
    public void parse_missingNote_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("1 d/2026-10-20 ty/MEETING"));
    }
}
