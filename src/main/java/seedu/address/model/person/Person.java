package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;
    private final Remark remark;
    private final Role role;
    private final Company company;
    private final Boss boss;
    private final FollowUpDate followUpDate;

    // Data fields
    private final Address address;
    private final Set<Tag> tags = new HashSet<>();
    private final Category category;

    /**
     * Creates a person with the required contact details and default employment details.
     */
    public Person(Name name, Phone phone, Email email, Address address,
                  Set<Tag> tags, Remark remark, Category category) {
        this(name, phone, email, address, tags, remark, category, new Role("TEST ROLE"),
                new Company("TEST COMPANY"), new Boss("TEST BOSS"));
    }

    /**
     * Creates a person with all contact and employment details.
     */
    public Person(Name name, Phone phone, Email email, Address address,
                  Set<Tag> tags, Remark remark, Category category, Role role, Company company, Boss boss) {
        this(name, phone, email, address, tags, remark, category, role, company, boss, null);
    }

    /**
     * Creates a person with all contact and employment details, including an optional follow-up date.
     */
    public Person(Name name, Phone phone, Email email, Address address,
                  Set<Tag> tags, Remark remark, Category category, Role role, Company company, Boss boss,
                  FollowUpDate followUpDate) {
        requireAllNonNull(name, phone, email, address, tags, remark, category, role, company, boss);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.remark = remark;
        this.role = role;
        this.company = company;
        this.boss = boss;
        this.followUpDate = followUpDate;
        this.tags.addAll(tags);
        this.category = category;
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public Remark getRemark() {
        return remark;
    }

    public Role getRole() {
        return role;
    }

    public Company getCompany() {
        return company;
    }

    public Boss getBoss() {
        return boss;
    }

    public FollowUpDate getFollowUpDate() {
        return followUpDate;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }


    public Category getCategory() {
        return this.category;
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().fullName.equals(getName().fullName);
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person otherPerson)) {
            return false;
        }

        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && tags.equals(otherPerson.tags)
                && remark.equals(otherPerson.remark)
                && role.equals(otherPerson.role)
                && company.equals(otherPerson.company)
                && boss.equals(otherPerson.boss)
                && Objects.equals(followUpDate, otherPerson.followUpDate)
                && category.equals(otherPerson.category);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, tags, remark, role, company, boss, followUpDate, category);
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("tags", tags)
                .add("role", role)
                .add("company", company)
                .add("boss", boss);
        if (followUpDate != null) {
            builder.add("followUpDate", followUpDate);
        }
        return builder.add("category", category).toString();
    }

}
