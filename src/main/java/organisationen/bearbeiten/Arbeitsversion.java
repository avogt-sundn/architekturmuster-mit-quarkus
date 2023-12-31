package organisationen.bearbeiten;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "arbeitsversion")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
class Arbeitsversion extends PanacheEntityBase {

    @Id
    @GeneratedValue
    Long id;

    @Column(nullable = false)
    Long organisationId;

    @JdbcTypeCode(value = SqlTypes.JSON)
    String jsonString;
}