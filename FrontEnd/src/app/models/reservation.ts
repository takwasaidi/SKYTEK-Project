import { Salle } from "./Salle";
import { User } from "./user";

export interface Reservation {
    id?: number;
  user: User;
  salle: Salle;
  dateReservation: string; // format yyyy-MM-dd
  heureDebut: string;      // format HH:mm
  heureFin: string;        // format HH:mm
}
