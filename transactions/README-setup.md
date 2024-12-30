#

## Mehrere datasources

Es müssen properties für die datasource als auch für Hibnerate ORM gesetzt werden:

-
      quarkus.datasource."greeting".db-kind=h2
      quarkus.datasource."greeting".jdbc.url=jdbc:h2:mem:greeting
      quarkus.datasource."greeting".username=sa
      quarkus.datasource."greeting".password=sa
      quarkus.datasource."greeting".jdbc.transactions=xa
-
      quarkus.hibernate-orm."greeting".packages=quarkitecture.greeting

  - Entity beans in dem package `quarkitecture.greeting` werden immer über die PersistenceUnit `greeting` behandelt.

-     quarkus.hibernate-orm."greeting".datasource=greeting

  - die PersistenceUnit wird nun mit der Datasource verbunden.

Damit wird diese Exception gelöst:

````bash
GreetingResourceTest#testHelloEndpoint() java.lang.RuntimeException: io.quarkus.builder.BuildException: Build failure: Build failed due to errors
        [error]: Build step io.quarkus.hibernate.orm.deployment.HibernateOrmProcessor#configurationDescriptorBuilding threw an exception: io.quarkus.runtime.configuration.ConfigurationException: Datasource must be defined for persistence unit 'greeting'. Refer to https://quarkus.io/guides/datasource for guidance.
        at io.quarkus.hibernate.orm.deployment.HibernateOrmProcessor.collectDialectConfig(HibernateOrmProcessor.java:1128)
        at io.quarkus.hibernate.orm.deployment.HibernateOrmProcessor.producePersistenceUnitDescriptorFromConfig(HibernateOrmProcessor.java:927)
        at io.quarkus.hibernate.orm.deployment.HibernateOrmProcessor.handleHibernateORMWithNoPersistenceXml(HibernateOrmProcessor.java:871)

````
- https://quarkus.io/guides/hibernate-orm#multiple-persistence-units