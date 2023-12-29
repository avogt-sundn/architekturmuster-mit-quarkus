package avogt.quarkus.organisationskatalog;

import java.util.Set;

import lombok.Data;

@Data
public class Organisation {
    Long id;

    String name;
    String nummer;
    String beschreibung;
    Set<Adresse> adressen;
}
