package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Objects;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Category;
import seedu.address.model.person.Person;

/**
 * Adds a person to a particular category.
 */
public class CategoryCommand extends Command {
    public static final String COMMAND_WORD = "category";

    public static final String MESSAGE_SUCCESS = "Changed %1$s's category to %2$s.";
    public static final String MESSAGE_ALREADY_IN_CATEGORY = "%1$s is already in %2$s.";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Places the person identified by the index number used in the displayed person list into a category\n"
            + "Valid categories: UNCATEGORIZED, CLIENT, INVESTOR, LAWYER, ACCOUNTANT, CONSULTANT, OTHER"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_CATEGORY + "CATEGORY\n"
            + Category.MESSAGE_CONSTRAINTS + "\n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_CATEGORY + "INVESTOR";

    private final Index targetIndex;

    private final Category targetCategory;

    /**
     * @param targetIndex of the person in the filtered person list to place into category
     * @param targetCategory the category that the person should be placed into
     */
    public CategoryCommand(Index targetIndex, Category targetCategory) {
        requireNonNull(targetIndex);
        requireNonNull(targetCategory);
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

        if (personToCategorize.getCategory() == targetCategory) {
            return new CommandResult(String.format(MESSAGE_ALREADY_IN_CATEGORY,
                    personToCategorize.getName(), targetCategory));
        }

        //Assigning the category to a person
        Person editedPerson = new Person(
                personToCategorize.getName(),
                personToCategorize.getPhone(),
                personToCategorize.getEmail(),
                personToCategorize.getAddress(),
                personToCategorize.getTags(),
                personToCategorize.getRemark(),
                targetCategory,
                personToCategorize.getRole(),
                personToCategorize.getCompany(),
                personToCategorize.getBoss(),
                personToCategorize.getFollowUpDate(),
                personToCategorize.getInteractions());

        model.setPerson(personToCategorize, editedPerson);
        model.commitAddressBook(COMMAND_WORD);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_SUCCESS, editedPerson.getName(), targetCategory));
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
    public int hashCode() {
        return Objects.hash(targetIndex, targetCategory);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("targetCategory", targetCategory)
                .toString();
    }

}
