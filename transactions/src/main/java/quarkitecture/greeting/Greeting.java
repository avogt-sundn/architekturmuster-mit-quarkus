package quarkitecture.greeting;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Greeting extends PanacheEntity {

    @Column(unique = true)
    public String name;
    public LocalDateTime timestamp;
}
