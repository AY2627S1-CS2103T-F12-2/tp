package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.CategoryCommand;
import seedu.address.model.person.Category;

public class CategoryCommandParserTest {

    private CategoryCommandParser parser = new CategoryCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, CategoryCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArg_throwsParseException() {
        assertParseFailure(parser, "2 c/Yes", Category.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_emptyCategory_throwsParseException() {
        assertParseFailure(parser, "1 c/", Category.MESSAGE_EMPTY_CATEGORY);
        assertParseFailure(parser, "1 c/   ", Category.MESSAGE_EMPTY_CATEGORY);
    }

    @Test
    public void parse_absentPrefix_throwsParseException() {
        assertParseFailure(parser, "1", String.format(MESSAGE_INVALID_COMMAND_FORMAT, CategoryCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidNumericIndex_throwsIndexError() {
        assertParseFailure(parser, "0 c/CLIENT", ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "-1 c/CLIENT", ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "2147483648 c/CLIENT", ParserUtil.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_validArgs_returnsCategoryCommand() {
        CategoryCommand expectedCategoryCommand =
                new CategoryCommand(INDEX_FIRST_PERSON, Category.UNCATEGORIZED);

        assertParseSuccess(parser, "1 c/UNCATEGORIZED", expectedCategoryCommand);
        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n 1 \n \t c/ UNCATEGORIZED  \t", expectedCategoryCommand);
        assertParseSuccess(parser, "1 c/client", new CategoryCommand(INDEX_FIRST_PERSON, Category.CLIENT));
    }
}

