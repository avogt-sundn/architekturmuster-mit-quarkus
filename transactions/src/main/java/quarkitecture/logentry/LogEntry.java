package quarkitecture.logentry;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class LogEntry extends PanacheEntity {

    public String message;
    @Temporal(TemporalType.TIMESTAMP)
    public LocalDateTime timestamp;
}
