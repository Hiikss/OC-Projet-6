export interface AuthenticatedUser {
  userId: string,
  email: string,
  username: string,
  accessToken: string,
  refreshToken: string,
}
