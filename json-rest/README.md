# json-rest

Themen:

- .. wie ein REST API empfangene Daten verarbeitet
- .. Deserialisieren mit JSON-B (Jakarte Enterprise 10)
- .. json body eines POST/PUT/PATCH an den Java-Code übergeben
  - Serialisieren in ein String-Feld eines Entity bean
  - Serialisieren in einen String-Parameter der Resource-Methode
- Zalando Leitfaden und Prüfung mit zally-Maven-Plugin

## Ein POST body soll ganz oder teilweise als String deserialisiert werden

Das package: [json_payload_ohne_bean_binding](./src/main/java/json_payload_ohne_bean_binding/)

### a. POST body als String deserialisieren

- [FieldResource.java](./src/main/java/json_payload_ohne_bean_binding/field/FieldResource.java) hat die Methode

  ```java
  @POST
  public Response saveObjectFromJson(Organisation body) {}
  ```

### b. POST body in eine Java-Klasse deserialisieren

- [BodyResource.java](./src/main/java/json_payload_ohne_bean_binding/body/BodyResource.java)

  ```java
  @POST
  public Response saveObjectFromJson(String body) {}
  ```

## Zalando Rest API Guide und zally Plugin

Der Guide kann im Internet gefunden werden unter [https://opensource.zalando.com/restful-api-guidelines/](https://opensource.zalando.com/restful-api-guidelines/).

Das zally Plugin prüft den Code gegen ein vorliegendes openapi.yaml File. Damit die `openapi.yaml` von Quarkus generiert wird,

- muss das Plugin `quarkus-smallrye-openapi` in der `pom.xml` eingebunden sein
- und in der [src/main/resources/application.properties](./src/main/resources/application.properties) die Eigenschaft `quarkus.smallrye-openapi.path` auf den Pfad der `openapi.yaml` gesetzt sein.

    ```properties
    quarkus.smallrye-openapi.store-schema-directory=src/main/resources/openapi
    ```
