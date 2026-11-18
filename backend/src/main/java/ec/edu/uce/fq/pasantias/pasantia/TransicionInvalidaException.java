package ec.edu.uce.fq.pasantias.pasantia;

public class TransicionInvalidaException extends RuntimeException {

    public TransicionInvalidaException(EstadoPasantia origen, EstadoPasantia destino) {
        super("No se puede pasar de " + origen + " a " + destino);
    }
}
