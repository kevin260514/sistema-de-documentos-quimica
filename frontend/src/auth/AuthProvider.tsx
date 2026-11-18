import { useCallback, useMemo, useState, type ReactNode } from 'react'
import { api } from '../api/client'
import type { LoginRequest, LoginResponse, Usuario } from '../types'
import { AuthContext } from './context'
import { clearSession, getStoredUser, saveSession } from './session'

export function AuthProvider({ children }: { children: ReactNode }) {
  const [usuario, setUsuario] = useState<Usuario | null>(() => getStoredUser())

  const login = useCallback(async (correo: string, password: string) => {
    const body: LoginRequest = { correo, password }
    const respuesta = await api.post<LoginResponse>('/auth/login', body)
    saveSession(respuesta.token, respuesta.usuario)
    setUsuario(respuesta.usuario)
    return respuesta.usuario
  }, [])

  const logout = useCallback(() => {
    clearSession()
    setUsuario(null)
  }, [])

  const value = useMemo(() => ({ usuario, login, logout }), [usuario, login, logout])

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}
