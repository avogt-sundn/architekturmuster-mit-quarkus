# code-analyzing

Wir benutzen maven plugins für die Code-Analyse.

1. formatter
2. checkstyle
3. sonarlint

## Formatierung

Die Einstellungen für die Formatierung sind in der Datei `formatter.xml` [im Projekt unter ../.vscode/java-formatter.xml](../.vscode/java-formatter.xml) definiert.

- In VS code werden Formatierungseinstellungen in der Datei `settings.json` [im Projekt unter ../.vscode/settings.json](../.vscode/settings.json) aktiviert.

  ```json
  # .vscode/settings.json
  {
     "java.format.settings.url": ".vscode/java-formatter.xml",
  }
  ```

Maven kann den Quellcode des Projekts formatieren:

  ```bash
  mvn formatter:format
  ```

- Danach kann mit verify geprüft werden, ob die Formatierung korrekt ist:

  ```bash
  mvn verify
  ```

## Rest API Guide by Zalando

Der Zalando Rest API Guide ist ein Leitfaden für die Erstellung von RESTful APIs. Er enthält Empfehlungen für die Erstellung von APIs, die einfach zu verwenden und zu pflegen sind. Umsetzung und anschließende Prüfung durch das `zally`-Plugin sind im Modul [../json-rest/](../json-rest/README.md) ausgeführt.
