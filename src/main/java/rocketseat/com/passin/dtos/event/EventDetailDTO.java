package rocketseat.com.passin.dtos.event;

public record EventDetailDTO(String id, String title, String details,
                             String slug,
                             Integer maximumAttendees, Integer attendeesAmount){}
