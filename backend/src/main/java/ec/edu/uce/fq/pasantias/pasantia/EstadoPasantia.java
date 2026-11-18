package ec.edu.uce.fq.pasantias.pasantia;

import java.util.EnumSet;
import java.util.Set;

/**
 * Maquina de estados del tramite de pasantia.
 * Es la unica fuente de verdad: el frontend nunca decide transiciones.
 * Debe coincidir con el CHECK de la columna pasantia.estado (migracion V1).
 */
public enum EstadoPasantia {
    BORRADOR,
    ENVIADA,
    OBSERVADA,
    APROBADA,
    EVIDENCIAS_ENVIADAS,
    EVIDENCIAS_OBSERVADAS,
    EVIDENCIAS_APROBADAS,
    AVAL_PENDIENTE_FIRMA,
    FINALIZADA,
    VENCIDA;

    /** Estados a los que se puede llegar desde este. */
    public Set<EstadoPasantia> siguientes() {
        return switch (this) {
            case BORRADOR -> EnumSet.of(ENVIADA);
            case ENVIADA -> EnumSet.of(APROBADA, OBSERVADA);
            case OBSERVADA -> EnumSet.of(ENVIADA, VENCIDA);
            case APROBADA -> EnumSet.of(EVIDENCIAS_ENVIADAS);
            case EVIDENCIAS_ENVIADAS -> EnumSet.of(EVIDENCIAS_APROBADAS, EVIDENCIAS_OBSERVADAS);
            case EVIDENCIAS_OBSERVADAS -> EnumSet.of(EVIDENCIAS_ENVIADAS, VENCIDA);
            case EVIDENCIAS_APROBADAS -> EnumSet.of(AVAL_PENDIENTE_FIRMA);
            // Si el subdecano rechaza el aval, vuelve al secretario (RN27).
            case AVAL_PENDIENTE_FIRMA -> EnumSet.of(FINALIZADA, EVIDENCIAS_APROBADAS);
            case FINALIZADA, VENCIDA -> EnumSet.noneOf(EstadoPasantia.class);
        };
    }

    public boolean puedePasarA(EstadoPasantia destino) {
        return siguientes().contains(destino);
    }

    /** Lanza {@link TransicionInvalidaException} si la transicion no esta permitida. */
    public void validarTransicion(EstadoPasantia destino) {
        if (!puedePasarA(destino)) {
            throw new TransicionInvalidaException(this, destino);
        }
    }

    public boolean esFinal() {
        return siguientes().isEmpty();
    }
}
