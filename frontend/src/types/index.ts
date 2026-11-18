export type Rol = 'ESTUDIANTE' | 'SECRETARIO' | 'SUBDECANO' | 'ADMIN'

export interface Usuario {
  id: number
  nombres: string
  apellidos: string
  correo: string
  roles: Rol[]
}

export interface LoginRequest {
  correo: string
  password: string
}

export interface LoginResponse {
  token: string
  usuario: Usuario
}

// Debe coincidir con el enum EstadoPasantia del backend.
export type EstadoPasantia =
  | 'BORRADOR'
  | 'ENVIADA'
  | 'OBSERVADA'
  | 'APROBADA'
  | 'EVIDENCIAS_ENVIADAS'
  | 'EVIDENCIAS_OBSERVADAS'
  | 'EVIDENCIAS_APROBADAS'
  | 'AVAL_PENDIENTE_FIRMA'
  | 'FINALIZADA'
  | 'VENCIDA'
