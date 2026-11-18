import type { Usuario } from '../types'

// La sesion vive solo mientras la pestana esta abierta (sessionStorage).
const TOKEN_KEY = 'pasantias.token'
const USER_KEY = 'pasantias.usuario'

export function getToken(): string | null {
  try {
    return sessionStorage.getItem(TOKEN_KEY)
  } catch {
    return null
  }
}

export function getStoredUser(): Usuario | null {
  try {
    const raw = sessionStorage.getItem(USER_KEY)
    return raw ? (JSON.parse(raw) as Usuario) : null
  } catch {
    return null
  }
}

export function saveSession(token: string, usuario: Usuario): void {
  try {
    sessionStorage.setItem(TOKEN_KEY, token)
    sessionStorage.setItem(USER_KEY, JSON.stringify(usuario))
  } catch {
    // almacenamiento no disponible
  }
}

export function clearSession(): void {
  try {
    sessionStorage.removeItem(TOKEN_KEY)
    sessionStorage.removeItem(USER_KEY)
  } catch {
    // almacenamiento no disponible
  }
}
