package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INTERACTION_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INTERACTION_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INTERACTION_TYPE;

import java.util.Locale;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.InteractionCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Interaction;
import seedu.address.model.person.InteractionType;

/** Parses arguments for an {@code InteractionCommand}. */
public class InteractionCommandParser implements Parser<InteractionCommand> {

    @Override
    public InteractionCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap arguments = ArgumentTokenizer.tokenize(args,
                PREFIX_INTERACTION_DATE, PREFIX_INTERACTION_TYPE, PREFIX_INTERACTION_NOTE);
        arguments.verifyNoDuplicatePrefixesFor(
                PREFIX_INTERACTION_DATE, PREFIX_INTERACTION_TYPE, PREFIX_INTERACTION_NOTE);

        Index index;
        try {
            index = ParserUtil.parseIndex(arguments.getPreamble());
        } catch (ParseException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    InteractionCommand.MESSAGE_USAGE), e);
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
            throw new ParseException(Interaction.MESSAGE_CONSTRAINTS);
        }

        if (!InteractionType.isValidInteractionType(typeText)) {
            throw new ParseException(InteractionType.MESSAGE_CONSTRAINTS);
        }
        
        InteractionType type = InteractionType.valueOf(typeText.toUpperCase(Locale.ROOT));
        return new InteractionCommand(index, date, type, note);
    }
}
