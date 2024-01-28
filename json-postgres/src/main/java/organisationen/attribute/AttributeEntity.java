package organisationen.attribute;

import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
/**
 * Damit die Felder nach JSON serialisiert werden, müssen sie entweder public
 * sein, oder getter haben (am einfachsten mit @Data an der Klasse)!
 */
// @Data
public class AttributeEntity extends PanacheEntity {

    @JdbcTypeCode(value = SqlTypes.JSON)
    @Column
    public String jsonString;

    @JsonbDateFormat(JsonbDateFormat.DEFAULT_FORMAT)
    public LocalDateTime createdAt = LocalDateTime.now();

}
