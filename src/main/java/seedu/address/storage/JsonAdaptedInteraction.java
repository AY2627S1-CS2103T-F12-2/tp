package seedu.address.storage;

import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Interaction;
import seedu.address.model.person.InteractionType;

/** Jackson-friendly form of an interaction. */
class JsonAdaptedInteraction {
    private final String date;
    private final String type;
    private final String note;

    @JsonCreator
    public JsonAdaptedInteraction(@JsonProperty("date") String date, @JsonProperty("type") String type,
            @JsonProperty("note") String note) {
        this.date = date;
        this.type = type;
        this.note = note;
    }

    public JsonAdaptedInteraction(Interaction source) {
        this(source.getDate().toString(),
                source.getType().name(), source.getNote());
    }

    public Interaction toModelType() throws IllegalValueException {
        if (date == null || type == null || note == null) {
            throw new IllegalValueException(Interaction.MESSAGE_CONSTRAINTS);
        }

        if (!Interaction.isValidDate(date)) {
            throw new IllegalValueException(Interaction.MESSAGE_CONSTRAINTS);
        }

        if (!InteractionType.isValidInteractionType(type)) {
            throw new IllegalValueException(InteractionType.MESSAGE_CONSTRAINTS);
        }

        InteractionType interactionType =
                InteractionType.valueOf(type.toUpperCase(Locale.ROOT));

        return new Interaction(date, interactionType, note);
    }
}
