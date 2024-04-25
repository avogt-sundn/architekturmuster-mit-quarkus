package id_mit_arbeitsversion;

import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class MyPkCompositeEntity extends EntityBaseMitArbeitsversion {

    String stadt;

    @Builder
    public MyPkCompositeEntity(String stadt, Boolean arbeitsversion) {
        this.stadt = stadt;
        this.arbeitsversion = arbeitsversion;
    }

}
