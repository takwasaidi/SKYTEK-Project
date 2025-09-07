import { Entreprise } from './entreprise';
export interface User {
   id: number;
  firstname: string;
  lastname: string;
  email: string;
  phone:string;
  user_type: string;
  entreprise : Entreprise
}