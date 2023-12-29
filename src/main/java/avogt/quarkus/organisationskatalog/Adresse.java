package avogt.quarkus.organisationskatalog;

import lombok.Data;

@Data
public class Adresse {
    Long id;
    String strasse;
    Integer postleitzahl;
    String stadt;
}
