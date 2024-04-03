package rocketseat.com.passin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rocketseat.com.passin.domain.attendee.Attendee;
import rocketseat.com.passin.domain.event.Event;
import rocketseat.com.passin.domain.event.exceptions.EventNotFoundException;
import rocketseat.com.passin.dtos.attendee.AttendeeIdDTO;
import rocketseat.com.passin.dtos.attendee.AttendeeRequestDTO;
import rocketseat.com.passin.dtos.event.EventIdDTO;
import rocketseat.com.passin.dtos.event.EventRequestDTO;
import rocketseat.com.passin.dtos.event.EventResponseDTO;
import rocketseat.com.passin.repositories.EventRepository;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final AttendeeService attendeeService;

    public EventResponseDTO getEventDetail(String eventId){
        Event event = this.getEvent(eventId);
        List<Attendee> attendeeList = this.attendeeService.getAllAttendeesFromEvent(eventId);
        return new EventResponseDTO(event, attendeeList.size());
    }

    public EventIdDTO createEvent(EventRequestDTO eventDTO){
        Event newEvent = new Event();
        newEvent.setTitle(eventDTO.title());
        newEvent.setDetails(eventDTO.details());
        newEvent.setMaximumAttendees(eventDTO.maximumAttendees());
        newEvent.setSlug(this.createSlug(eventDTO.title()));

        this.eventRepository.save(newEvent);
        return new EventIdDTO(newEvent.getId());
    }

    public AttendeeIdDTO registerAttendeeOnEvent(String eventId, AttendeeRequestDTO attendeeRequestDTO){
        this.attendeeService.verifyAttendeeSubscription(eventId, attendeeRequestDTO.email());
        Event event = this.getEvent(eventId);
        List<Attendee> attendeeList = this.attendeeService.getAllAttendeesFromEvent(eventId);

        if(event.getMaximumAttendees() <= attendeeList.size())
            throw new RuntimeException("Event is full!");

        Attendee newAttendee = new Attendee();
            newAttendee.setName(attendeeRequestDTO.name());
            newAttendee.setEmail(attendeeRequestDTO.email());
            newAttendee.setEvent(event);
            newAttendee.setCreatedAt(LocalDateTime.now());

        this.attendeeService.registerAttendee(newAttendee);
        return new AttendeeIdDTO(newAttendee.getId());
    }

    private Event getEvent(String eventId){
        return this.eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Could not find event with ID: "
                        + eventId));
    }

    private String createSlug(String text){
        String normalize = Normalizer.normalize(text, Normalizer.Form.NFD);

        return normalize
                .replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]", "")
                .replaceAll("[^\\w\\s]", "")
                .replaceAll("\\s+", "-")
                .toLowerCase();
    }
}
