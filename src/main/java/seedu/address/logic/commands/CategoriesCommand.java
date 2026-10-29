package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import seedu.address.model.Model;
import seedu.address.model.person.Category;
import seedu.address.model.person.Person;


/**
 * Lists all categories and the number of contacts assigned to each category.
 */
public class CategoriesCommand extends Command {
    public static final String COMMAND_WORD = "categories";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists all categories and the number of persons in each category.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Listed all categories:\n%1$s";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        Map<Category, Integer> categoryPersonCountMap = countPersonsPerCategory(model.getUnfilteredPersonList());
        String formattedMessage = getFormattedMessage(categoryPersonCountMap);
        return new CommandResult(String.format(MESSAGE_SUCCESS, formattedMessage));
    }

    /**
     * Creates a map that records the number of contacts assigned to each category.
     */
    private Map<Category, Integer> countPersonsPerCategory(List<Person> persons) {
        Map<Category, Integer> categoryCounts = new EnumMap<>(Category.class);

        Arrays.stream(Category.values())
                .forEach(category -> categoryCounts.put(category, 0));

        persons.forEach(person -> {
            Category category = person.getCategory();
            categoryCounts.put(category, categoryCounts.get(category) + 1);
        });
        return categoryCounts;
    }

    /**
     * Creates and returns a formatted multi-line summary of each category with its respective assignee count.
     */
    private String getFormattedMessage(Map<Category, Integer> categoryPersonCountMap) {
        return categoryPersonCountMap.entrySet().stream()
                .map(entry -> entry.getKey() + " : " + entry.getValue())
                .collect(Collectors.joining("\n"));

    }
}
