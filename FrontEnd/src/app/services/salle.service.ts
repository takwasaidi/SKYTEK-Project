import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Salle } from '../models/Salle';

@Injectable({
  providedIn: 'root'
})
export class SalleService {

  private apiUrl = 'http://localhost:8087/api/salle';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Salle[]> {
    return this.http.get<Salle[]>(this.apiUrl);
  }

  getById(id: number): Observable<Salle> {
    return this.http.get<Salle>(`${this.apiUrl}/${id}`);
  }

  /* create(salle: Salle): Observable<Salle> {
    return this.http.post<Salle>(this.apiUrl, salle);
  } */
    create(salle: Salle, images: File[]): Observable<any> {
  const formData = new FormData();

  formData.append('nom', salle.nom);
  formData.append('capacite', salle.capacite.toString());
  formData.append('tarifHoraire', salle.tarifHoraire.toString());
  formData.append('emplacement', salle.emplacement);
  formData.append('estDisponible', salle.estDisponible.toString());
  formData.append('enMaitenance', salle.enMaitenance.toString());

  salle.equipmentIds.forEach(id => {
    formData.append('equipmentIds', id.toString());
  });

  images.forEach((file) => {
    formData.append('images', file);
  });

  return this.http.post(`${this.apiUrl}`, formData);
}

  update(id: number, salle: Salle): Observable<Salle> {
    return this.http.put<Salle>(`${this.apiUrl}/${id}`, salle);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
 addSalleWithImages(salle: Salle, images: File[], equipmentIds: number[]): Observable<any> {
  const formData = new FormData();

  // Ajouter l'objet salle en JSON
  formData.append('salle', new Blob([JSON.stringify(salle)], {
    type: 'application/json'
  }));

  // Ajouter les images
  images.forEach(img => {
    formData.append('imagesFile', img, img.name);
  });

  // Ajouter les IDs des équipements
  equipmentIds.forEach(id => {
    formData.append('equipmentIds', id.toString());
  });

  return this.http.post(this.apiUrl, formData, {
    reportProgress: true,
    observe: 'events'
  });
}

public addSalle(salle: FormData) {
  return this.http.post<Salle>(this.apiUrl, salle);
}

}
