package seedu.address.model.person;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/** Checks management chains consistently when adding or editing contacts. */
public final class BossChainValidator {
    public static final String MESSAGE_BOSS_CYCLE =
            "Error: A contact cannot be their own boss or form a cyclical management chain.";

    private BossChainValidator() {}

    /**
     * Returns whether the proposed contact's boss chain contains a cycle.
     *
     * @param people existing contacts
     * @param original contact being replaced, or null when adding a new contact
     * @param proposed contact whose boss chain is checked
     */
    public static boolean createsCycle(List<Person> people, Person original, Person proposed) {
        String bossName = proposed.getBoss().fullName;
        Set<String> visited = new HashSet<>();
        visited.add(proposed.getName().fullName.toLowerCase(Locale.ROOT));

        while (!bossName.isEmpty()) {
            if (!visited.add(bossName.toLowerCase(Locale.ROOT))) {
                return true;
            }
            String bossToFind = bossName;
            Person boss = people.stream()
                    .filter(person -> person != original)
                    .filter(person -> person.getName().fullName.equalsIgnoreCase(bossToFind))
                    .findFirst()
                    .orElse(null);
            if (boss == null) {
                return false;
            }
            bossName = boss.getBoss().fullName;
        }
        return false;
    }
}
