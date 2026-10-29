package seedu.address.commons.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class CsvUtilTest {

    @TempDir
    public Path testFolder;

    @Test
    public void escape_null_returnsEmptyString() {
        assertEquals("", CsvUtil.escape(null));
    }

    @Test
    public void escape_plainValue_returnsUnchanged() {
        assertEquals("", CsvUtil.escape(""));
        assertEquals("Amy Rose", CsvUtil.escape("Amy Rose"));
        assertEquals("amy@ex.com", CsvUtil.escape("amy@ex.com"));
    }

    @Test
    public void escape_specialCharacters_wrapsInQuotes() {
        // comma
        assertEquals("\"Blk 1, Clementi Rd\"", CsvUtil.escape("Blk 1, Clementi Rd"));
        // double quotes are doubled
        assertEquals("\"Said \"\"hi\"\"\"", CsvUtil.escape("Said \"hi\""));
        // line breaks
        assertEquals("\"line1\nline2\"", CsvUtil.escape("line1\nline2"));
        assertEquals("\"line1\rline2\"", CsvUtil.escape("line1\rline2"));
    }

    @Test
    public void toRow_mixedValues_escapesAndJoinsWithCommas() {
        assertEquals("Amy Rose,\"Blk 1, Clementi Rd\",", CsvUtil.toRow("Amy Rose", "Blk 1, Clementi Rd", null));
    }

    @Test
    public void toRow_noValues_returnsEmptyString() {
        assertEquals("", CsvUtil.toRow());
    }

    @Test
    public void writeCsv_missingParentFolder_createsFolderAndFile() throws IOException {
        Path file = testFolder.resolve("exports").resolve("contacts.csv");

        CsvUtil.writeCsv(file, "Name\nAmy Rose\n");

        assertTrue(Files.exists(file));
        assertEquals("Name\nAmy Rose\n", Files.readString(file));
    }

    @Test
    public void writeCsv_existingFile_overwritesContent() throws IOException {
        Path file = testFolder.resolve("contacts.csv");
        Files.writeString(file, "old content that is longer");

        CsvUtil.writeCsv(file, "new");

        assertEquals("new", Files.readString(file));
    }

    @Test
    public void writeCsv_pathIsFolder_throwsIoException() {
        assertThrows(IOException.class, () -> CsvUtil.writeCsv(testFolder, "content"));
    }
}
