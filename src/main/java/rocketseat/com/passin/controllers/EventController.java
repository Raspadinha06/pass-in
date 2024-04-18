package rocketseat.com.passin.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "pass-in")
public class EventController{
    private final EventService eventService;
    private final AttendeeService attendeeService;

    @Operation(summary = "Displays the details of an event.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event found successfully!"),
            @ApiResponse(responseCode = "404", description = "Event not found."),
            @ApiResponse(responseCode = "500", description = "Data search error."),
    })
    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable String eventId){
        EventResponseDTO event = this.eventService.getEventDetail(eventId);
        return ResponseEntity.ok(event);
    }

    @Operation(summary = "Displays the attendees of an event.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attendees found successfully!"),
            @ApiResponse(responseCode = "404", description = "Event not found."),
            @ApiResponse(responseCode = "500", description = "Data search error."),
    })
    @CrossOrigin("http://localhost:5173")
    @GetMapping("/attendees/{eventId}")
    public ResponseEntity<AttendeeListResponseDTO> getEventAttendees(@PathVariable String eventId){
        AttendeeListResponseDTO attendeeListResponse = this.attendeeService.getEventAttendees(eventId);
        return ResponseEntity.ok(attendeeListResponse);
    }

    @Operation(summary = "Creates an event.", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Event created successfully!"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters."),
            @ApiResponse(responseCode = "409", description = "Event already exists!"),
    })
    @PostMapping
    public ResponseEntity<EventIdDTO> createEvent(@RequestBody EventRequestDTO body, UriComponentsBuilder uriComponentsBuilder){
        EventIdDTO eventIdDTO = this.eventService.createEvent(body);

        var uri = uriComponentsBuilder.path("/events/{id}").buildAndExpand(eventIdDTO.eventId()).toUri();

        return ResponseEntity.created(uri).body(eventIdDTO);
    }

    @Operation(summary = "Creates an attendee.", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Attendee created successfully!"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters."),
            @ApiResponse(responseCode = "409", description = "Attendee already exists!"),
    })
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
