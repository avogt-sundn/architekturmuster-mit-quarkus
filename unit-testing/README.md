# Unit testing

Wir unterscheiden zwei Arten von Tests

1. Reine Unit-Tests
   - erkennbar durch das Fehlen von Annotations an der Test-Klasse
2. Quarkus-Tests
   - erkennbar durch die Annotation `@QuarkusTest`

Beide Sind JUnit5-Tests, die mit VS Code, maven oder dem Quarkus CLI ausgeführt werden können.

- Testmethoden müssen mit `@Test` annotiert sein.

## Quarkus Test Framework

 `@QuarkusTest` ist eine Annotation, die den Test in einem Quarkus-Container ausführt.

 Damit lassen sich alle Quarkus-Features wie CDI, JAX-RS, Hibernate, Panache, RESTEasy, und viele mehr testen.

### Vorsicht vor Instanzvariablen

 Felder in der Testklasse sollten nur verwendet werden, wenn der lifecycle von JUnit5 und RestAssured sowie Quarkus selber klar ist.

 Die Regel lautet: lieber in der Test-Methode lokale Variablen verwenden.

 Die Testklasse unter [src/test/java/restassured_specs/TestableRestResourceTest.java](TestableRestResourceTest)
 dokumentiert ein Fehlverhalten, wenn Instanzvariablen verwendet werden:

- in der Methode `_MitSpecInStatic()` wird der RequestSpecBuilder verwendet, um eine RequestSpec zu bauen. Dieser Builder nutzt den Kontext, in dem auch der Pfad zum Rest-Endpunkt zu setzen ist.
- es wird die `TestHTTPEndpoint` annotation verwendet, um den Endpunkt auf den Pfad der zu testenden Resource Klasse zu setzen.
- arbeitet der Builder zu früh, ist der Pfad noch leer. Danach wird er leider auch nicht mehr von Quarkus richtig gesetzt, so dass der Test fehlschlägt.

## Continuous testing mit dem Quarkus Dev Mode

Sinnvoll ist es, dass die tests auch ihre Standardausgabe auch im Dev Mode auf der Konsole ausgeben.
Diese Option spart das aktivieren der Ausgabe durch die Tastaturkombination `Ctrl+O` :

```properties
quarkus.test.display-test-output=true
````
