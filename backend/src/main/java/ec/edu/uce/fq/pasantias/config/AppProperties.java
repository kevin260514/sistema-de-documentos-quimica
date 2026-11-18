package ec.edu.uce.fq.pasantias.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** Propiedades propias de la aplicacion (prefijo "app" en application.yml). */
@ConfigurationProperties(prefix = "app")
public record AppProperties(Jwt jwt, Storage storage) {

    public record Jwt(String secret, int expiracionMinutos) {
    }

    public record Storage(String path, int maxArchivoMb) {
    }
}
