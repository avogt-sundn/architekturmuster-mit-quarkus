# Zum Thema "welcher json Parser" - JSON-B oder Jackson

## Bevorzugte Nutzung von JSON-B

Das Quarkus Projekt nutzt jakarta json binding (JSON-B), gesteuert durch diese dependency:

````xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-resteasy-reactive-jsonb</artifactId>
</dependency>
````

> Jackson ist nicht eingebunden und soll auch nicht genutzt werden.

## Ein Fehler tritt auf: RestAssured wirft eine Exception im Test

````java
 Test OrganisationenBearbeitenResourceTest#ChangeStatus() failed
: java.lang.NoSuchMethodError: 'javax.json.bind.JsonbBuilder org.eclipse.yasson.JsonBindingProvider.create()'
        at io.restassured.internal.RestAssuredResponseOptionsImpl.as(RestAssuredResponseOptionsImpl.java:169)
        at organisationen.bearbeiten.OrganisationenBearbeitenResourceTest.ChangeStatus(OrganisationenBearbeitenResourceTest.java:121)
````

- RestAssured will den jsonb parser benutzten (da jackson nicht im classpath ist).
- Es importiert aber noch die alte `javax.json.bin.*` packages, die im classpath fehlen, da Quarkus selber seit Version 3.0 hier die JEE10 packages unter `jakarta.json.bind.*` nutzt.

Der Fehler wird provoziert durch diesen Test-Abschnitt

````java
Arbeitsversion s = given().with().contentType(ContentType.JSON)
        .body(jsonb.toJson(organisation))
        .when().get(organisation.getFachschluessel() + "/draft")
        .then().statusCode(equalTo(HttpStatus.SC_OK))
        .and().rootPath("organisation").extract().as(Arbeitsversion.class);
````

- `extract().as(Arbeitsversion.class)` führt eine Deserialisierung des in der http response empfangenen json Fragments in eine Instanz der Java-Klasse durch
- dabei wird ein Parser eingesetzt, abhängig davon, welche Parser auf dem classpath liegen

## Lösung: Verzicht auf `extract().as(Class<> type)`

````java
String s = given().with().contentType(ContentType.JSON)
        .body(jsonb.toJson(organisation))
        .when().get(organisation.getFachschluessel() + "/draft")
        .then().statusCode(equalTo(HttpStatus.SC_OK))
        .and().rootPath("organisation").extract().asPrettyString();
Arbeitsversion orgExtracted = jsonb.fromJson(s, Arbeitsversion.class);
````

## Lösung: smallrye-OpenAPI extension aufnehmen

````xml
 <dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-openapi</artifactId>
</dependency>
````

.. dann gibt RestAssured Fehler bei der Deserialisierung von Datumsstempeln diesen Fehler:

````java
Test OrganisationenBearbeitenResourceTest#ChangeStatus() failed
: java.lang.RuntimeException: com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Java 8 date/time type `java.time.LocalDateTime` not supported by default: add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to enable handling
 at [Source: (String)"{"createdAt":"2024-01-08T13:19:42.37801","fachschluessel":"8fb4de3e-9d9c-4b40-9b2e-4b90efe5119e","id":2751,"organisation":{"adressen":[{"id":2912,"postleitzahl":10117,"stadt":"Berlin","strasse":"Charitéplatz 1"}],"beschreibung":"ChangeStatus","fachschluessel":"8fb4de3e-9d9c-4b40-9b2e-4b90efe5119e","id":2912,"name":"1"}}"; line: 1, column: 14] (through reference chain: organisationen.bearbeiten.Arbeitsversion["createdAt"])
````

- offensichtlich ist durch die openapi dependency auch **jackson im classpath aufgetaucht und wird deshalb von RestAssured eingesetzt!**

### Die Lösung: die jackson dependencies für Datumsstempel sind auch zu ergänzen

Ergänze in der pom.xml

````xml
 <dependency>
    <groupId>com.fasterxml.jackson.datatype</groupId>
    <artifactId>jackson-datatype-jdk8</artifactId>
    <version>2.10.0.pr1</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.datatype</groupId>
    <artifactId>jackson-datatype-jsr310</artifactId>
    <version>2.10.0.pr1</version>
    <scope>test</scope>
</dependency>
````
