package rocketseat.com.passin.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import rocketseat.com.passin.dtos.attendee.AttendeeIdDTO;
import rocketseat.com.passin.dtos.attendee.AttendeeRequestDTO;
import rocketseat.com.passin.dtos.event.EventIdDTO;
import rocketseat.com.passin.dtos.event.EventRequestDTO;
import rocketseat.com.passin.dtos.event.EventResponseDTO;
import rocketseat.com.passin.dtos.attendee.AttendeeListResponseDTO;
import rocketseat.com.passin.services.AttendeeService;
import rocketseat.com.passin.services.EventService;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController{
    private final EventService eventService;
    private final AttendeeService attendeeService;

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable String eventId){
        EventResponseDTO event = this.eventService.getEventDetail(eventId);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/attendees/{eventId}")
    @CrossOrigin("http://localhost:5173")
    public ResponseEntity<AttendeeListResponseDTO> getEventAttendees(@PathVariable String eventId){
        AttendeeListResponseDTO attendeeListResponse = this.attendeeService.getEventAttendees(eventId);
        return ResponseEntity.ok(attendeeListResponse);
    }

    @PostMapping
    public ResponseEntity<EventIdDTO> createEvent(@RequestBody EventRequestDTO body, UriComponentsBuilder uriComponentsBuilder){
        EventIdDTO eventIdDTO = this.eventService.createEvent(body);

        var uri = uriComponentsBuilder.path("/events/{id}").buildAndExpand(eventIdDTO.eventId()).toUri();

        return ResponseEntity.created(uri).body(eventIdDTO);
    }

    @PostMapping("/{eventId}/attendees")
    public ResponseEntity<AttendeeIdDTO> registerAttendee(@PathVariable String eventId,
                                                          @RequestBody AttendeeRequestDTO body,
                                                          UriComponentsBuilder uriComponentsBuilder){
        AttendeeIdDTO attendeeIdDTO = this.eventService.registerAttendeeOnEvent(eventId, body);

        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/badge")
                .buildAndExpand(attendeeIdDTO.attendeeId()).toUri();

        return ResponseEntity.created(uri).body(attendeeIdDTO);
    }
}
