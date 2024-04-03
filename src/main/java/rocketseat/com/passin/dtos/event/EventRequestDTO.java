package rocketseat.com.passin.dtos.event;

public record EventRequestDTO (
        String title,
        String details,
        Integer maximumAttendees){
}
