export interface Salle {
  id?: number;
  nom: string;
  capacite: number;
  tarifHoraire: number;
  emplacement: string;
  imagesUrls: string[];
  estDisponible: boolean;
  enMaitenance: boolean;
  equipmentIds: number[];
}
