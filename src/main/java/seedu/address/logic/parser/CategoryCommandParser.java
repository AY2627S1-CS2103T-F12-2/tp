package seedu.address.logic.parser;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.CategoryCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Category;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.*;

public class CategoryCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the CategoryCommand
     * and returns a CategoryCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public CategoryCommand parse(String args) throws ParseException {

        requireNonNull(args);
        //get the arguments
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_CATEGORY);

        Index index;

        try {
            //get value
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, CategoryCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_CATEGORY);

        if (argMultimap.getValue(PREFIX_CATEGORY).isEmpty()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, CategoryCommand.MESSAGE_USAGE));
        }

        String categoryFieldInput = argMultimap.getValue(PREFIX_CATEGORY).get();

        //exception if category is not found within category list
        try {
            Category category = Category.valueOf(categoryFieldInput.toUpperCase());
            return new CategoryCommand(index, category);
        } catch (IllegalArgumentException e) {
            throw new ParseException("Invalid Category");
        }

    }

}
