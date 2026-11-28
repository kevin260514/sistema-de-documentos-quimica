package ec.edu.uce.fq.pasantias.domain.workflow;

import java.util.Objects;
import static ec.edu.uce.fq.pasantias.domain.workflow.EstadoAval.*;

public final class MaquinaAval {
    private MaquinaAval() {}
    public enum Evento { A01, A02, A03, A04, A05 }
    // Matriz estructural; las guardas, persistencia y permisos se comprueban aparte.
    public static boolean permite(Evento evento, EstadoAval origen,
                                  EstadoAval destino, RolOperativo rol) {
        Objects.requireNonNull(evento);
        Objects.requireNonNull(destino);
        Objects.requireNonNull(rol);
        return switch (evento) {
            case A01 -> origen == null && destino == PENDIENTE_FIRMA && rol == RolOperativo.SECRETARIO;
            case A02 -> origen == PENDIENTE_FIRMA && destino == FIRMADO && rol == RolOperativo.SUBDECANO;
            case A03 -> origen == PENDIENTE_FIRMA && destino == RECHAZADO && rol == RolOperativo.SUBDECANO;
            case A04 -> origen == PENDIENTE_FIRMA && destino == ANULADO && rol == RolOperativo.SECRETARIO;
            // A05 requiere autorización doble y transición T15 atómica.
            case A05 -> false;
        };
    }
}
