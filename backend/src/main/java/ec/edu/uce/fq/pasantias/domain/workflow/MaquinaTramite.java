package ec.edu.uce.fq.pasantias.domain.workflow;

import java.util.Objects;
import java.util.Set;
import static ec.edu.uce.fq.pasantias.domain.workflow.EstadoTramite.*;

 /**
  * Matriz estructural de transiciones v1.2. No autoriza por sí sola:
  * el caso de uso debe comprobar identidad, propiedad, suplencia, guardas
  * de negocio, concurrencia, auditoría y efectos en una transacción.
  */
public final class MaquinaTramite {
    private MaquinaTramite() {}
    public enum Evento {
        T01, T02, T03, T04, T05, T06, T07, T08, T09, T10,
        T11, T12, T13, T14, T15, T16, T17, T18, T19
    }
    public static boolean permite(Evento evento, EstadoTramite origen,
                                  EstadoTramite destino, RolOperativo rol) {
        Objects.requireNonNull(evento);
        Objects.requireNonNull(origen);
        Objects.requireNonNull(destino);
        Objects.requireNonNull(rol);
        return switch (evento) {
            case T01 -> coincide(origen,destino,rol,BORRADOR,ENVIADA,RolOperativo.ESTUDIANTE);
            case T02 -> coincide(origen,destino,rol,ENVIADA,APROBADA,RolOperativo.SECRETARIO);
            case T03 -> coincide(origen,destino,rol,ENVIADA,OBSERVADA,RolOperativo.SECRETARIO);
            case T04 -> coincide(origen,destino,rol,OBSERVADA,ENVIADA,RolOperativo.ESTUDIANTE);
            case T05 -> coincide(origen,destino,rol,OBSERVADA,VENCIDA,RolOperativo.SISTEMA);
            case T06 -> coincide(origen,destino,rol,APROBADA,EVIDENCIAS_ENVIADAS,RolOperativo.ESTUDIANTE);
            case T07 -> coincide(origen,destino,rol,APROBADA,VENCIDA,RolOperativo.SISTEMA);
            case T08 -> coincide(origen,destino,rol,EVIDENCIAS_ENVIADAS,EVIDENCIAS_APROBADAS,RolOperativo.SECRETARIO);
            case T09 -> coincide(origen,destino,rol,EVIDENCIAS_ENVIADAS,EVIDENCIAS_OBSERVADAS,RolOperativo.SECRETARIO);
            case T10 -> coincide(origen,destino,rol,EVIDENCIAS_OBSERVADAS,EVIDENCIAS_ENVIADAS,RolOperativo.ESTUDIANTE);
            case T11 -> coincide(origen,destino,rol,EVIDENCIAS_OBSERVADAS,VENCIDA,RolOperativo.SISTEMA);
            case T12 -> coincide(origen,destino,rol,EVIDENCIAS_APROBADAS,AVAL_PENDIENTE_FIRMA,RolOperativo.SECRETARIO);
            case T13 -> coincide(origen,destino,rol,AVAL_PENDIENTE_FIRMA,FINALIZADA,RolOperativo.SUBDECANO);
            case T14 -> coincide(origen,destino,rol,AVAL_PENDIENTE_FIRMA,EVIDENCIAS_APROBADAS,RolOperativo.SUBDECANO);
            // T15 se deniega aquí: requiere un caso de uso separado con doble autorización.
            case T15 -> false;
            case T16 -> coincide(origen,destino,rol,APROBADA,OBSERVADA,RolOperativo.SECRETARIO);
            case T17 -> destino == CANCELADA
                && Set.of(BORRADOR,ENVIADA,OBSERVADA,APROBADA,EVIDENCIAS_OBSERVADAS).contains(origen)
                && Set.of(RolOperativo.ESTUDIANTE,RolOperativo.SECRETARIO).contains(rol);
            // T18 requiere recuperar el estado previo real y verificar P-08.
            case T18 -> false;
            case T19 -> coincide(origen,destino,rol,AVAL_PENDIENTE_FIRMA,EVIDENCIAS_APROBADAS,RolOperativo.SECRETARIO);
        };
    }
    private static boolean coincide(EstadoTramite origen, EstadoTramite destino,
        RolOperativo rol, EstadoTramite esperado, EstadoTramite siguiente, RolOperativo autorizado) {
        return origen == esperado && destino == siguiente && rol == autorizado;
    }
}
