package pk_composite;

import org.hibernate.annotations.GenericGenerator;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@IdClass(KatalogId.class)
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KatalogEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(generator = "katalog-generator")
    @GenericGenerator(name = "katalog-generator", type = KatalogIdGenerator.class)
    Long id;

    @Id
    @Default // auch bei Benutzung des builders wird der Default-Wert auf false gesetzt
    Boolean isArbeitsversion = false;

    String eintrag;

    public Boolean isHauptversion() {
        return this.isArbeitsversion == null || !this.isArbeitsversion;
    }

}