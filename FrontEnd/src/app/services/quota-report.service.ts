import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class QuotaReportService {

   private apiUrl = 'http://localhost:8087/api/reporting/quota/excel';

  constructor(private http: HttpClient) { }

  downloadExcelReport() {
    return this.http.get(this.apiUrl, { responseType: 'blob' });
  }
}
