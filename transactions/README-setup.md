#

## Mehrere datasources

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
- https://quarkus.io/guides/hibernate-orm#multiple-persistence-units