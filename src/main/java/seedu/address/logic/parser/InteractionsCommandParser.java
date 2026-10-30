package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.InteractionsCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new InteractionsCommand object
 */
public class InteractionsCommandParser implements Parser<InteractionsCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the InteractionsCommand
     * and returns an InteractionsCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public InteractionsCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args);
        String preamble = argMultimap.getPreamble();

        // no index given: list interactions of all contacts
        if (preamble.isEmpty()) {
            return new InteractionsCommand();
        }

        Index index;
        try {
            //get index of target person to show Interactions with
            index = ParserUtil.parseIndex(preamble);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, InteractionsCommand.MESSAGE_USAGE), pe);
        }
        return new InteractionsCommand(index);
    }
}
