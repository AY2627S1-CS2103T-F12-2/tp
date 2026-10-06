package seedu.address.logic.commands;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Adds a person to a particular category.
 */
public class CategoryCommand extends Command{
    public static final String COMMAND_WORD = "cate";

    public static final String MESSAGE_SUCCESS = "Person has been added to new category: %1$s";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Places the person identified by the index number used in the displayed person list into a category\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    private final Index targetIndex;

    private final String targetCategory;

    public CategoryCommand(Index targetIndex, String targetCategory) {
        this.targetIndex = targetIndex;
        this.targetCategory = targetCategory;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToCategorize = lastShownList.get(targetIndex.getZeroBased());

        //Assigning the category to a person


        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(personToCategorize)));
    }

}
