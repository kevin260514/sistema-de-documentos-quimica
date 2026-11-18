package ec.edu.uce.fq.pasantias.pasantia;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;

class EstadoPasantiaTest {

    @Test
    void flujoCompletoSinObservaciones() {
        List<EstadoPasantia> flujo = List.of(
            EstadoPasantia.BORRADOR,
            EstadoPasantia.ENVIADA,
            EstadoPasantia.APROBADA,
            EstadoPasantia.EVIDENCIAS_ENVIADAS,
            EstadoPasantia.EVIDENCIAS_APROBADAS,
            EstadoPasantia.AVAL_PENDIENTE_FIRMA,
            EstadoPasantia.FINALIZADA);

        for (int i = 0; i < flujo.size() - 1; i++) {
            assertThat(flujo.get(i).puedePasarA(flujo.get(i + 1)))
                .as("%s -> %s", flujo.get(i), flujo.get(i + 1))
                .isTrue();
        }
    }

    @Test
    void solicitudObservadaPuedeReenviarse() {
        assertThat(EstadoPasantia.ENVIADA.puedePasarA(EstadoPasantia.OBSERVADA)).isTrue();
        assertThat(EstadoPasantia.OBSERVADA.puedePasarA(EstadoPasantia.ENVIADA)).isTrue();
    }

    @Test
    void avalRechazadoVuelveAlSecretario() {
        assertThat(EstadoPasantia.AVAL_PENDIENTE_FIRMA.puedePasarA(EstadoPasantia.EVIDENCIAS_APROBADAS)).isTrue();
    }

    @Test
    void noSePuedenSaltarPasos() {
        assertThat(EstadoPasantia.BORRADOR.puedePasarA(EstadoPasantia.APROBADA)).isFalse();
        assertThat(EstadoPasantia.ENVIADA.puedePasarA(EstadoPasantia.FINALIZADA)).isFalse();
        assertThat(EstadoPasantia.APROBADA.puedePasarA(EstadoPasantia.AVAL_PENDIENTE_FIRMA)).isFalse();
    }

    @Test
    void estadosFinalesNoTienenSalida() {
        assertThat(EstadoPasantia.FINALIZADA.esFinal()).isTrue();
        assertThat(EstadoPasantia.VENCIDA.esFinal()).isTrue();
        assertThat(EstadoPasantia.BORRADOR.esFinal()).isFalse();
    }

    @Test
    void validarTransicionLanzaExcepcionSiNoEsValida() {
        assertThatThrownBy(() -> EstadoPasantia.FINALIZADA.validarTransicion(EstadoPasantia.ENVIADA))
            .isInstanceOf(TransicionInvalidaException.class)
            .hasMessageContaining("FINALIZADA")
            .hasMessageContaining("ENVIADA");
    }

    @Test
    void todoEstadoNoFinalTieneAlMenosUnaSalida() {
        for (EstadoPasantia estado : EstadoPasantia.values()) {
            if (!estado.esFinal()) {
                assertThat(estado.siguientes()).isNotEmpty();
            }
        }
    }
}
