package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BOSS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMPANY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FOLLOW_UP_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.BossContainsKeywordsPredicate;
import seedu.address.model.person.Category;
import seedu.address.model.person.CompanyContainsKeywordsPredicate;
import seedu.address.model.person.EmailContainsKeywordsPredicate;
import seedu.address.model.person.FollowUpDate;
import seedu.address.model.person.FollowUpDatesPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonInCategoriesPredicate;
import seedu.address.model.person.PhoneContainsKeywordsPredicate;
import seedu.address.model.person.RoleContainsKeywordsPredicate;
import seedu.address.model.person.TagContainsKeywordsPredicate;

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
                PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ROLE, PREFIX_COMPANY, PREFIX_BOSS,
                PREFIX_TAG, PREFIX_CATEGORY, PREFIX_FOLLOW_UP_DATE);
        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
        List<Predicate<Person>> predicates = new ArrayList<>();
        List<String> followUpDateValues = getSearchValues(argMultimap, PREFIX_FOLLOW_UP_DATE);
        if (!followUpDateValues.isEmpty()) {
            List<FollowUpDate> followUpDates = new ArrayList<>();
            for (String followUpDateValue : followUpDateValues) {
                followUpDates.add(ParserUtil.parseFollowUpDate(followUpDateValue));
            }
            predicates.add(new FollowUpDatesPredicate(followUpDates));
        }

        List<String> nameKeywords = getSearchValues(argMultimap, PREFIX_NAME);
        if (!nameKeywords.isEmpty()) {
            predicates.add(new NameContainsKeywordsPredicate(nameKeywords));
        }

        List<String> phoneKeywords = getSearchValues(argMultimap, PREFIX_PHONE);
        if (!phoneKeywords.isEmpty()) {
            predicates.add(new PhoneContainsKeywordsPredicate(phoneKeywords));
        }

        List<String> emailKeywords = getSearchValues(argMultimap, PREFIX_EMAIL);
        if (!emailKeywords.isEmpty()) {
            predicates.add(new EmailContainsKeywordsPredicate(emailKeywords));
        }

        List<String> roleKeywords = getSearchValues(argMultimap, PREFIX_ROLE);
        if (!roleKeywords.isEmpty()) {
            predicates.add(new RoleContainsKeywordsPredicate(roleKeywords));
        }

        List<String> companyKeywords = getSearchValues(argMultimap, PREFIX_COMPANY);
        if (!companyKeywords.isEmpty()) {
            predicates.add(new CompanyContainsKeywordsPredicate(companyKeywords));
        }

        List<String> bossKeywords = getSearchValues(argMultimap, PREFIX_BOSS);
        if (!bossKeywords.isEmpty()) {
            predicates.add(new BossContainsKeywordsPredicate(bossKeywords));
        }

        List<String> tagKeywords = getSearchValues(argMultimap, PREFIX_TAG);
        if (!tagKeywords.isEmpty()) {
            predicates.add(new TagContainsKeywordsPredicate(tagKeywords));
        }

        List<String> categoryValues = getSearchValues(argMultimap, PREFIX_CATEGORY);
        if (!categoryValues.isEmpty()) {
            List<Category> categories = new ArrayList<>();
            for (String categoryValue : categoryValues) {
                if (!Category.isValidCategory(categoryValue)) {
                    throw new ParseException(Category.MESSAGE_CONSTRAINTS);
                }
                categories.add(Category.valueOf(categoryValue.toUpperCase(Locale.ROOT)));
            }
            predicates.add(new PersonInCategoriesPredicate(categories));
        }

        return new FindCommand(predicates.stream()
                .reduce(Predicate::and)
                .orElseThrow(() -> new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE))));
    }

    /** Returns validated values for a search prefix. */
    private List<String> getSearchValues(ArgumentMultimap arguments, Prefix prefix) throws ParseException {
        List<String> values = arguments.getAllValues(prefix).stream()
                .map(String::trim)
                .map(value -> value.replaceAll("\\s+", " "))
                .toList();
        for (String value : values) {
            if (value.isBlank()) {
                throw new ParseException(FindCommand.MESSAGE_EMPTY_SEARCH_KEYWORD);
            }
        }
        return values;
    }

}
