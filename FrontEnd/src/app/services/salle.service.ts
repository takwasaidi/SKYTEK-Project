import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Salle } from '../models/Salle';
import { ImageProcessingService } from './image-processing.service';
import { map } from 'rxjs/operators'; 
@Injectable({
  providedIn: 'root'
})
export class SalleService {

  private apiUrl = 'http://localhost:8087/api/salle';

  constructor(private http: HttpClient,private imageService: ImageProcessingService) {}

  getAll(): Observable<Salle[]> {
    return this.http.get<Salle[]>(this.apiUrl)
    .pipe(
       map((salles: any[]) => salles.map(salle => this.imageService.createImages(salle)))
    );
  }
  

  getById(id: number): Observable<Salle> {
    return this.http.get<Salle>(`${this.apiUrl}/${id}`);
    
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
