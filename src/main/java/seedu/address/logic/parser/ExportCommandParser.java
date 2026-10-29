package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILE_NAME;

import seedu.address.logic.commands.ExportCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses arguments for {@link ExportCommand}. */
public class ExportCommandParser implements Parser<ExportCommand> {

    // Only letters, digits, '-' and '_' are allowed, so the name is safe on every OS
    public static final String FILE_NAME_VALIDATION_REGEX = "[A-Za-z0-9_-]+";
    public static final String DEFAULT_FILE_NAME = "export";
    public static final String MESSAGE_INVALID_FILE_NAME =
            "Invalid file name! Use letters, digits, '-' or '_' only, without '.csv'.";

    @Override
    public ExportCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_FILE_NAME);
        if (!argMultimap.getPreamble().isEmpty()
                || argMultimap.getAllValues(PREFIX_FILE_NAME).size() > 1) {
            throw invalidFormat();
        }

        String fileName = argMultimap.getValue(PREFIX_FILE_NAME).orElse(DEFAULT_FILE_NAME).trim();
        if (!fileName.matches(FILE_NAME_VALIDATION_REGEX)) {
            throw new ParseException(MESSAGE_INVALID_FILE_NAME);
        }
        return new ExportCommand(fileName);
    }

    private ParseException invalidFormat() {
        return new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
    }
}