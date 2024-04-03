package rocketseat.com.passin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rocketseat.com.passin.domain.attendee.Attendee;
import rocketseat.com.passin.domain.checkin.CheckIn;
import rocketseat.com.passin.dtos.events.attendees.AttendeeDetailDTO;
import rocketseat.com.passin.dtos.events.attendees.AttendeeListResponseDTO;
import rocketseat.com.passin.repositories.AttendeeRepository;
import rocketseat.com.passin.repositories.CheckInRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {
    private final AttendeeRepository attendeeRepository;
    private final CheckInRepository checkInRepository;

    public List<Attendee> getAllAttendeesFromEvent(String eventId){
        return this.attendeeRepository.findByEventId(eventId);
    }

    public AttendeeListResponseDTO getEventAttendees(String eventId){
        List<Attendee> attendeeList = this.getAllAttendeesFromEvent(eventId);

        List<AttendeeDetailDTO> attendeeDetailList =
                attendeeList.stream().map(attendee -> {
                    Optional<CheckIn> checkIn = this.checkInRepository
                            .findByAttendeeId(attendee.getId());
                    LocalDateTime checkedInAt = checkIn.isPresent() ?
                            checkIn.get().getCreatedAt() : null;
                    return new AttendeeDetailDTO(attendee.getId(), attendee.getName(), attendee.getEmail(),
                            attendee.getCreatedAt(), checkedInAt);
                }).toList();

        return new AttendeeListResponseDTO(attendeeDetailList);
    }
}
