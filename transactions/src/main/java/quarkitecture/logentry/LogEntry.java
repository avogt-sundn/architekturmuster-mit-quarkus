package quarkitecture.logentry;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class LogEntry extends PanacheEntity {

    public String message;
    public LocalDateTime timestamp;
}
