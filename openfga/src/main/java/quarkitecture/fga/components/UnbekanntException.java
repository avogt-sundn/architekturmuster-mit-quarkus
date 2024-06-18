package quarkitecture.fga.components;

public class UnbekanntException extends RuntimeException {

    public UnbekanntException(String string) {
        super("Unbekannt ist hier " + string);
    }

}
