package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;

public class AddEmailValidationTest {
    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parse_missingEmailParts_explainsRequiredFormat() {
        String expectedMessage =
                "Invalid email! Please provide a local part, '@', and a valid domain (e.g. name@example.com).";
        for (String email : new String[]{"carol@", "@example.com", "carol.example.com", "carol@@example.com"}) {
            assertThrows(ParseException.class, expectedMessage, () ->
                    parser.parseCommand(commandWithEmail(email)));
        }
    }

    @Test
    public void parse_validNonComDomain_success() {
        assertDoesNotThrow(() -> parser.parseCommand(commandWithEmail("carol+work@example.org")));
    }

    private String commandWithEmail(String email) {
        return "add n/Carol Tan p/81234567 e/" + email + " a/Office rl/Analyst com/ABC Bank";
    }
}
