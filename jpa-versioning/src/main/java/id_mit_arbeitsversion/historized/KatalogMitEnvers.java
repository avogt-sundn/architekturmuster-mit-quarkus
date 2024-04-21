package id_mit_arbeitsversion.historized;

import java.util.UUID;

import id_mit_arbeitsversion.EntityBaseMitArbeitsversion;
import id_mit_arbeitsversion.IdMitArbeitsversion;
import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import lombok.EqualsAndHashCode;

@Entity()
@IdClass(IdMitArbeitsversion.class)
@EqualsAndHashCode(callSuper = false)
public class KatalogMitEnvers extends EntityBaseMitArbeitsversion {

    UUID fachlicherSchluessel;
    String eintrag;

}