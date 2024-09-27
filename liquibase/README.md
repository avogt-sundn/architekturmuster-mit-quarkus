# liquibase

## Quarkus Projekt

Dieses Projekt benutzt diese extensions:

````bash
quarkus extension list
# 2024-09-02 09:02:36,042 WARN  [io.qua.boo.res.mav.wor.WorkspaceLoader] (ForkJoinPool.commonPool-worker-3) Module(s) under /workspaces/architekturmuster-mit-quarkus/getting-started will be handled as thirdparty dependencies because /workspaces/architekturmuster-mit-quarkus/getting-started/pom.xml does not exist
# Looking for the newly published extensions in registry.quarkus.io
# Current Quarkus extensions installed:
#
# ✬ ArtifactId                                         Extension Name
# ✬ quarkus-hibernate-orm-panache                      Hibernate ORM with Panache
# ✬ quarkus-jdbc-h2                                    JDBC Driver - H2
# ✬ quarkus-jdbc-postgresql                            JDBC Driver - PostgreSQL
# ✬ quarkus-liquibase                                  Liquibase
# ✬ quarkus-resteasy-reactive-jackson                  RESTEasy Reactive Jackson
#
# To get more information, append `--full` to your command line.
````

- h2 für schnelle in-memory datenbank Tests mit JPA
- postgres für eine containerisierte Datenbank im dev Modus

### Starten der Container-Datenbank postgresql

````bash
# starten
docker compose up -d postgres
# stoppen
docker compose down

````

## Das maven plugin für liquibase

grundlegende Konfiguration muss unter `<build><plugins></plugins>` so angelegt werden:

````xml
<build>
    <plugins>
        <plugin>
            <groupId>org.liquibase</groupId>
            <artifactId>liquibase-maven-plugin</artifactId>
            <version>4.29.1</version>
            <configuration>
            </configuration>
        </plugin>
````

Die Konfiguration des liquibase plugin im Abschnitt `<configuration>`:

````xml
<plugin>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-maven-plugin</artifactId>
    <version>4.29.1</version>
    <configuration>
        <url>jdbc:h2:file:./target/h2db;DB_CLOSE_DELAY=-1</url>
        <searchPath>${project.basedir}/src/main/resources</searchPath>
        <propertyFile>db/liquibase.properties</propertyFile>
        <changeLogFile>db/changelog/changesets/changeLog.xml</changeLogFile>
        <outputChangeLogFile>${project.basedir}/target/generated-changelog.xml</outputChangeLogFile>
    </configuration>
</plugin>
````

## Arbeiten mit liquibase zur Schemagenerierung


````bash
mvn liquibase:generateChangeLog
````

- schreibt in die Datei `target/generated-changelog.xml`
- der Name ist in der pom.xml konfiguriert

````bash
docker compose down
# zum löschen der Datenbank einfach Container löschen mit: down
docker compose up -d postgres
# dadurch wird hier eine neue Instanz erzeugt: up

mvn clean
# H2 und vormals erzeugtes log löschen
quarkus dev -Dquarkus.hibernate-orm.database.generation=drop-and-create
# Schema durch Hibernate anlegen lassen

mvn liquibase:generateChangeLog
````

## Einblick in postgres: psql Befehle auf Kommandozeile

Um in die Datenbank hineinzuschauen, muss gegeben sein:

1. die Datenbank läuft
2. der `psql` Befehl kann von der Kommandozeile aus gestartet werden
3. Ausgaben erscheinen auf der Kommandozeile

Starte `psql` im Container mit dem `exec` Befehl von `docker`:

````bash
docker compose exec postgres bash
# root@a011eca056c9:/#
````

Danach kann mit dem Befehl `psql` eine interaktive SQL-Befehlszeile gestartet werden. Das neue prompt erlaubt nun SQL direkt an die Datenbank zu schicken:

- Parameter `-U`: der username
- Parameter `-d`: der Datenbankname

Postgres gruppiert Tabellen in Schemata, und Schemata finden sich in Datenbanken, In einem Schema liegen dann die SQL Tabellen.

- Der user `postgres` ist der sogenannte root und deshalb umfassend berechtigt, auf alle Schemata und Datenbanken zuzugreifen,
- definiert wurde er [hier](docker-compose.yml):

    ````bash
    # docker-compose.yml
    services:
    postgres:
        environment:
        POSTGRES_USER: postgres
        POSTGRES_PASSWORD: password
        POSTGRES_DB: "katalog"
    ````

````bash
# application.properties:
quarkus.datasource.jdbc.url=jdbc:postgresql://${DB_HOST:localhost}:5432/katalog
# der Datenbankname lautet: katalog
````


- die Umgebungsvariable DB_HOST wird in der vscode Konfiguration gesetzt und berücksichtigt, ob die postgres Datenbank unter localhost oder per docker network unter ihrem hostname `postgres` zu erreichen ist.


Mit diesen Informationen starten wir die SQL-Befehlszeile:

````bash
root@a011eca056c9:/# psql -d katalog -U postgres
# psql (14.1 (Debian 14.1-1.pgdg110+1))
# Type "help" for help.

katalog=#
````

und können nun in die Datenbankstruktur schauen:

- Listet alle Tabellen im Schema auf:

    ````bash
    \d
                    List of relations
    Schema |         Name          | Type  |  Owner
    --------+-----------------------+-------+----------
    public | databasechangelog     | table | postgres
    public | databasechangeloglock | table | postgres
    public | hibernate_sequences   | table | postgres
    public | katalogentity         | table | postgres
    (4 rows)
    ````

- zeigt die Spalten einer Tabelle samt Datentypen:


    ````bash
    \d katalogentity
    #                        Table "public.katalogentity"
    #      Column     |          Type          | Collation | Nullable | Default
    # ----------------+------------------------+-----------+----------+---------
    #  id             | bigint                 |           | not null |
    #  eintrag        | character varying(255) |           |          |
    #  zweitereintrag | character varying(255) |           |          |
    # Indexes:
    #     "katalogentity_pkey" PRIMARY KEY, btree (id)
    # ````

 Weitere Befehle finden sich z.B. hier:

- <https://www.devart.com/dbforge/postgresql/studio/postgres-list-schemas.html#what-is-schema>
- ****

## Arbeiten mit GUI Admin-Wekzeug 'pgadmin'

So zeigt pgadmin die Struktur der Datenbank:

![alt text](.images/SCR-20240903-ooj.png)

- starte den Container mit

    ````bash
    docker compose up -d pgadmin
    ````

- öffne im Browser unter <http://localhost:9008/browser/>
- Logindaten kommn von [hier](docker-compose.yml):

    ````yaml
    pgadmin:
        image: dpage/pgadmin4
        environment:
        PGADMIN_DEFAULT_EMAIL: admin@admin.com
        PGADMIN_DEFAULT_PASSWORD: root
    ````

- klick auf das Icon mit dem Titel `Add New Server`:
  - General
    - Name: z.b. `local`
  - Connection (müssen übereinstimmen mit denem im [docker-compose.yml](docker-compose.yml)):
    - Host name/adress: `postgres` (wenn im Devcontainer arbeitend)
    - Username: `postgres`
    - Password: `password`
  - ![alt text](.images/SCR-20240903-ox7.png)

## H2 im Test

Im Projekt ist eine H2 als Test-Datenbank eingerichtet. Sie wird benutzt, wenn unit tests in der IDE oder mit `mvn test`ausgeführt werden.

- in der `application-test.properties` finden sich diese Einträge dazu.

Die Einträge gehören zum `test`-Profil, da sie in einer Datei mit dem Muster

- `application-{profil}.properties`

enthalten sind. Der Präfix `%test.` kann **und muss** innerhalb der so benannten Datei entfallen.

## Quarkus dev-Profil mit H2 oder Postgres

Das `dev`-Profil wird mit H2 gestartet, dabei ist diese H2 datei-basiert.
Es benutzt liquibase und testet so das geschriebene changeset gegen H2.
Hibernate validiert lediglich danach, führt aber selber keine Schemaänderung aus.

````bash
quarkus dev
# mvn quarkus:dev

# nutzt also H2 persistent, abgelegt in der Datei im target Ordner
mvn clean; quarkus dev
# löscht die Datenbanb und legt H2 neu an, rollt liquibase aus

quarkus dev -Dquarkus.profile=prod -Dquarkus.test.profile=prod
# startet den Service gegen die postgres Datenbank
# und läßt Unit Tests gegen dieselbe postgres laufen
````


## Initialisierung im Image

 Der Postgres Container bietet die Möglichkeit, psql-Kommandos zum Start in einem bash-Skript einzuschleusen.
Im Rahmen der container-basierten Entwicklung machen wir das in der [docker-compose.yml](docker-compose.yml), wo die image-Referenz ersetzt wird durch ein [build-file](src/test/docker/postgres/Dockerfile), welches unser [init-Skript](src/test/docker/postgres/init-postgres.sh) einbaut.

## Schemaänderungen durchführen

Arbeitsweise:

1. Kodiere neue Entities oder anderen JPA Code.
2. Kodiere die Schema-Änderungen in die [changeLog.xml](src/main/resources/db/changeLog.xml)
3. Führe die Liquibase Schemaänderungen auf der Zieldatenbank aus.
4. Starte den Quarkus Service.

Nun gibt es Varianten und Hilfsmittel:

Das Rücksetzen der Datenbank ist am einfachsten mit dem Kommando
`(docker compose down;docker compose up -d postgres)`.

2.a Erzeuge das Schema mit hibernate, generiere ein changeLog.xml zu bestehendem Schema

````bash
mvn test -Dquarkus.test.profile=prod,update
mvn liquibase:generateChangeLog
cp target/generated-changelog.xml src/main/resources/db/changeLog.xml
````

validate:

- Caused by: org.hibernate.tool.schema.spi.SchemaManagementException: Schema-validation: missing column [eintra2g] in table [KatalogEntity]

### Umgang mit H2 und Postgres Inkompatibilitäten

Im Beispielcode findet sich ein `@Lob String`, der also auf eine große Spalte abgebildet werden soll. Liquibase muss hier differenzieren:

- h2 will als SQL Typ 'CHARACTER LARGE OBJECT', im changeset generiert liquibase dazu den Typ 'text'
- postgres will als SQL Typ 'OID', das generierte `text`wird leider nicht darauf abgebildet.

Darum werden getrennt changesets angelegt in [002-langertext.xml](src/main/resources/db/changelog/changesets/002-langertext.xml). In den Profilen werden nun unterschiedliche labels aktiviert, die die changesets auswählen:

- `quarkus.liquibase.labels=postgres-changeset-only`

- wählt alle changesets mit dem label *zusätzlich* zu den nicht markierten aus:

    ````xml
    <changeSet author="team-architekturmuster" id="3" labels="postgres-changeset-only">
        <addColumn tableName="katalogentity">
            <column name="langertext" type="oid" />
        </addColumn>
    </changeSet>
    ````

### troubleshooting

- [ERROR] Error setting up or running Liquibase:
[ERROR] liquibase.exception.LiquibaseException: liquibase.exception.MigrationFailedException: Migration failed for changeset changeLog.xml::1725208825608-1::vscode (generated):
[ERROR]      Reason: liquibase.exception.DatabaseException: ERROR: relation "hibernate_sequences" already exists [Failed SQL: (0) CREATE TABLE hibernate_sequences (next_val BIGINT, sequence_name VARCHAR(255) NOT NULL, CONSTRAINT hibernate_sequences_pkey PRIMARY KEY (sequence_name))]
  - hier hilft ein mvn liquibase:changelogSync
  - Ursache kann sein, dass in der databasechangelog Tabelle die changes mit verschiedenen filenames auftauchen:

    ````
        katalog=# select * from databasechangelog;
        id        |       author       |     filename     |        dateexecuted        | orderexecuted | exectype |               md
    5sum               |                description                | comments | tag | liquibase | contexts | labels | deployment_id
    -----------------+--------------------+------------------+----------------------------+---------------+----------+-----------------
    -------------------+-------------------------------------------+----------+-----+-----------+----------+--------+---------------
    1725208825608-1 | vscode (generated) | db/changeLog.xml | 2024-09-16 17:29:31.872321 |             1 | EXECUTED | 9:b9668c95a312c4
    d7dbaaef738b0f7dfb | createTable tableName=hibernate_sequences |          |     | 4.25.1    |          |        | 6507771831
    1725208825608-2 | vscode (generated) | db/changeLog.xml | 2024-09-16 17:29:31.891662 |             2 | EXECUTED | 9:3284275c30003a
    692f29975518a0c338 | createTable tableName=katalogentity       |          |     | 4.25.1    |          |        | 6507771831
    zwei            | avogt              | db/changeLog.xml | 2024-09-16 17:29:31.905749 |             3 | EXECUTED | 9:9e5e9ed52ae04a
    a84aa3ed4c1e213748 | addColumn tableName=katalogentity         |          |     | 4.25.1    |          |        | 6507771831
    1725208825608-1 | vscode (generated) | changeLog.xml    | 2024-09-16 17:36:26.281936 |             4 | EXECUTED | 9:b9668c95a312c4
    d7dbaaef738b0f7dfb | createTable tableName=hibernate_sequences |          |     | 4.25.1    |          |        | 6508186297
    1725208825608-2 | vscode (generated) | changeLog.xml    | 2024-09-16 17:36:26.309986 |             5 | EXECUTED | 9:3284275c30003a
    692f29975518a0c338 | createTable tableName=katalogentity       |          |     | 4.25.1    |          |        | 6508186297
    zwei            | avogt              | changeLog.xml    | 2024-09-16 17:36:26.313523 |             6 | EXECUTED | 9:9e5e9ed52ae04a
    a84aa3ed4c1e213748 | addColumn tableName=katalogentity         |          |     | 4.25.1    |          |        | 6508186297
    (6 rows)
    ````

- java.lang.IllegalStateException: The named datasource 'default-reactive' has not been properly configured. See <https://quarkus.io/guides/datasource#multiple-datasources> for information on how to do that.
- `[error]: Build step io.quarkus.hibernate.orm.deployment.HibernateOrmProcessor#configurationDescriptorBuilding threw an exception: io.quarkus.runtime.configuration.ConfigurationException: Datasource must be defined for persistence unit 'h2test'. Refer to https://quarkus.io/guides/datasource for guidance.`
  - `quarkus.hibernate-orm."pg".datasource=pg`
  - Lösung: sobald mehr als eine persistenceunit benutzt wird, muss diese konfigurativ eindeutig mit einer definierten datasource verbunden werden!
- `[error]: Build step io.quarkus.hibernate.orm.deployment.HibernateOrmProcessor#configurationDescriptorBuilding threw an exception: io.quarkus.runtime.configuration.ConfigurationException: Packages must be configured for persistence unit 'guitars'.`
  - ....
- `[ERROR] liquibase.exception.LiquibaseException: liquibase.exception.CommandValidationException: Output ChangeLogFile '/workspaces/architekturmuster-mit-quarkus/liquibase/target/generated-changelog.xml' already exists!`
  - einfach die Datei vorher löschen, denn das Kommando `mvn liquibase:generateChangeLog` vermeidet das stille Überschreiben der in er pom.xml deklarierten Ausgabedatei.

## Links

- Offizielles Docker image für postgres:
  - <https://hub.docker.com/_/postgres/>
  - darin beschrieben, wie man mit einem [init script](./src/test/docker/postgres/init-postgres.sh) eine zweite Datenbank erzeugt.

- liquibase diffs
  - <https://www.liquibase.com/blog/liquibase-diffs#what-types-of-diff-based-commands-are-available-in-liquibase>
- postgres
  - <https://postgresql-tutorial.com/postgresql-how-to-list-all-columns-of-a-table/>
