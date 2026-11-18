import { Outlet, useNavigate } from 'react-router-dom'
import { useAuth } from '../auth/useAuth'

export function MainLayout() {
  const { usuario, logout } = useAuth()
  const navigate = useNavigate()

  function salir() {
    logout()
    navigate('/login', { replace: true })
  }

  return (
    <div className="min-h-screen bg-slate-50 text-slate-900">
      <header className="border-b border-slate-200 bg-white">
        <div className="mx-auto flex max-w-5xl flex-wrap items-center justify-between gap-2 px-4 py-3">
          <h1 className="text-base font-semibold sm:text-lg">
            Pasantías · Facultad de Química
          </h1>
          {usuario && (
            <div className="flex items-center gap-3 text-sm">
              <span className="text-slate-600">
                {usuario.nombres} {usuario.apellidos}
              </span>
              <button
                type="button"
                onClick={salir}
                className="rounded-md border border-slate-300 px-3 py-1 hover:bg-slate-100"
              >
                Salir
              </button>
            </div>
          )}
        </div>
      </header>
      <main className="mx-auto max-w-5xl px-4 py-6">
        <Outlet />
      </main>
    </div>
  )
}
