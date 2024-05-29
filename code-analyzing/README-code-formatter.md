# Code Formatierung

Einheitliche Formatierung des Codes ist wichtig, um die Lesbarkeit zu erhöhen und die Zusammenarbeit zu erleichtern.
Alle, die an einem Projekt arbeiten, sollten sich auf eine einheitliche Formatierung einigen.

Dazu müssen alle beteiligten Werkzeuge dasselbe Format lesen und einheitlich umsetzen, Editoren/IDEs sollten spätestens beim Speichern richtig formatieren. Build tools können eingecheckte Abweichungen korrigieren.

In Java Projekten wird oft der Eclipse Formatter verwendet, da er seit Jahren durch die Eclipse IDE etabliert ist und heute von allen gängigen IDEs unterstützt wird.

- seine Konfigurationsdatei (im XML Format) kann in diese IDEs importiert werden kann:

    * IntelliJ (kann importieren)
    * VS Code
    * und Eclipse (benutzen die XML Datei als Speicherort)

Im Build Ablauf setzen wir ein maven-plugin ein, das wiederum dieselbe Konfigurationsdatei verwendet:

* [Eclipse Code Formatter](https://code.revelc.net/formatter-maven-plugin/examples.html)
* [Spotless](https://github.com/diffplug/spotless/tree/main/plugin-maven)

## Formatierung der IDE einrichten

### VS Code

In VS code werden Formatierungseinstellungen in der Datei `settings.json` im Projekt unter [./.vscode/settings.json](./.vscode/settings.json) aktiviert.

  ```json
  # .vscode/settings.json
  {
     "java.format.settings.url": ".settings/java-formatter.xml",
  }
  ```

Quellen
 - Java Linting
    -  https://code.visualstudio.com/docs/java/java-linting

## Formatierung im Build Ablauf

### Wo ist die Formatierung definiert?
Die Einstellungen für die Formatierung sind in der Datei `java-formatter.xml` im Projekt unter [.settings/java-formatter.xml](../.config/java-formatter.xml) definiert.

### Wie wird die Formatierung ausgelöst?
Maven kann den Quellcode des Projekts formatieren:

  ```bash
  # spotless plugin
  mvn spotless:apply
  ```

Es kann auch nur geprüft werden, ob die Formatierung korrekt ist, ohne eine Korrektur vorzunehmen:

  ```bash
  # spotless plugin
  mvn spotless:check
  # dasselbe, aber über maven lifecycle goal
  mvn validate
  ```

spotless zeigt Abweichungen praktischerweise auch gleich als Diff an:

```bash
[ERROR] Failed to execute goal com.diffplug.spotless:spotless-maven-plugin:2.43.0:check (default-cli) on project code-analyzing: The following files had format violations:
[ERROR]     src/main/java/quarkitecture/GreetingResource.java
[ERROR]         @@ -15,7 +15,7 @@
[ERROR]          public·class·GreetingResource·{
[ERROR]
[ERROR]          ····@GET
[ERROR]         -········@Produces(MediaType.TEXT_PLAIN)
[ERROR]         +····@Produces(MediaType.TEXT_PLAIN)
[ERROR]          ····public·String·getItem()·{
[ERROR]
[ERROR]          ········return·"Hello·from·RESTEasy·Reactive";
[ERROR] Run 'mvn spotless:apply' to fix these violations.
[ERROR] -> [Help 1]
```
 - hier war die Zeile mit der `@Produces` annotation zu weit eingerückt (beginnt mit `-`), die vorgeschlagene Korrektur  beginnt mit `+`


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
```bash
# .vscode/settings.json
{
    "java.checkstyle.configuration": "${workspaceFolder}/.config/checkstyle-config.xml"
}
```


### Tabs oder Leerzeichen)

Gerade bei Tabs ist es sehr wichtig, dass die Einstellungen in der IDE und im Build Ablauf übereinstimmen

 - Leerzeichen:

   ```xml
   <!-- java-formatter.xml -->
   <setting id="org.eclipse.jdt.core.formatter.tabulation.char" value="space" />
   ```

 - Tabs:

   ```xml
   <!-- java-formatter.xml -->
   <setting id="org.eclipse.jdt.core.formatter.tabulation.char" value="tab" />
   ```

### Einrückung

```xml
<!-- java-formatter.xml -->
<setting id="org.eclipse.jdt.core.formatter.tabulation.size" value="4" />
```

### Zeilenumbrüche

Es gibt seit je her Unterschiede zwischen den Betriebssystemen:
- Windows: CRLF
- Mac und Linux: LF

Auch hier gilt: alle einigen sich, damit die Entwickler sich nicht regelmäßig die Formatierung überschreiben.


1. Jeder Entwickler sollte sein lokalen git Einstellung vornehmen:
   ```bash
   # ausführen in der Konsole
   git config --global core.autocrlf input
   ```

1. git im Projekt eingestellt durch `.gitattributes`

    ```bash
    # .gitattributes
    * text=auto
    ```
1. eclipse format settings

    ```xml
    <!-- java-formatter.xml -->
    <setting id="org.eclipse.jdt.core.formatter.linefeed" value="LF" />
    ```

1. VS Code settings

    ```json
    # .vscode/settings.json
    {
        "files.eol": "\n"
    }
    ```


### Zeilenumbruch am Dateiende

Es gibt unterschiedliche Meinungen, ob eine Datei mit einem Zeilenumbruch enden sollte oder nicht. Einige Editoren fügen automatisch einen Zeilenumbruch am Dateiende ein, andere nicht. Hier stellen wir checkstyle und den Eclipse Code Formatter so ein, dass ein Zeilenumbruch am Dateiende eingefügt wird.

1. java-formatter.xml

```xml
<!-- java-formatter.xml -->
<setting id="org.eclipse.jdt.core.formatter.insert_new_line_at_end_of_file_if_missing" value="insert" />
```

1. Checkstyle

```xml
<module name="NewlineAtEndOfFile" />
```


### Zeilenlänge

```xml
<!-- java-formatter.xml -->
<setting id="org.eclipse.jdt.core.formatter.lineSplit" value="140" />
```



## Import statments Sortierung

### Impsort Maven Plugin

Das Impsort Maven Plugin sortiert die Imports in den Java Dateien.

```bash
mvn impsort:sort
```

```xml
<plugin>
    <groupId>net.revelc.code</groupId>
    <artifactId>impsort-maven-plugin</artifactId>
    <version>1.10.0</version>
    <configuration>
        <!-- store outside of target to speed up formatting when mvn clean is used -->
        <cachedir>.cache/impsort-maven-plugin-${impsort-maven-plugin.version}</cachedir>
        <groups>java.,javax.,jakarta.,org.,com.</groups>
        <staticGroups>*</staticGroups>
        <skip>${format.skip}</skip>
        <removeUnused>true</removeUnused>
    </configuration>
</plugin>
```

Leider kann impsort nicht zwischen static und non-static imports bei der wildcard Nutzung differenzieren.

Empfehlung: checkstyle und IDE sollten so eingerichtet werden, dass bei static imports wildcards zugelassen sind:

- Beispiel:

    ```java
    # erlaubte wildcars nur bei static
    import static io.restassured.RestAssured.*;
    import static org.hamcrest.CoreMatchers.*;
    import static org.hamcrest.Matchers.*;

    # ausgeschriebene imports
    import org.apache.http.HttpStatus;
    ```

### Checkstyle Regeln für Import Sortierung

Der Import mittels `*` ist in der Regel nicht erlaubt, da er die Lesbarkeit des Codes erschwert. Es ist besser, die Klassen einzeln zu importieren.

- statische Imports sollten aber `*`erlauben, da mit ihnen fluent APIs importiert und im content assist der IDE angezeigt werden.

    ```xml
    <module name="AvoidStarImport" >
        <property name="allowStaticMemberImports" value="true" />
    </module>
    ````

---

## Known issues

### die Datei src/main/resources/application.properties wird nicht formatiert

- den gesamten src folder anzugeben reicht nicht aus, um die Datei zu formatieren

```xml
<directories>
    <directory>${project.build.sourceDirectory}</directory>
</directories>
```

# Quellen

- Formatter Maven Plugin
  - Projekt: https://code.revelc.net/formatter-maven-plugin/examples.html
  - Einstellungen:  https://code.revelc.net/formatter-maven-plugin/format-mojo.html

- Impsort Maven Plugin
  - Projekt https://code.revelc.net/impsort-maven-plugin/
  - Einstellungen: https://code.revelc.net/impsort-maven-plugin/sort-mojo.html