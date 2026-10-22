package seedu.address.storage;

import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Interaction;
import seedu.address.model.person.InteractionType;
import seedu.address.model.person.Name;

/** Jackson-friendly form of an interaction. */
class JsonAdaptedInteraction {
    private final String name;
    private final String date;
    private final String type;
    private final String note;

    @JsonCreator
    public JsonAdaptedInteraction(@JsonProperty("name") String name,
            @JsonProperty("date") String date, @JsonProperty("type") String type,
            @JsonProperty("note") String note) {
        this.name = name;
        this.date = date;
        this.type = type;
        this.note = note;
    }

    public JsonAdaptedInteraction(Interaction source) {
        this(source.getName().fullName, source.getDate().toString(),
                source.getType().name(), source.getNote());
    }

    public Interaction toModelType() throws IllegalValueException {
        if (name == null || date == null || type == null || note == null) {
            throw new IllegalValueException(Interaction.MESSAGE_CONSTRAINTS);
        }

        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }

        if (!Interaction.isValidDate(date)) {
            throw new IllegalValueException(Interaction.MESSAGE_CONSTRAINTS);
        }

        if (!InteractionType.isValidInteractionType(type)) {
            throw new IllegalValueException(InteractionType.MESSAGE_CONSTRAINTS);
        }

        InteractionType interactionType =
                InteractionType.valueOf(type.toUpperCase(Locale.ROOT));

        return new Interaction(name, date, interactionType, note);
    }
}
