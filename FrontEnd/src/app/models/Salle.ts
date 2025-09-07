import { FileHandle } from "./file-handle";

export interface Salle {
  id?: number;
  nom: string;
  capacite: number;
  tarifHoraire: number;
  emplacement: string;
  titre: string;
  description:string;
  estDisponible: boolean;
  enMaitenance: boolean;
  equipmentIds: number[];
  salleImages: FileHandle[];
}

