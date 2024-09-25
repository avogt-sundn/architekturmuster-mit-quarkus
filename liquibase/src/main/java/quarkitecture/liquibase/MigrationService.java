package quarkitecture.liquibase;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MigrationService {

    // // You can Inject the object if you want to use it manually
    // private final LiquibaseFactory liquibaseFactory;

    // @Inject
    // public MigrationService(LiquibaseFactory liquibaseFactory) {
    // this.liquibaseFactory = liquibaseFactory;
    // }

    // public void checkMigration() throws LiquibaseException {
    // // Get the list of liquibase change set statuses
    // try (Liquibase liquibase = liquibaseFactory.createLiquibase()) {
    // List<ChangeSetStatus> status =
    // liquibase.getChangeSetStatuses(liquibaseFactory.createContexts(),
    // liquibaseFactory.createLabels());
    // Log.info("Liquibase status: " + status);
    // }
    // }
}
