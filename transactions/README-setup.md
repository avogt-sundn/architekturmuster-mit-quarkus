#

## Verteilte Transaktionen XA

Diese Voraussetzungen sind zu klären:

1. alle Treiber (jdbc, amqp, kafka ...) müssen XA unterstützen (also implementieren)
   - maximal eine Datasource kann auch ohne XA bedient werden als [letzte Instanz](https://www.narayana.io/docs/project/index.html#_last_resource_commit_optimization_lrco)
1. Der backend server muss XA fähig sein und dies auch richtig konfiguriert haben.
1. Quarkus muss auf jeder beteiligten datasource aktiviert sein,
- das passiert automatisch, sobald mehr als eine datasource in einer Transaktion auftritt
- besser noch man aktiviert XA explizit:
 `quarkus.datasource[.optional name].jdbc.transactions=xa`

## Mehrere datasources definieren und zuweisen

Es müssen properties für die datasource als auch für Hibnerate ORM gesetzt werden:

-
      quarkus.datasource."customer".db-kind=h2
      quarkus.datasource."customer".jdbc.url=jdbc:h2:mem:customer
      quarkus.datasource."customer".username=sa
      quarkus.datasource."customer".password=sa
      quarkus.datasource."customer".jdbc.transactions=xa
-
      quarkus.hibernate-orm."customer".packages=quarkitecture.customer

  - Entity beans in dem package `quarkitecture.customer` werden immer über die PersistenceUnit `customer` behandelt.

-     quarkus.hibernate-orm."customer".datasource=customer

  - die PersistenceUnit wird nun mit der Datasource verbunden.

Damit wird diese Exception gelöst:

````bash
customerResourceTest#testHelloEndpoint() java.lang.RuntimeException: io.quarkus.builder.BuildException: Build failure: Build failed due to errors
        [error]: Build step io.quarkus.hibernate.orm.deployment.HibernateOrmProcessor#configurationDescriptorBuilding threw an exception: io.quarkus.runtime.configuration.ConfigurationException: Datasource must be defined for persistence unit 'customer'. Refer to https://quarkus.io/guides/datasource for guidance.
        at io.quarkus.hibernate.orm.deployment.HibernateOrmProcessor.collectDialectConfig(HibernateOrmProcessor.java:1128)
        at io.quarkus.hibernate.orm.deployment.HibernateOrmProcessor.producePersistenceUnitDescriptorFromConfig(HibernateOrmProcessor.java:927)
        at io.quarkus.hibernate.orm.deployment.HibernateOrmProcessor.handleHibernateORMWithNoPersistenceXml(HibernateOrmProcessor.java:871)

````

#### Links

- Quarkus Guide
  - https://quarkus.io/guides/hibernate-orm#multiple-persistence-units

  - https://quarkus.io/guides/datasource#datasource-multiple-single-transaction
- last resource commit optimization (nicht XA arbeitet mit XAs zusammen):
  - https://www.narayana.io/docs/project/index.html#_last_resource_commit_optimization_lrco
