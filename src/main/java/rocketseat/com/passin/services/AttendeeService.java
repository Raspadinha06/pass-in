package rocketseat.com.passin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import rocketseat.com.passin.domain.attendee.Attendee;
import rocketseat.com.passin.domain.attendee.exceptions.AttendeeAlreadyExistsException;
import rocketseat.com.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import rocketseat.com.passin.domain.checkin.CheckIn;
import rocketseat.com.passin.dtos.attendee.AttendeeBadgeDTO;
import rocketseat.com.passin.dtos.attendee.AttendeeBadgeResponseDTO;
import rocketseat.com.passin.dtos.attendee.AttendeeDetailDTO;
import rocketseat.com.passin.dtos.attendee.AttendeeListResponseDTO;
import rocketseat.com.passin.repositories.AttendeeRepository;
import rocketseat.com.passin.repositories.CheckInRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {
    private final AttendeeRepository attendeeRepository;
    private final CheckInService checkInService;

    public List<Attendee> getAllAttendeesFromEvent(String eventId){
        return this.attendeeRepository.findByEventId(eventId);
    }

    public AttendeeListResponseDTO getEventAttendees(String eventId){
        List<Attendee> attendeeList = this.getAllAttendeesFromEvent(eventId);

        List<AttendeeDetailDTO> attendeeDetailList =
                attendeeList.stream().map(attendee -> {
                    Optional<CheckIn> checkIn = this.checkInService.getCheckIn(attendee.getId());
                    LocalDateTime checkedInAt = checkIn.isPresent() ?
                            checkIn.get().getCreatedAt() : null;
                    return new AttendeeDetailDTO(attendee.getId(), attendee.getName(), attendee.getEmail(),
                            attendee.getCreatedAt(), checkedInAt);
                }).toList();

        return new AttendeeListResponseDTO(attendeeDetailList);
    }

    public void verifyAttendeeSubscription(String eventId, String email){
        Optional<Attendee> isAttendeeRegistered = this.attendeeRepository.findByEventIdAndEmail(eventId, email);
        if(isAttendeeRegistered.isPresent())
            throw new AttendeeAlreadyExistsException("Attendee already exists!");
    }

    public Attendee registerAttendee(Attendee newAttendee){
        this.attendeeRepository.save(newAttendee);
        return newAttendee;
    }

    public void checkInAttendee(String attendeeId){
        Attendee attendee = this.getAttendee(attendeeId);
        this.checkInService.checkIn(attendee);
    }

    public AttendeeBadgeResponseDTO getAttendeeBadge(String attendeeId, UriComponentsBuilder uriComponentsBuilder){
        Attendee attendee = this.getAttendee(attendeeId);

        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/check-in")
                .buildAndExpand(attendeeId).toUri().toString();

        AttendeeBadgeDTO badgeDTO = new AttendeeBadgeDTO(attendee.getName(),
                attendee.getEmail(), uri , attendee.getEvent().getId());
        return new AttendeeBadgeResponseDTO(badgeDTO);
    }

    private Attendee getAttendee(String attendeeId){
        return this.attendeeRepository.findById(attendeeId)
                .orElseThrow(() -> new AttendeeNotFoundException("Could not find attendee with ID: "
                        + attendeeId));
    }
}
