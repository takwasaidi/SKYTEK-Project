import { Component } from '@angular/core';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-mfa',
  templateUrl: './mfa.component.html',
  styleUrls: ['./mfa.component.css']
})
export class MfaComponent {
 email: string = '';
  qrCodeUrl: string = '';
  mfaCode!: number;
token: string | null | undefined = null;

  errorMessage: string = '';
  
  constructor(private authService: AuthenticationService) {}

  // generateQrCode() {
  //   this.authService.getMfaSetup(this.email).subscribe({
  //     next: (otpAuthUrl: string) => {
  //       const encoded = encodeURIComponent(otpAuthUrl);
  //       this.qrCodeUrl = `https://api.qrserver.com/v1/create-qr-code/?data=${encoded}&size=200x200`;
  //     },
  //     error: () => {
  //       this.errorMessage = 'Erreur lors de la génération du QR code.';
  //     }
  //   });
  // }

//  verifyCode() {
//   this.authService.verifyMfaCode(this.email, this.mfaCode).subscribe({
//     next: (response) => {
//       this.token = response.accessToken;
//       localStorage.setItem('token', this.token!);  // Ajoute ça pour stocker le token
//       alert('MFA validé avec succès. Token stocké.');
//     },
//     error: () => {
//       this.errorMessage = 'Code MFA invalide.';
//     }
//   });
// }
}
