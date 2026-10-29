package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FOLLOW_UP_DATE;

import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses arguments for {@link SortCommand}. */
public class SortCommandParser implements Parser<SortCommand> {

    @Override
    public SortCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_FOLLOW_UP_DATE);
        if (!argMultimap.getPreamble().isEmpty()
                || argMultimap.getAllValues(PREFIX_FOLLOW_UP_DATE).isEmpty()
                || argMultimap.getAllValues(PREFIX_FOLLOW_UP_DATE).size() > 1) {
            throw invalidFormat();
        }

        String direction = argMultimap.getValue(PREFIX_FOLLOW_UP_DATE).orElse("").trim().toLowerCase();
        if (direction.isEmpty() || direction.equals("asc")) {
            return new SortCommand();
        }
        if (direction.equals("desc")) {
            return new SortCommand(true);
        }
        throw invalidFormat();
    }

    private ParseException invalidFormat() {
        return new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
    }
}
