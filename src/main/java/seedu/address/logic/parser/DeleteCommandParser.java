package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMPANY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FOLLOW_UP_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Company;
import seedu.address.model.person.Email;
import seedu.address.model.person.FollowUpDate;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Role;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        if (args.trim().equals("all")) {
            return new DeleteCommand();
        }

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                PREFIX_ROLE, PREFIX_COMPANY, PREFIX_FOLLOW_UP_DATE);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ROLE,
                PREFIX_COMPANY, PREFIX_FOLLOW_UP_DATE);

        int selectorCount = (argMultimap.getValue(PREFIX_NAME).isPresent() ? 1 : 0)
                + (argMultimap.getValue(PREFIX_PHONE).isPresent() ? 1 : 0)
                + (argMultimap.getValue(PREFIX_EMAIL).isPresent() ? 1 : 0)
                + (argMultimap.getValue(PREFIX_ROLE).isPresent() ? 1 : 0)
                + (argMultimap.getValue(PREFIX_COMPANY).isPresent() ? 1 : 0)
                + (argMultimap.getValue(PREFIX_FOLLOW_UP_DATE).isPresent() ? 1 : 0);
        if (selectorCount > 0) {
            if (selectorCount != 1 || !argMultimap.getPreamble().isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
            }
            if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
                Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
                return new DeleteCommand(phone);
            }
            if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
                Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
                return new DeleteCommand(name);
            }
            if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
                Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
                return new DeleteCommand(email);
            }
            if (argMultimap.getValue(PREFIX_COMPANY).isPresent()) {
                Company company = ParserUtil.parseCompany(argMultimap.getValue(PREFIX_COMPANY).get());
                return new DeleteCommand(company);
            }
            if (argMultimap.getValue(PREFIX_ROLE).isPresent()) {
                Role role = ParserUtil.parseRole(argMultimap.getValue(PREFIX_ROLE).get());
                return new DeleteCommand(role.value);
            }
        }

        if (argMultimap.getValue(PREFIX_FOLLOW_UP_DATE).isPresent()) {
            FollowUpDate followUpDate = ParserUtil.parseFollowUpDate(
                    argMultimap.getValue(PREFIX_FOLLOW_UP_DATE).get());
            return new DeleteCommand(followUpDate);
        }

        try {
            Index index = ParserUtil.parseIndex(args);
            return new DeleteCommand(index);
        } catch (ParseException pe) {
            if (args.trim().matches("-?\\d+")) {
                throw pe;
            }
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE), pe);
        }
    }

}
