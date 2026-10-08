package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class CompanyTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Company(null));
    }

    @Test
    public void constructor_invalidCompanyName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Company(""));
    }

    @Test
    public void isValidCompanyName() {
        // null company name
        assertThrows(NullPointerException.class, () -> Company.isValidCompanyName(null));

        // invalid company names
        assertFalse(Company.isValidCompanyName("")); // empty string
        assertFalse(Company.isValidCompanyName("  ")); // spaces only
        assertFalse(Company.isValidCompanyName("AB")); // fewer than 3 characters
        assertFalse(Company.isValidCompanyName("A@B")); // special character
        assertFalse(Company.isValidCompanyName("Apple&Inc")); // special character
        assertFalse(Company.isValidCompanyName("Apple  Inc")); // repeated spaces
        assertFalse(Company.isValidCompanyName(" Apple Inc")); // leading space
        assertFalse(Company.isValidCompanyName("Apple Inc ")); // trailing space

        // valid company names
        assertTrue(Company.isValidCompanyName("ABC")); // minimum length
        assertTrue(Company.isValidCompanyName("Apple"));
        assertTrue(Company.isValidCompanyName("Apple Inc"));
        assertTrue(Company.isValidCompanyName("Company123")); // alphanumeric characters
        assertTrue(Company.isValidCompanyName("Company 123"));
    }

    @Test
    public void constructor_condensesSpaces() {
        Company companyName = new Company("  Apple   Inc  ");

        assertTrue(companyName.value.equals("Apple Inc"));
    }
}
