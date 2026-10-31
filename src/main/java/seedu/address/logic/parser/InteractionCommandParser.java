package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INTERACTION_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INTERACTION_DELETE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INTERACTION_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INTERACTION_TYPE;

import java.util.Locale;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteInteractionCommand;
import seedu.address.logic.commands.InteractionCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Interaction;
import seedu.address.model.person.InteractionType;

/**
 * Parses arguments for logging or deleting an interaction.
 */
public class InteractionCommandParser implements Parser<Command> {
    public static final String MESSAGE_MIXED_OPERATIONS = "Use del/ alone, without d/, ty/, or nt/.";

    @Override
    public Command parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap arguments = ArgumentTokenizer.tokenize(args,
                PREFIX_INTERACTION_DATE, PREFIX_INTERACTION_TYPE, PREFIX_INTERACTION_NOTE, PREFIX_INTERACTION_DELETE);
        arguments.verifyNoDuplicatePrefixesFor(
                PREFIX_INTERACTION_DATE, PREFIX_INTERACTION_TYPE, PREFIX_INTERACTION_NOTE, PREFIX_INTERACTION_DELETE);

        Index index;
        try {
            index = ParserUtil.parseIndex(arguments.getPreamble());
        } catch (ParseException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    InteractionCommand.MESSAGE_USAGE), e);
        }

        if (arguments.getValue(PREFIX_INTERACTION_DELETE).isPresent()) {
            if (arguments.getValue(PREFIX_INTERACTION_DATE).isPresent()
                    || arguments.getValue(PREFIX_INTERACTION_TYPE).isPresent()
                    || arguments.getValue(PREFIX_INTERACTION_NOTE).isPresent()) {
                throw new ParseException(MESSAGE_MIXED_OPERATIONS);
            }
            return new DeleteInteractionCommand(index,
                    ParserUtil.parseIndex(arguments.getValue(PREFIX_INTERACTION_DELETE).get()));
        }

        if (arguments.getValue(PREFIX_INTERACTION_DATE).isEmpty()
                || arguments.getValue(PREFIX_INTERACTION_TYPE).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    InteractionCommand.MESSAGE_USAGE));
        }
        String date = arguments.getValue(PREFIX_INTERACTION_DATE).get();
        String typeText = arguments.getValue(PREFIX_INTERACTION_TYPE).get();
        String note = arguments.getValue(PREFIX_INTERACTION_NOTE).orElse("");

        if (!Interaction.isValidDate(date)) {
            throw new ParseException(Interaction.MESSAGE_INVALID_DATE);
        }

        if (!InteractionType.isValidInteractionType(typeText)) {
            throw new ParseException(InteractionType.MESSAGE_CONSTRAINTS);
        }

        InteractionType type = InteractionType.valueOf(typeText.toUpperCase(Locale.ROOT));
        return new InteractionCommand(index, date, type, note);
    }
}
