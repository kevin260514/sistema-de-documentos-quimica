import type { Rol } from '../types'

// Pagina de inicio de cada rol (se usa tras el login y en la ruta "/").
export const RUTA_INICIO: Record<Rol, string> = {
  ESTUDIANTE: '/estudiante',
  SECRETARIO: '/secretario',
  SUBDECANO: '/subdecano',
  ADMIN: '/admin',
}

export function rutaInicioDe(roles: Rol[]): string {
  const primero = roles[0]
  return primero ? RUTA_INICIO[primero] : '/login'
}
