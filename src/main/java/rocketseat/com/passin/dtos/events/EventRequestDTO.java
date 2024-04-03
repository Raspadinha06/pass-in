package rocketseat.com.passin.dtos.events;

public record EventRequestDTO (
        String title,
        String details,
        Integer maximumAttendees){
}
