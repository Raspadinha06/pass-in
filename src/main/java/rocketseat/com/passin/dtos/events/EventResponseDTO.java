package rocketseat.com.passin.dtos.events;

import lombok.Getter;
import rocketseat.com.passin.domain.event.Event;
import rocketseat.com.passin.dtos.events.EventDetailDTO;

@Getter
public class EventResponseDTO {
    EventDetailDTO event;

    public EventResponseDTO(Event event, Integer numberOfAttendees){
        this.event = new EventDetailDTO(
                event.getId(), event.getTitle(),
                event.getDetails(), event.getSlug(),
                event.getMaximumAttendees(), numberOfAttendees);
    }

}
