export interface RegisterRequest {
  firstname?: string;
  lastname?: string;
  email?: string;
  phone?: string;
  password?: string;
  role?: string;
  mfaEnabled?: string;
  entrepriseId?: number;
}