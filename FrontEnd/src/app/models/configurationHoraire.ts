export interface ConfigurationHoraire {
  id?: number;
  jour: string; // Exemple : 'MONDAY'
  heureDebut: string; // Format 'HH:mm'
  heureFin: string;
  estOuvert: boolean;
}
