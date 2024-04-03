package rocketseat.com.passin.dtos.events;

public record EventDetailDTO(String id, String title, String details,
                             String slug,
                             Integer maximumAttendees, Integer attendeesAmount){}
