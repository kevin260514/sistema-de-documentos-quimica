import { zodResolver } from '@hookform/resolvers/zod'
import { useState } from 'react'
import { useForm } from 'react-hook-form'
import { Navigate, useNavigate } from 'react-router-dom'
import { z } from 'zod'
import { ApiError } from '../api/client'
import { rutaInicioDe } from '../auth/rutas'
import { useAuth } from '../auth/useAuth'

const esquema = z.object({
  correo: z.string().min(1, 'Ingresa tu correo').email('Correo no válido'),
  password: z.string().min(1, 'Ingresa tu contraseña'),
})

type Formulario = z.infer<typeof esquema>

export function LoginPage() {
  const { usuario, login } = useAuth()
  const navigate = useNavigate()
  const [error, setError] = useState<string | null>(null)
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<Formulario>({ resolver: zodResolver(esquema) })

  if (usuario) return <Navigate to={rutaInicioDe(usuario.roles)} replace />

  async function enviar(datos: Formulario) {
    setError(null)
    try {
      const u = await login(datos.correo, datos.password)
      navigate(rutaInicioDe(u.roles), { replace: true })
    } catch (e) {
      setError(
        e instanceof ApiError && e.status === 401
          ? 'Correo o contraseña incorrectos'
          : 'No se pudo iniciar sesión. Intenta de nuevo.',
      )
    }
  }

  return (
    <div className="flex min-h-screen items-center justify-center bg-slate-50 px-4">
      <form
        onSubmit={handleSubmit(enviar)}
        className="w-full max-w-sm space-y-4 rounded-xl border border-slate-200 bg-white p-6 shadow-sm"
        noValidate
      >
        <div>
          <h1 className="text-xl font-semibold">Pasantías</h1>
          <p className="text-sm text-slate-600">Facultad de Química · UCE</p>
        </div>

        <div className="space-y-1">
          <label htmlFor="correo" className="text-sm font-medium">
            Correo institucional
          </label>
          <input
            id="correo"
            type="email"
            autoComplete="username"
            className="w-full rounded-md border border-slate-300 px-3 py-2"
            {...register('correo')}
          />
          {errors.correo && <p className="text-sm text-red-600">{errors.correo.message}</p>}
        </div>

        <div className="space-y-1">
          <label htmlFor="password" className="text-sm font-medium">
            Contraseña
          </label>
          <input
            id="password"
            type="password"
            autoComplete="current-password"
            className="w-full rounded-md border border-slate-300 px-3 py-2"
            {...register('password')}
          />
          {errors.password && <p className="text-sm text-red-600">{errors.password.message}</p>}
        </div>

        {error && <p className="text-sm text-red-600">{error}</p>}

        <button
          type="submit"
          disabled={isSubmitting}
          className="w-full rounded-md bg-slate-900 px-3 py-2 font-medium text-white hover:bg-slate-700 disabled:opacity-60"
        >
          {isSubmitting ? 'Ingresando…' : 'Ingresar'}
        </button>
      </form>
    </div>
  )
}
