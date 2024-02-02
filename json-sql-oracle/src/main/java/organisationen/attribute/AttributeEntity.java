package organisationen.attribute;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.json.bind.annotation.JsonbTypeSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Query;
import jakarta.persistence.Table;

@Table(name = "attribute")
@Entity
/**
 * Damit die Felder nach JSON serialisiert werden, müssen sie entweder public
 * sein, oder getter haben (am einfachsten mit @Data an der Klasse)!
 */
// @Data
public class AttributeEntity extends PanacheEntity {

    @JdbcTypeCode(value = SqlTypes.JSON)
    @Column(name = "json_string")
    @JsonbTypeSerializer(value = StringWithJsonSerializer.class)
    public String jsonString;

    @Column(name = "simple")
    public String simple;

    @JsonbDateFormat(JsonbDateFormat.DEFAULT_FORMAT)
    public LocalDateTime createdAt = LocalDateTime.now();

    public static AttributeEntity findFirstByKeyValue(String key, String value) {

        String sql = "select * from attribute WHERE json_string->>'" + key + "' like '%" + value + "%'";
        Query query = getEntityManager().createNativeQuery(sql, AttributeEntity.class);
        List<?> result = query.getResultList();
        return (AttributeEntity) result.get(0);
    }

}
