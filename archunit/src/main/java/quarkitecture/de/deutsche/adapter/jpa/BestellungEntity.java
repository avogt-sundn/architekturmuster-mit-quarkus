package quarkitecture.de.deutsche.adapter.jpa;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class BestellungEntity {
    @Id
    @GeneratedValue
    Long id;

    @Column(unique = true)
    String uid;

    @Column(nullable = false)
    LocalDateTime datum;

    public BestellungEntity(String bestellung) {
    }

}
