package quarkitecture.registration.customer;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class CustomerRegistration extends PanacheEntity {

    @Column(unique = true)
    public String name;
    @Temporal(TemporalType.TIMESTAMP)
    public LocalDateTime timestamp;
}
