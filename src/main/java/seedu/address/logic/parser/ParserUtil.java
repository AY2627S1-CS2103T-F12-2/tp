package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Boss;
import seedu.address.model.person.Company;
import seedu.address.model.person.Email;
import seedu.address.model.person.FollowUpDate;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Role;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index must be a positive integer.";
    public static final String MESSAGE_EDIT_NAME =
            "Invalid name! Please enter the name in the format: Firstname Surname.";
    public static final String MESSAGE_EDIT_NAME_CHARACTERS =
            "Invalid name! Names may contain alphabetic characters only.";
    public static final String MESSAGE_EDIT_PHONE_LENGTH =
            "Invalid phone number! Please enter exactly 8 digits.";
    public static final String MESSAGE_EDIT_PHONE_CHARACTERS =
            "Invalid phone number! Please use numbers only.";
    public static final String MESSAGE_EDIT_PHONE_SPACES =
            "Invalid phone number! Spaces are not allowed.";
    public static final String MESSAGE_EDIT_EMAIL_AT =
            "Invalid email! Please include an '@' symbol.";
    public static final String MESSAGE_EDIT_EMAIL_DOMAIN =
            "Invalid email! Please use an email address ending with '.com'.";
    public static final String MESSAGE_EDIT_EMAIL_SPACES =
            "Invalid email! Spaces are not allowed.";
    public static final String MESSAGE_EDIT_EMAIL_FORMAT =
            "Invalid email! Please use the format username@example.com.";
    public static final String MESSAGE_EDIT_ROLE =
            "Invalid role! Please use alphanumeral characters. Special symbols are not allowed.";
    public static final String MESSAGE_EDIT_COMPANY =
            "Invalid company name! Please use alphanumeral characters. Special symbols are not allowed.";
    public static final String MESSAGE_EDIT_BOSS =
            "Invalid boss name! Please enter the name in the format: Firstname Surname.";
    public static final String MESSAGE_EDIT_BOSS_CHARACTERS =
            "Invalid boss name! Names may contain alphabetic characters only.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified value is invalid (not a non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /** Parses a phone number using the edit command's exact eight-digit rule. */
    public static Phone parseEditPhone(String phone) throws ParseException {
        requireNonNull(phone);
        if (phone.contains(" ")) {
            throw new ParseException(MESSAGE_EDIT_PHONE_SPACES);
        }
        String trimmedPhone = phone.trim();
        if (!trimmedPhone.matches("\\d+")) {
            throw new ParseException(MESSAGE_EDIT_PHONE_CHARACTERS);
        }
        if (trimmedPhone.length() != 8) {
            throw new ParseException(MESSAGE_EDIT_PHONE_LENGTH);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /** Parses an edit email using the command's .com and no-spaces rules. */
    public static Email parseEditEmail(String email) throws ParseException {
        requireNonNull(email);
        if (email.contains(" ")) {
            throw new ParseException(MESSAGE_EDIT_EMAIL_SPACES);
        }
        String trimmedEmail = email.trim();
        if (trimmedEmail.indexOf('@') < 0) {
            throw new ParseException(MESSAGE_EDIT_EMAIL_AT);
        }
        if (!trimmedEmail.toLowerCase(java.util.Locale.ROOT).endsWith(".com")) {
            throw new ParseException(MESSAGE_EDIT_EMAIL_DOMAIN);
        }
        if (trimmedEmail.indexOf('@') != trimmedEmail.lastIndexOf('@')
                || !Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(MESSAGE_EDIT_EMAIL_FORMAT);
        }
        return new Email(trimmedEmail);
    }

    /** Parses an edit name and normalizes repeated spaces. */
    public static Name parseEditName(String name) throws ParseException {
        requireNonNull(name);
        String normalizedName = name.trim().replaceAll(" +", " ");
        if (!normalizedName.matches("[A-Za-z]+ [A-Za-z]+")) {
            if (!normalizedName.matches("[A-Za-z ]+")) {
                throw new ParseException(MESSAGE_EDIT_NAME_CHARACTERS);
            }
            throw new ParseException(MESSAGE_EDIT_NAME);
        }
        return new Name(normalizedName);
    }

    /**
     * Parses a {@code String role} into a {@code Role}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static Role parseRole(String role) throws ParseException {
        requireNonNull(role);
        String trimmedRole = role.trim();
        if (!Role.isValidRole(trimmedRole)) {
            throw new ParseException(Role.MESSAGE_CONSTRAINTS);
        }
        return new Role(trimmedRole);
    }

    /** Parses a role for the edit command. */
    public static Role parseEditRole(String role) throws ParseException {
        requireNonNull(role);
        String normalizedRole = role.trim().replaceAll(" +", " ");
        if (!Role.isValidRole(normalizedRole)) {
            throw new ParseException(MESSAGE_EDIT_ROLE);
        }
        return new Role(normalizedRole);
    }

    /**
     * Parses a {@code String company} into a {@code Company}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static Company parseCompany(String company) throws ParseException {
        requireNonNull(company);
        String trimmedCompany = company.trim();
        if (!Company.isValidCompanyName(trimmedCompany)) {
            throw new ParseException(Company.MESSAGE_CONSTRAINTS);
        }
        return new Company(trimmedCompany);
    }

    /** Parses a company for the edit command. */
    public static Company parseEditCompany(String company) throws ParseException {
        requireNonNull(company);
        String normalizedCompany = company.trim().replaceAll(" +", " ");
        if (!Company.isValidCompanyName(normalizedCompany)) {
            throw new ParseException(MESSAGE_EDIT_COMPANY);
        }
        return new Company(normalizedCompany);
    }

    /**
     * Parses a {@code String boss} into a {@code Boss}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static Boss parseBoss(String boss) throws ParseException {
        requireNonNull(boss);
        String trimmedBoss = boss.trim();
        if (!Boss.isValidBoss(trimmedBoss)) {
            throw new ParseException(Boss.MESSAGE_CONSTRAINTS);
        }
        return new Boss(trimmedBoss);
    }

    /** Parses an optional two-word boss name for the edit command. */
    public static Boss parseEditBoss(String boss) throws ParseException {
        requireNonNull(boss);
        String normalizedBoss = boss.trim().replaceAll(" +", " ");
        if (normalizedBoss.isEmpty()) {
            return new Boss("");
        }
        if (!normalizedBoss.matches("[A-Za-z]+ [A-Za-z]+")) {
            if (!normalizedBoss.matches("[A-Za-z ]+")) {
                throw new ParseException(MESSAGE_EDIT_BOSS_CHARACTERS);
            }
            throw new ParseException(MESSAGE_EDIT_BOSS);
        }
        return new Boss(normalizedBoss);
    }

    /** Parses a follow-up date in the format {@code yyyy-MM-dd}. */
    public static FollowUpDate parseFollowUpDate(String followUpDate) throws ParseException {
        requireNonNull(followUpDate);
        String trimmedFollowUpDate = followUpDate.trim();
        if (!FollowUpDate.isValidFollowUpDate(trimmedFollowUpDate)) {
            throw new ParseException(FollowUpDate.MESSAGE_CONSTRAINTS);
        }
        return new FollowUpDate(trimmedFollowUpDate);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }
}
