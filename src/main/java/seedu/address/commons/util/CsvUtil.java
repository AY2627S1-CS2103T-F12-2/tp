package seedu.address.commons.util;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Formats values as comma-separated values (CSV) and writes CSV files.
 */
public class CsvUtil {

    /**
     * Returns {@code value} escaped for a CSV cell.
     * Values containing a comma, double quote or line break are wrapped in double quotes,
     * and any double quotes inside them are doubled. A null value becomes an empty cell.
     */
    public static String escape(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    /**
     * Returns a CSV row made of the escaped {@code values}, separated by commas.
     */
    public static String toRow(String... values) {
        return Arrays.stream(values)
                .map(CsvUtil::escape)
                .collect(Collectors.joining(","));
    }

    /**
     * Writes {@code csvContent} to {@code file}, creating the file and its parent folders if needed.
     *
     * @throws IOException if the file cannot be created or written.
     */
    public static void writeCsv(Path file, String csvContent) throws IOException {
        FileUtil.createIfMissing(file);
        FileUtil.writeToFile(file, csvContent);
    }
}
