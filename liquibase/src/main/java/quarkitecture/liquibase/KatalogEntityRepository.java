package quarkitecture.liquibase;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class KatalogEntityRepository implements PanacheRepository<KatalogEntity> {
    // Repository methods can be added here if needed
}
