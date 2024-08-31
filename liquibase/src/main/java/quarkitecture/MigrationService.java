package quarkitecture;

import java.util.List;

import io.quarkus.liquibase.LiquibaseFactory;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import liquibase.Liquibase;
import liquibase.changelog.ChangeSetStatus;
import liquibase.exception.LiquibaseException;

@ApplicationScoped
public class MigrationService {
    // You can Inject the object if you want to use it manually
    private final LiquibaseFactory liquibaseFactory;

    @Inject
    public MigrationService(LiquibaseFactory liquibaseFactory) {
        this.liquibaseFactory = liquibaseFactory;
    }

    public void checkMigration() throws LiquibaseException {
        // Get the list of liquibase change set statuses
        try (Liquibase liquibase = liquibaseFactory.createLiquibase()) {
            List<ChangeSetStatus> status = liquibase.getChangeSetStatuses(liquibaseFactory.createContexts(),
                    liquibaseFactory.createLabels());
            Log.info("Liquibase status: " + status);
        }
    }
}