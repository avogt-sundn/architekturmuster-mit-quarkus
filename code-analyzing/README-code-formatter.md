# Code Formatierung

## Formatierung der IDE einrichten

### VS Code

In VS code werden Formatierungseinstellungen in der Datei `settings.json` im Projekt unter [./.vscode/settings.json](./.vscode/settings.json) aktiviert.

  ```json
  # .vscode/settings.json
  {
     "java.format.settings.url": ".vscode/java-formatter.xml",
  }
  ```

## Formatierung im Build Ablauf

Die Einstellungen für die Formatierung sind in der Datei `formatter.xml` [im Projekt unter ./.vscode/java-formatter.xml](../.vscode/java-formatter.xml) definiert.

  Maven kann den Quellcode des Projekts formatieren:

  ```bash
  mvn formatter:format
  ```


Danach kann mit verify geprüft werden, ob die Formatierung korrekt ist:

  ```bash
  mvn verify
  ```

Formatieren abschalten:

  ```bash
  # auch bei jedem anderen maven goal kann mit skip abgeschaltet werden:
  mvn test -Dformat.skip=true
  ```


 - beim Prüfen führen Abweichungen in der Formatierung zum Abbruch:

    ```bash
    [ERROR] Failed to execute goal net.revelc.code.formatter:formatter-maven-plugin:2.23.0:validate (default) on project code-analyzing: File '/workspaces/architekturmuster-mit-quarkus/code-analyzing/src/main/java/quarkitecture/GreetingResource.java' has not been previously formatted. Please format file (for example by invoking `mvn net.revelc.code.formatter:formatter-maven-plugin:2.23.0:format`) and commit before running validation! -> [Help 1]
    ```

## Checkstyle

### VS Code Checkstyle extension

```bash
# .devcontainer/devcontainer.json
"customizations": {
    "vscode": {
        "extensions": [
            "shengchen.vscode-checkstyle",
        ],
```
