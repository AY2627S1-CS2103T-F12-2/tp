package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMPANY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;

import java.util.List;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.CompanyContainsKeywordsPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.RoleContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public FindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(trimmedArgs,
                PREFIX_NAME, PREFIX_ROLE, PREFIX_COMPANY);

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            String[] nameKeywords = argMultimap.getValue(PREFIX_NAME).get().split("\\s+");
            return new FindCommand(new NameContainsKeywordsPredicate(List.of(nameKeywords)));
        }

        if (argMultimap.getValue(PREFIX_ROLE).isPresent()) {
            String[] roleKeywords = argMultimap.getValue(PREFIX_ROLE).get().split("\\s+");
            return new FindCommand(new RoleContainsKeywordsPredicate(List.of(roleKeywords)));
        }

        if (argMultimap.getValue(PREFIX_COMPANY).isPresent()) {
            String[] companyKeywords = argMultimap.getValue(PREFIX_COMPANY).get().split("\\s+");
            return new FindCommand(new CompanyContainsKeywordsPredicate(List.of(companyKeywords)));
        }

        String[] keywords = trimmedArgs.split("\\s+");
        return new FindCommand(new NameContainsKeywordsPredicate(List.of(keywords)));
    }

}
