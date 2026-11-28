package ec.edu.uce.fq.pasantias.domain.workflow;

import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static ec.edu.uce.fq.pasantias.domain.workflow.EstadoTramite.*;
import static ec.edu.uce.fq.pasantias.domain.workflow.RolOperativo.*;

class MaquinasEstadosTest {
    record Transicion(MaquinaTramite.Evento evento, EstadoTramite origen, EstadoTramite destino, RolOperativo rol) {}
    @Test
    void matrizTramiteDeniegaTodoSalvoTransicionesExplicitas() {
        Set<Transicion> esperadas = new HashSet<>();
        String[] filas = {
            "T01 BORRADOR ENVIADA ESTUDIANTE",
            "T02 ENVIADA APROBADA SECRETARIO",
            "T03 ENVIADA OBSERVADA SECRETARIO",
            "T04 OBSERVADA ENVIADA ESTUDIANTE",
            "T05 OBSERVADA VENCIDA SISTEMA",
            "T06 APROBADA EVIDENCIAS_ENVIADAS ESTUDIANTE",
            "T07 APROBADA VENCIDA SISTEMA",
            "T08 EVIDENCIAS_ENVIADAS EVIDENCIAS_APROBADAS SECRETARIO",
            "T09 EVIDENCIAS_ENVIADAS EVIDENCIAS_OBSERVADAS SECRETARIO",
            "T10 EVIDENCIAS_OBSERVADAS EVIDENCIAS_ENVIADAS ESTUDIANTE",
            "T11 EVIDENCIAS_OBSERVADAS VENCIDA SISTEMA",
            "T12 EVIDENCIAS_APROBADAS AVAL_PENDIENTE_FIRMA SECRETARIO",
            "T13 AVAL_PENDIENTE_FIRMA FINALIZADA SUBDECANO",
            "T14 AVAL_PENDIENTE_FIRMA EVIDENCIAS_APROBADAS SUBDECANO",
            "T16 APROBADA OBSERVADA SECRETARIO",
            "T19 AVAL_PENDIENTE_FIRMA EVIDENCIAS_APROBADAS SECRETARIO"
        };
        for (String fila : filas) {
            String[] p = fila.split(" ");
            esperadas.add(new Transicion(MaquinaTramite.Evento.valueOf(p[0]), EstadoTramite.valueOf(p[1]), EstadoTramite.valueOf(p[2]), RolOperativo.valueOf(p[3])));
        }
        for (EstadoTramite origen : Set.of(BORRADOR,ENVIADA,OBSERVADA,APROBADA,EVIDENCIAS_OBSERVADAS)) {
            for (RolOperativo rol : Set.of(ESTUDIANTE,SECRETARIO))
                esperadas.add(new Transicion(MaquinaTramite.Evento.T17, origen,CANCELADA,rol));
        }
        for (var evento : MaquinaTramite.Evento.values())
            for (var origen : EstadoTramite.values())
                for (var destino : EstadoTramite.values())
                    for (var rol : RolOperativo.values()) {
                        var caso = new Transicion(evento,origen,destino,rol);
                        assertEquals(esperadas.contains(caso), MaquinaTramite.permite(evento,origen,destino,rol), caso.toString());
                    }
    }
    record TransicionAval(MaquinaAval.Evento evento, EstadoAval origen, EstadoAval destino, RolOperativo rol) {}
    @Test
    void matrizAvalDeniegaTodoSalvoTransicionesExplicitas() {
        var pendiente = EstadoAval.PENDIENTE_FIRMA;
        var esperadas = Set.of(
            new TransicionAval(MaquinaAval.Evento.A01,null,pendiente,SECRETARIO),
            new TransicionAval(MaquinaAval.Evento.A02,pendiente,EstadoAval.FIRMADO,SUBDECANO),
            new TransicionAval(MaquinaAval.Evento.A03,pendiente,EstadoAval.RECHAZADO,SUBDECANO),
            new TransicionAval(MaquinaAval.Evento.A04,pendiente,EstadoAval.ANULADO,SECRETARIO)
        );
        EstadoAval[] origenes = {null,pendiente,EstadoAval.FIRMADO,EstadoAval.RECHAZADO,EstadoAval.ANULADO};
        for (var evento : MaquinaAval.Evento.values())
            for (var origen : origenes)
                for (var destino : EstadoAval.values())
                    for (var rol : RolOperativo.values()) {
                        var caso = new TransicionAval(evento,origen,destino,rol);
                        assertEquals(esperadas.contains(caso), MaquinaAval.permite(evento,origen,destino,rol), caso.toString());
                    }
    }
}
