package id_mit_arbeitsversion;

public class FehlendesInterfaceException extends RuntimeException {

    public FehlendesInterfaceException(Object object) {
        super("Diese entity Klasse muss von " + UsingIdGenerator.class.getName() + " ableiten: " + object.getClass());
    }

}
