export interface Reservation {
    id?: number;
  userId: number;
  salleId: number;
  dateReservation: string; // format yyyy-MM-dd
  heureDebut: string;      // format HH:mm
  heureFin: string;        // format HH:mm
}
