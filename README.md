# Architekturmuster mit Quarkus

Dies ist ein Multi-module maven Projekt.
VS Code kann damit gut umgehen.

## Was ist im top level directory enthalten?

1. Der Workspace für VS Code:

   1. VS Code am besten in diesem Ordner öffnen.

   1. Der `java projects` view kann alle module untereinander darstellen.

   1. das [.devcontainer](.devcontainer) directory definiert das Setup der VS Code-Entwicklungsumgebung, das für alle Module einheitlich verwendet wird.
   1.  der `.vscode` directory

1. diese README.md gibt Orientierung und erlaubt, in die Themen zu navigieren.

##  Wie ist die Struktur des Projekts?

Die oberste Ebene dieses git repository nimmt einzelne Projekte auf.

Jedes Projekt hat diese Eigenschaften:

* es kann eigenständig mit Maven gebaut werden
* es behandelt ein Thema, das sich leicht aus seinem `directory`-Namen ableiten läßt
* das pom führt die `<groupId>` mit demselben Namen wie das directory
* im Projekt findet sich die `Java packages` Struktur:
  * es gibt *keinen* domain package prefix
  * oberste Packages benennen ein Unter-Thema, das vertikal geschnitten ist und deshalb keine Klassen aus benachbarten Packages nutzen darf

  
