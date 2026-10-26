package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMPANY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FOLLOW_UP_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.CompanyContainsKeywordsPredicate;
import seedu.address.model.person.FollowUpDate;
import seedu.address.model.person.FollowUpDatePredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
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
                PREFIX_NAME, PREFIX_ROLE, PREFIX_COMPANY, PREFIX_FOLLOW_UP_DATE);
        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_ROLE, PREFIX_COMPANY, PREFIX_FOLLOW_UP_DATE);

        List<Predicate<Person>> predicates = new ArrayList<>();
        if (argMultimap.getValue(PREFIX_FOLLOW_UP_DATE).isPresent()) {
            FollowUpDate followUpDate = ParserUtil.parseFollowUpDate(
                    argMultimap.getValue(PREFIX_FOLLOW_UP_DATE).get());
            predicates.add(new FollowUpDatePredicate(followUpDate));
        }

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            String[] nameKeywords = argMultimap.getValue(PREFIX_NAME).get().split("\\s+");
            predicates.add(new NameContainsKeywordsPredicate(List.of(nameKeywords)));
        }

        if (argMultimap.getValue(PREFIX_ROLE).isPresent()) {
            String[] roleKeywords = argMultimap.getValue(PREFIX_ROLE).get().split("\\s+");
            predicates.add(new RoleContainsKeywordsPredicate(List.of(roleKeywords)));
        }

        if (argMultimap.getValue(PREFIX_COMPANY).isPresent()) {
            String[] companyKeywords = argMultimap.getValue(PREFIX_COMPANY).get().split("\\s+");
            predicates.add(new CompanyContainsKeywordsPredicate(List.of(companyKeywords)));
        }

        return new FindCommand(predicates.stream()
                .reduce(Predicate::and)
                .orElseThrow(() -> new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE))));
    }

}
