# Code Formatierung

Einheitliche Formatierung des Codes ist wichtig, um die Lesbarkeit zu erhöhen und die Zusammenarbeit zu erleichtern.
Alle, die an einem Projekt arbeiten, sollten sich auf eine einheitliche Formatierung einigen.

Dazu müssen auch alle Editoren ihre Formatierungseinstellungen entsprechend anpassen.

Dazu müssen alle beteiligten Werkzeuge dasselbe Format lesen und einheitlich umsetzen. In Java Projekten wird oft der [Eclipse Code Formatter](https://code.revelc.net/formatter-maven-plugin/examples.html) verwendet, da er seit Jahren in der Java Community etabliert ist und somit von allen gängigen IDEs unterstützt wird:

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

### Welche Fehler gibt es dabei?

Beim Prüfen führen Abweichungen in der Formatierung zum Abbruch:

```bash
[ERROR] Failed to execute goal net.revelc.code.formatter:formatter-maven-plugin:2.23.0:validate (default) on project code-analyzing: File '/workspaces/architekturmuster-mit-quarkus/code-analyzing/src/main/java/quarkitecture/GreetingResource.java' has not been previously formatted. Please format file (for example by invoking `mvn net.revelc.code.formatter:formatter-maven-plugin:2.23.0:format`) and commit before running validation! -> [Help 1]
```

Hilfe dazu gibt es unter:

```bash
mvn formatter:help -Ddetail=true -Dgoal=format
```

### Maven Formatter Plugin in der pom.xml


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

Unterhalb von
```xml
    <build>
        <plugins>
```
ist dieser Block einzufügen:

```xml
<plugin>
    <groupId>net.revelc.code.formatter</groupId>
    <artifactId>formatter-maven-plugin</artifactId>
    <version>2.23.0</version>
    <executions>
        <execution>
            <goals>
                <goal>validate</goal>
            </goals>
        </execution>
    </executions>
    <dependencies>
        <!-- Quarkus eigene formatter Definition,
        wird von unserer überlagert.
        -->
        <!-- https://repo.maven.apache.org/maven2/io/quarkus/quarkus-ide-config/2.5.1.Final/ -->
        <dependency>
            <artifactId>quarkus-ide-config</artifactId>
            <groupId>io.quarkus</groupId>
            <version>${quarkus.platform.version}</version>
        </dependency>
    </dependencies>
    <configuration>
        <configFile>${project.basedir}/.config/java-formatter.xml</configFile>
        <lineEnding>LF</lineEnding>
        <encoding>UTF-8</encoding>
        <skip>${format.skip}</skip>
        <compilerCompliance>${maven.compiler.source}</compilerCompliance>
        <compilerCompliance>${maven.compiler.source}</compilerCompliance>
    </configuration>
</plugin>
<plugin>
    <groupId>net.revelc.code</groupId>
    <artifactId>impsort-maven-plugin</artifactId>
    <version>1.9.0</version>
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

    1. maven formatter plugin:

    ```xml
        <plugin>
    <groupId>net.revelc.code.formatter</groupId>
    <artifactId>formatter-maven-plugin</artifactId>
    <version>2.23.0</version>
    <configuration>
        <lineEnding>LF</lineEnding>
        <encoding>UTF-8</encoding>
    </configuration>
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

## Import Sortierung
### Impsort Maven Plugin

Das Impsort Maven Plugin sortiert die Imports in den Java Dateien.

```bash
mvn impsort:sort
```

```xml
<plugin></plugin>
    <groupId>net.revelc.code</groupId>
    <artifactId>impsort-maven-plugin</artifactId>
    <version>1.9.0</version>
    <configuration>
        <groups>java.,javax.,jakarta.,org.,com.</groups>
        <staticGroups>*</staticGroups>
        <removeUnused>true</removeUnused>
    </configuration>

```

### Checkstyle Regeln für Import Sortierung

Der Import mittels `*` ist in der Regel nicht erlaubt, da er die Lesbarkeit des Codes erschwert. Es ist besser, die Klassen einzeln zu importieren.

- statische Imports sollten aber `*`erlauben, da mit ihnen fluent APIs importiert und im content assist der IDE angezeigt werden.

    ```xml
    <module name="AvoidStarImport" >
        <property name="allowStaticMemberImports" value="true" />
    </module>
    ````


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