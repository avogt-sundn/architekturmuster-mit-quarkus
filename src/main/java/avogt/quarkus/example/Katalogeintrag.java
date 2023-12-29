package avogt.quarkus.example;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "katalogeintrag")
@Data
class Katalogeintrag {

    @Id
    @GeneratedValue
    Long id;

    @Column
    String name;
    @Column
    String nummer;
    @Column
    String beschreibung;

    /**
     *
     */
    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    Set<Adresse> adressen;
}

@Entity
@Table(name = "katalogeintrag")
@Data
class Adresse {

    @Id
    @GeneratedValue
    Long id;

    @Column
    String strasse;

    @Column
    Long postleitzahl;

}