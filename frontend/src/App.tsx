import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom'
import { ProtectedRoute } from './auth/ProtectedRoute'
import { rutaInicioDe } from './auth/rutas'
import { useAuth } from './auth/useAuth'
import { MainLayout } from './layouts/MainLayout'
import { LoginPage } from './pages/LoginPage'
import { NoAutorizadoPage } from './pages/NoAutorizadoPage'
import { AdminPage } from './pages/admin/AdminPage'
import { MisTramitesPage } from './pages/estudiante/MisTramitesPage'
import { BandejaPage } from './pages/secretario/BandejaPage'
import { AvalesPendientesPage } from './pages/subdecano/AvalesPendientesPage'

function Inicio() {
  const { usuario } = useAuth()
  return <Navigate to={usuario ? rutaInicioDe(usuario.roles) : '/login'} replace />
}

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/no-autorizado" element={<NoAutorizadoPage />} />
        <Route path="/" element={<Inicio />} />

        <Route element={<ProtectedRoute />}>
          <Route element={<MainLayout />}>
            <Route element={<ProtectedRoute roles={['ESTUDIANTE']} />}>
              <Route path="/estudiante" element={<MisTramitesPage />} />
            </Route>
            <Route element={<ProtectedRoute roles={['SECRETARIO']} />}>
              <Route path="/secretario" element={<BandejaPage />} />
            </Route>
            <Route element={<ProtectedRoute roles={['SUBDECANO']} />}>
              <Route path="/subdecano" element={<AvalesPendientesPage />} />
            </Route>
            <Route element={<ProtectedRoute roles={['ADMIN']} />}>
              <Route path="/admin" element={<AdminPage />} />
            </Route>
          </Route>
        </Route>

        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  )
}
