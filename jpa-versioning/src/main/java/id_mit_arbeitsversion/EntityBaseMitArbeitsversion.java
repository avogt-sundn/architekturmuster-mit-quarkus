package id_mit_arbeitsversion;

import org.hibernate.annotations.GenericGenerator;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@IdClass(IdMitArbeitsversion.class)
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class EntityBaseMitArbeitsversion extends PanacheEntityBase implements UsingIdGenerator {

    @Id
    @GeneratedValue(generator = "pkcomposite-generator")
    @GenericGenerator(name = "pkcomposite-generator", type = IdGenerator.class)
    Long id;

    @Id
    Boolean arbeitsversion = false;

    public Boolean isHauptversion() {
        return this.arbeitsversion == null || !this.arbeitsversion;
    }

}