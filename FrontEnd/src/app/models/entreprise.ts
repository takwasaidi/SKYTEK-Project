export interface Quota {
  id?: number;
  quota: number;
  quotaUtilise: number;
}

export interface Entreprise {
  id?: number;
  nom: string;
  email: string;
  quota?: Quota; // <-- Note que quota est un objet ici, pas un number directement
}
