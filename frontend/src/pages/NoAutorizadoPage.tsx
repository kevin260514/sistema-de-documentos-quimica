import { Link } from 'react-router-dom'

export function NoAutorizadoPage() {
  return (
    <div className="flex min-h-screen flex-col items-center justify-center gap-3 px-4 text-center">
      <h1 className="text-2xl font-semibold">Sin permiso</h1>
      <p className="text-slate-600">Tu rol no tiene acceso a esta página.</p>
      <Link to="/" className="underline">
        Volver al inicio
      </Link>
    </div>
  )
}
