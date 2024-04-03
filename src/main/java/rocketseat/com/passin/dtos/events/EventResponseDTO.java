package rocketseat.com.passin.dtos.events;

import rocketseat.com.passin.domain.event.Event;
import rocketseat.com.passin.dtos.events.EventDetailDTO;

public class EventResponseDTO {
    EventDetailDTO event;

    public EventResponseDTO(Event event, Integer numberOfAttendees){
        this.event = new EventDetailDTO(
                event.getId(), event.getTitle(),
                event.getDetails(), event.getSlug(),
                event.getMaximumAttendees(), numberOfAttendees);
    }

}
