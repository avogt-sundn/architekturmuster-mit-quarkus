# Code Formatierung

Einheitliche Formatierung des Codes ist wichtig, um die Lesbarkeit zu erhöhen und die Zusammenarbeit zu erleichtern.
Alle, die an einem Projekt arbeiten, sollten sich auf eine einheitliche Formatierung einigen.

Dazu müssen auch alle Editoren ihre Formatierungseinstellungen entsprechend anpassen.

Dazu müssen alle beteiligten Werkzeuge dasselbe Format lesen und einheitlich umsetzen. In Java Projekten wird oft der **Eclipse Code Formatter** verwendet, da er seit Jahren in der Java Community etabliert ist und somit von allen gängigen IDEs unterstützt wird:

* IntelliJ
* VS Code
* Eclipse

Im Build Ablauf setzen wir das `Formatter` maven-plugin ein, um die Formatierung zu prüfen und ggf. zu korrigieren.

## Formatierung der IDE einrichten

### VS Code

In VS code werden Formatierungseinstellungen in der Datei `settings.json` im Projekt unter [./.vscode/settings.json](./.vscode/settings.json) aktiviert.

  ```json
  # .vscode/settings.json
  {
     "java.format.settings.url": ".vscode/java-formatter.xml",
  }
  ```

Quellen
 - Java Linting
    -  https://code.visualstudio.com/docs/java/java-linting

## Formatierung im Build Ablauf

Die Einstellungen für die Formatierung sind in der Datei `formatter.xml` [im Projekt unter ./.vscode/java-formatter.xml](../.vscode/java-formatter.xml) definiert.

Maven kann den Quellcode des Projekts formatieren:

  ```bash
  mvn formatter:format
  ```

   * sdsd

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

## Einstellungen des Formats in der XML Datei

### Tabs oder Leerzeichen?

Gerade bei Tabs ist es sehr wichtig, dass die Einstellungen in der IDE und im Build Ablauf übereinstimmen

 - Leerzeichen:

   ```xml
   <setting id="org.eclipse.jdt.core.formatter.tabulation.char" value="space" />
   ```

 - Tabs:

   ```xml
   <setting id="org.eclipse.jdt.core.formatter.tabulation.char" value="tab" />
   ```
