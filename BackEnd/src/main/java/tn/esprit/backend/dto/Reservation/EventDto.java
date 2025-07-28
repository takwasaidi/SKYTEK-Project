package tn.esprit.backend.dto.Reservation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {
    private Integer id;
    private String title;
    private String start;
    private String end;
    private String salle;
    private String user;
}
