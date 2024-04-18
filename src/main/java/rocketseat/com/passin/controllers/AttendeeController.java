package rocketseat.com.passin.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import rocketseat.com.passin.dtos.attendee.AttendeeBadgeResponseDTO;
import rocketseat.com.passin.services.AttendeeService;
import rocketseat.com.passin.services.CheckInService;

@RestController
@RequestMapping("/attendees")
@RequiredArgsConstructor
@Tag(name = "pass-in")
public class AttendeeController{
    private final AttendeeService attendeeService;

    @Operation(summary = "Displays the badge of an attendee.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attendee badge found successfully!"),
            @ApiResponse(responseCode = "404", description = "Badge not found."),
            @ApiResponse(responseCode = "500", description = "Data search error."),
    })
    @GetMapping("{attendeeId}/badge")
    public ResponseEntity<AttendeeBadgeResponseDTO> getAttendeeBadge(@PathVariable String attendeeId, UriComponentsBuilder uriComponentsBuilder){
        AttendeeBadgeResponseDTO response = this.attendeeService.getAttendeeBadge(attendeeId, uriComponentsBuilder);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Check into an event.", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Checked in successfully!"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters."),
            @ApiResponse(responseCode = "409", description = "Already checked in!"),
    })
    @PostMapping("/{attendeeId}/check-in")
    public ResponseEntity<Object> registerCheckIn(@PathVariable String attendeeId, UriComponentsBuilder uriComponentsBuilder){
        this.attendeeService.checkInAttendee(attendeeId);

        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/badge").buildAndExpand(attendeeId).toUri();

        return ResponseEntity.created(uri).build();
    }
}
