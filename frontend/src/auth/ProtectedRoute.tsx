import { Navigate, Outlet } from 'react-router-dom'
import type { Rol } from '../types'
import { useAuth } from './useAuth'

interface Props {
  /** Roles que pueden entrar. Si se omite, basta con haber iniciado sesion. */
  roles?: Rol[]
}

export function ProtectedRoute({ roles }: Props) {
  const { usuario } = useAuth()

  if (!usuario) return <Navigate to="/login" replace />
  if (roles && !usuario.roles.some((rol) => roles.includes(rol))) {
    return <Navigate to="/no-autorizado" replace />
  }
  return <Outlet />
}
