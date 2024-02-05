# json-rest

Themen:

- .. wie ein REST API empfangene Daten verarbeitet
- .. Deserialisieren mit JSON-B (Jakarte Enterprise 10)
- .. json body eines POST/PUT/PATCH an den Java-Code übergeben
  - Serialisieren in ein String-Feld eines Entity bean
  - Serialisieren in einen String-Parameter der Resource-Methode

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