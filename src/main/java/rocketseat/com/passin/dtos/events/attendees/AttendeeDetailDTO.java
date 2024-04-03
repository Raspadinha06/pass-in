package rocketseat.com.passin.dtos.events.attendees;

import java.time.LocalDateTime;

public record AttendeeDetailDTO(
        String id,
        String name,
        String email,
        LocalDateTime createdAt,
        LocalDateTime checkedInAt
) {
}
