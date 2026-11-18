import { createContext } from 'react'
import type { Usuario } from '../types'

export interface AuthContextValue {
  usuario: Usuario | null
  login: (correo: string, password: string) => Promise<Usuario>
  logout: () => void
}

export const AuthContext = createContext<AuthContextValue | null>(null)
