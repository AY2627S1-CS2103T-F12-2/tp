package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import seedu.address.commons.util.CsvUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/** Exports current address book to a csv file */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Exports the displayed contacts to a CSV file in the data folder.\n"
            + "Parameters: [fn/FILE_NAME] (letters, digits, '-' or '_'; \".csv\" is added automatically; "
            + "default: export)\n"
            + "Examples: " + COMMAND_WORD + "\n"
            + "          " + COMMAND_WORD + " fn/favorites";
    public static final String MESSAGE_SUCCESS = "Exported %1$d contact(s) to %2$s.";
    public static final String MESSAGE_EXPORT_FAILED = "Could not write export file %1$s: %2$s";

    private static final Path DEFAULT_EXPORT_FOLDER = Paths.get("data");

    // Update this header and toCsvRow together whenever a Person field is added.
    // Interactions are one-to-many, so they are not exported here.
    private static final String CSV_HEADER = CsvUtil.toRow("Name", "Phone", "Email", "Address", "Role",
            "Company", "Boss", "Follow-up date", "Category", "Tags", "Remark");

    private final String fileName;
    private final Path exportFolder;

    /**
     * Creates an ExportCommand that writes the displayed contacts to {@code fileName}.csv in the data folder.
     *
     * @param fileName file name without the ".csv" extension, already trimmed and validated by the parser.
     */
    public ExportCommand(String fileName) {
        this(fileName, DEFAULT_EXPORT_FOLDER);
    }

    /**
     * Creates an ExportCommand that writes the displayed contacts to {@code fileName}.csv in {@code exportFolder}.
     */
    public ExportCommand(String fileName, Path exportFolder) {
        requireNonNull(fileName);
        requireNonNull(exportFolder);
        this.fileName = fileName;
        this.exportFolder = exportFolder;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> persons = model.getFilteredPersonList();

        StringBuilder csvContent = new StringBuilder(CSV_HEADER).append(System.lineSeparator());
        for (Person person : persons) {
            csvContent.append(toCsvRow(person)).append(System.lineSeparator());
        }

        Path exportPath = exportFolder.resolve(fileName + ".csv");
        try {
            CsvUtil.writeCsv(exportPath, csvContent.toString());
        } catch (IOException e) {
            throw new CommandException(String.format(MESSAGE_EXPORT_FAILED, exportPath, e.getMessage()), e);
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, persons.size(), exportPath.toAbsolutePath()));
    }

    /**
     * Returns {@code person} as one CSV row, in the same column order as {@link #CSV_HEADER}.
     */
    private static String toCsvRow(Person person) {

        //fd can be null
        String followUpDate = person.getFollowUpDate() == null ? "" : person.getFollowUpDate().toString();

        String tags = person.getTags().stream()
                .map(tag -> tag.tagName)
                .sorted()
                .collect(Collectors.joining(";"));
        return CsvUtil.toRow(
                person.getName().fullName,
                person.getPhone().value,
                person.getEmail().value,
                person.getAddress().value,
                person.getRole().value,
                person.getCompany().value,
                person.getBoss().fullName,
                followUpDate,
                person.getCategory().name(),
                tags,
                person.getRemark().value);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ExportCommand otherExportCommand)) {
            return false;
        }
        return fileName.equals(otherExportCommand.fileName)
                && exportFolder.equals(otherExportCommand.exportFolder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, exportFolder);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("fileName", fileName + ".csv").toString();
    }
}
