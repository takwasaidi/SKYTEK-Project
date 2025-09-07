export interface ReservationInfoDto {
  id: number;
  dateReservation: string; // format: 'YYYY-MM-DD'
  heureDebut: string;      // format: 'HH:mm'
  heureFin: string;        // format: 'HH:mm'
  salle: string;           // Nom ou code de la salle
  utilisateur: string;     // Nom ou email de l'utilisateur
  status: string;          // ex: "VALIDEE", "EN_ATTENTE", etc.
}
