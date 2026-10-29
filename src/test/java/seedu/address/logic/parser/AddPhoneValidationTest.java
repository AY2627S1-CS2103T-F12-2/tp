package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;

public class AddPhoneValidationTest {
    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parse_numericPhoneWithWrongLength_explainsLength() {
        for (String phone : new String[]{"", "1234567", "123456789"}) {
            assertThrows(ParseException.class, "Invalid phone number! Please enter exactly 8 digits.", () ->
                    parser.parseCommand(commandWithPhone(phone)));
        }
    }

    @Test
    public void parse_nonNumericPhone_explainsCharacters() {
        for (String phone : new String[]{"1234abcd", "1234 5678", "+1234567"}) {
            assertThrows(ParseException.class, "Invalid phone number! Please use numbers only.", () ->
                    parser.parseCommand(commandWithPhone(phone)));
        }
    }

    @Test
    public void parse_eightDigitsWithLeadingZero_success() {
        assertDoesNotThrow(() -> parser.parseCommand(commandWithPhone("01234567")));
    }

    private String commandWithPhone(String phone) {
        return "add n/Carol Tan p/" + phone + " e/carol@example.com a/Office rl/Analyst com/ABC Bank";
    }
}
