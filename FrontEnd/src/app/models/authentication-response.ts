export interface AuthenticationResponse {
  accessToken?: string;
  refreshToken?: string;
   mfaRequired?: boolean;
}