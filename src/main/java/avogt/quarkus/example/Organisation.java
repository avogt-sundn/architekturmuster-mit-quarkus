package avogt.quarkus.example;

import java.util.HashSet;
import java.util.Set;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "organisation")
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Organisation extends PanacheEntityBase {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    Long id;

    @Column
    String name;
    @Column
    String nummer;
    @Column
    String beschreibung;

    /**
     * Eine Organisation besitzt ein oder mehrere Adressen.
     */
    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @Builder.Default
    Set<Adresse> adressen = new HashSet<>();

    public void addAdresse(Adresse adresse) {
        this.adressen.add(adresse);
    }
}