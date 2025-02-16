# Architekturmuster mit Quarkus

Dies ist ein Multi-module maven Projekt.
VS Code kann damit gut umgehen.

## Was ist im top level directory enthalten?

1. Der Workspace für VS Code:

   1. VS Code am besten in diesem Ordner öffnen.

   1. Der `java projects` view kann alle module untereinander darstellen.

   1. das [.devcontainer](.devcontainer) directory definiert das Setup der VS Code-Entwicklungsumgebung, das für alle Module einheitlich verwendet wird.

   1. das `.vscode` directory steuert einheitliche settings bei.

1. diese README.md gibt Orientierung und erlaubt, in die Themen zu navigieren.

## Wie ist die Struktur des Projekts?

Die oberste Ebene dieses git repository nimmt einzelne Projekte auf.

Jedes Projekt hat diese Eigenschaften:

* es kann eigenständig mit Maven gebaut werden
* es behandelt ein Thema, das sich leicht aus seinem `directory`-Namen ableiten läßt
* das pom führt die `<artifactId>` mit demselben Namen wie das directory, die `<groupId>` lautet `quarkitecture` ;-)
* im Projekt findet sich die `Java packages` Struktur:
  * es gibt *keinen* domain package prefix
  * oberste Packages benennen ein Unterthema,
  * das Unterthema ist vertikal geschnitten, deshalb referenziert es keine Klassen aus benachbarten Packages
    * das soll die Übernahme von Code vereinfachen, aber auch die Nachvollziehbarkeit erhöhen

## Ein neues Projekt anlegen

1. Benutze diesen Aufruf, um ein Quarkus Projekt zu generieren:

    ```bash
    quarkus create app quarkitecture:jpa-versioning --java=21 --wrapper
    ```

    * ein directory `jpa-versioning` wird erzeugt

1. Füge das Modul in das multi-module pom.xml ein

    ```bash
    <project xmlns="http://maven.apache.org/POM/4.0.0"
        ...
        <groupId>quarkitecture</groupId>
        <artifactId>architekturmuster-mit-quarkus</artifactId>
        ...
        <modules>
            <module>json-rest</module>
            ...
            <module>jpa-versioning</module>
        </modules>

    </project>
    ```

## Minimalismus-Prinzip

Wie ausführlich ein Aspekt ausgearbeitet werden soll, wollen diese Prinzipien steuern:

* Code soll minimal sein: wenn die Unit Tests auch nach Löschen einer Zeile erfolgreich laufen, sollte diese Zeile aus dem repository gelöscht werden
* default Werte sollten nicht explizit ausgeschrieben werden
* Minimalismen dürfen in Kommentaren oder `README.md`'s erläutert werden
* Code darf selbsterklärend sein, muss also nicht kommentiert werden
* Kommentare sollen im Quelltext die Besonderheiten der Implementierung kurz andeuten.
  * [hier zu den Kommentarbeispielen](./json-rest/src/main/java/json_payload_ohne_bean_binding/body/BodyResource.java)
* Gute Lösungsaspeke dürfen sich wiederholen in weiteren Projekten, der Fokus sollte aber auf dem neuen Aspekt liegen und den sich wiederholenden in seinem Ursprungsprojekt verlinken (vom README.md)
* 100% Testabdeckung
