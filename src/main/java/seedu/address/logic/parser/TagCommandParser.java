package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.RemarkCommand;
import seedu.address.logic.commands.TagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Remark;
import seedu.address.model.tag.Tag;

import java.util.Set;

/**
 * Parses input arguments and creates a {@code TagCommand}.
 */
public class TagCommandParser implements Parser {
    /**
     * Parses the given arguments and returns a {@code TagCommand}.
     *
     * @param args the user input arguments
     * @return a tag command
     * @throws ParseException if the user input does not conform to the expected format
     */
    public TagCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args,
                PREFIX_REMARK);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    TagCommand.MESSAGE_USAGE), pe);
        }

        Set<Tag> tagsToAdd = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        return new TagCommand(index, tagsToAdd);
    }

}
