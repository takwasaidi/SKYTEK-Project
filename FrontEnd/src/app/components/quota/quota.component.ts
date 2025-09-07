import { Component, OnInit } from '@angular/core';
import { QuotaService } from '../../services/quota.service';
import { EntrepriseQuotaDto } from 'src/app/models/entrepriseQuotaDto';
import { QuotaReportService } from 'src/app/services/quota-report.service';

@Component({
  selector: 'app-quota',
  templateUrl: './quota.component.html',
  styleUrls: ['./quota.component.css']
})

export class QuotaComponent implements OnInit{

entreprises: EntrepriseQuotaDto[] = [];
  editingId: number | null = null;
  editingQuota: number | null = null;
  loading = false;
  errorMsg = '';
   showModal = false; // <-- Add this

  constructor(private quotaService: QuotaService,private quotaReportService: QuotaReportService) {}

  ngOnInit(): void {
    this.load();
  }

  load() {
    this.loading = true;
    this.quotaService.getAll().subscribe({
      next: data => { this.entreprises = data; this.loading = false; },
      error: err => { this.errorMsg = 'Erreur chargement'; this.loading = false; console.error(err); }
    });
  }

  startEdit(e: EntrepriseQuotaDto) {
    this.editingId = e.entrepriseId;
    this.editingQuota = e.quota;
  }

  cancelEdit() {
    this.editingId = null;
    this.editingQuota = null;
  }

  saveEdit(entrepriseId: number) {
    if (this.editingQuota == null) return;
    this.quotaService.updateQuota(entrepriseId, this.editingQuota).subscribe({
      next: () => { this.load(); this.cancelEdit(); },
      error: err => { alert('Erreur mise à jour'); console.error(err); }
    });
  }

  resetUsage(entrepriseId: number) {
    if (!confirm('Réinitialiser le quota utilisé pour cette entreprise ?')) return;
    this.quotaService.resetUsage(entrepriseId).subscribe({
      next: () => { this.load(); },
      error: err => { alert('Erreur reset'); console.error(err); }
    });
  }
  reservations: any[] = [];
selectedEntrepriseName = '';

voirReservations(entrepriseId: number) {
  this.quotaService.getReservations(entrepriseId).subscribe({
    next: data => {
      this.reservations = data;
      const entreprise = this.entreprises.find(e => e.entrepriseId === entrepriseId);
      this.selectedEntrepriseName = entreprise ? entreprise.name : '';
         this.showModal = true; // <-- Use the flag

    },
    error: err => console.error(err)
  });
}

  closeModal() {
    this.showModal = false; // <-- Close by changing flag
  }






  downloadReport() {
    this.quotaReportService.downloadExcelReport().subscribe(blob => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'quota_report.xlsx';
      a.click();
      window.URL.revokeObjectURL(url);
    }, error => {
      console.error('Erreur lors du téléchargement du rapport', error);
    });
  }

}
