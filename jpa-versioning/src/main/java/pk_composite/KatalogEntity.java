package pk_composite;

import org.hibernate.annotations.GenericGenerator;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
    // @GeneratedValue(strategy = GenerationType.TABLE)
    Long id;

    @Id
    Boolean isArbeitsversion;

    String eintrag;
}
