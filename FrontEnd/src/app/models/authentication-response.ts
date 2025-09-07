export interface AuthenticationResponse {
  accessToken?: string;
  refreshToken?: string;
   mfaRequired?: boolean;
   secretImageUri?: string; // ✅ pour le QR Code MFA (Google Authenticator)
}