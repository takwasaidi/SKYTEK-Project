import { Injectable } from '@angular/core';
import { User } from '../models/user';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

export interface FichierDTO {
  id?: number;
  nomFichier: string;
  typeMime: string;
}

export interface ReclamationDTO {
  id?: number;
  sujet: string;
  description: string;
  fichiers?: FichierDTO[];
  user: User;
}

@Injectable({
  providedIn: 'root'
})
export class ReclamationService {
 private apiUrl = 'http://localhost:8087/api/reclamation';

  constructor(private http: HttpClient) {}

  addReclamation(
    sujet: string,
    description: string,
    fichiers?: File[]
  ): Observable<ReclamationDTO> {
    const formData = new FormData();
    formData.append('sujet', sujet);
    formData.append('description', description);

    if (fichiers) {
      fichiers.forEach(file => formData.append('fichiers', file));
    }

    return this.http.post<ReclamationDTO>(this.apiUrl, formData);
  }

  getAllReclamations() {
  return this.http.get<any[]>(this.apiUrl);
  }
   getAllByUser() {
    return this.http.get<any[]>(`${this.apiUrl}/allByUser`);
   }

downloadFile(id: number, filename: string) {
  return this.http.get(`${this.apiUrl}/files/${id}`, { 
    responseType: 'blob' 
  }).pipe(
    tap((blob: Blob) => {
      const a = document.createElement('a');
      const objectUrl = URL.createObjectURL(blob);
      a.href = objectUrl;
      a.download = filename;
      a.click();
      URL.revokeObjectURL(objectUrl);
    })
  );
}
  // Fonction pour annuler une réclamation
  cancelReclamation(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }



}
