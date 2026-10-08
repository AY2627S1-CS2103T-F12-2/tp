package seedu.address.logic.commands;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Category;
import seedu.address.model.person.Person;

import java.util.List;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

/**
 * Adds a person to a particular category.
 */
public class CategoryCommand extends Command{
    public static final String COMMAND_WORD = "category";

    public static final String MESSAGE_SUCCESS = "Person has been added to new category: %1$s";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Places the person identified by the value number used in the displayed person list into a category\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_CATEGORY + "CATEGORY\n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_CATEGORY + "ROLE";

    private final Index targetIndex;

    private final Category targetCategory;

    public CategoryCommand(Index targetIndex, Category targetCategory) {
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
        Person editedPerson = new Person(
                personToCategorize.getName(), personToCategorize.getPhone(), personToCategorize.getEmail(),
                personToCategorize.getAddress(), personToCategorize.getTags(), personToCategorize.getRemark(), targetCategory);

        model.setPerson(personToCategorize, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(editedPerson)));
    }



    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CategoryCommand otherCategoryCommand)) {
            return false;
        }

        return targetIndex.equals(otherCategoryCommand.targetIndex)
                && targetCategory.equals(otherCategoryCommand.targetCategory);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("targetCategory", targetCategory)
                .toString();
    }

}
