export interface EntrepriseQuotaDto {
  entrepriseId: number;
  name: string;
  email: string;
  quota: number;
  quotaUtilise: number;
  quotaRestant: number;
  quotaDepasse: boolean;
  montantDepassement: number;
}