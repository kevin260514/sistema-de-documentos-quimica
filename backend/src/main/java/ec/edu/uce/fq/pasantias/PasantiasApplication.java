package ec.edu.uce.fq.pasantias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class PasantiasApplication {

    public static void main(String[] args) {
        SpringApplication.run(PasantiasApplication.class, args);
    }
}
