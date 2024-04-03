package rocketseat.com.passin.dtos.attendee;

public record AttendeeBadgeDTO(
        String name,
        String email,
        String checkInUrl,
        String eventId
        ) {
}
