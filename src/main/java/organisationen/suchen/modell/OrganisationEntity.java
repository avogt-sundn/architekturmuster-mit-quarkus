package organisationen.suchen.modell;

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
public class OrganisationEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    public Long id;

    @Column
    public String name;
    @Column
    public String nummer;
    @Column
    public String beschreibung;

    /**
     * Eine Organisation besitzt ein oder mehrere Adressen.
     */
    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @Builder.Default
    public Set<AdresseEntity> adressen = new HashSet<>();

    public void addAdresse(AdresseEntity adresse) {
        this.adressen.add(adresse);
    }
}