package seedu.address.logic.parser;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.CategoryCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

import java.util.Optional;

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
            //get index
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

        String category = argMultimap.getValue(PREFIX_CATEGORY).get();

        return new CategoryCommand(index, category);

    }

}
