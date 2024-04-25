package id_mit_arbeitsversion.historized;

import java.util.UUID;

import id_mit_arbeitsversion.EntityBaseMitArbeitsversion;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class KatalogMitEnvers extends EntityBaseMitArbeitsversion {

    UUID fachlicherSchluessel;
    String eintrag;

}