package organisationen.bearbeiten;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "arbeitsversion")
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class ArbeitsversionEntity extends PanacheEntity {

    @Column(unique = true, nullable = false, columnDefinition = "uuid")
    UUID fachschluessel;

    @Enumerated(EnumType.ORDINAL)
    Status status;

    @JdbcTypeCode(value = SqlTypes.JSON)
    String jsonString;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

}