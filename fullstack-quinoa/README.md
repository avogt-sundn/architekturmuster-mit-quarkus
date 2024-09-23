# fullstack-quinoa

Dieses Projekt demonstriert die Quarkus-quinoa Extension:

* ein Angular Project existiert im selben src Baum wie Quarkus, nämlich unter [./src/main/webui]()
* `quarkus dev` baut und startet Backend und Frontend im Live-edit Modus

## Wie man in diesem Projekt arbeitet

Beim Schreiben von Code in diesem Projekt ist es wichtig, vorher den Quarkus Dev Mode zu starten:

```bash
./mvnw quarkus:dev
# oder falls Quarkus CLI installiert ist:
# quarkus dev
```

## Bekannte Fehler

Die Tests laufen auf Fehler, wenn sie innerhalb des Quarkus dev Modus ausgeführt werden.

```bash
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 38.60 s -- in quarkitecture.MyWebUITest
[INFO] Running quarkitecture.AllWebUITest
2024-09-22 09:23:36,370 INFO  [io.quarkus] (main) fullstack-quinoa stopped in 0.012s
2024-09-22 09:23:38,097 INFO  [io.qua.qui.dep.fra.FrameworkType] (build-27) Quinoa detected 'ANGULAR' frameworkType from package.json file.
2024-09-22 09:23:38,121 INFO  [io.qua.qui.dep.pac.PackageManagerRunner] (build-9) Running Quinoa package manager test command: npm run test
2024-09-22 09:23:38,281 INFO  [io.qua.qui.dep.pac.PackageManagerRunner] (build-9)
2024-09-22 09:23:38,281 INFO  [io.qua.qui.dep.pac.PackageManagerRunner] (build-9) > example-ng@0.0.0 test
2024-09-22 09:23:38,281 INFO  [io.qua.qui.dep.pac.PackageManagerRunner] (build-9) > ng test --no-watch --no-progress --browsers=ChromeHeadless
2024-09-22 09:23:38,281 INFO  [io.qua.qui.dep.pac.PackageManagerRunner] (build-9)
2024-09-22 09:23:40,140 INFO  [io.qua.qui.dep.pac.PackageManagerRunner] (build-9) 22 09 2024 09:23:40.139:INFO [karma-server]: Karma v6.4.2 server started at http://localhost:9876/
2024-09-22 09:23:40,140 INFO  [io.qua.qui.dep.pac.PackageManagerRunner] (build-9) 22 09 2024 09:23:40.140:INFO [launcher]: Launching browsers ChromeHeadless with concurrency unlimited
2024-09-22 09:23:40,144 INFO  [io.qua.qui.dep.pac.PackageManagerRunner] (build-9) 22 09 2024 09:23:40.144:INFO [launcher]: Starting browser ChromeHeadless
2024-09-22 09:23:40,355 INFO  [io.qua.qui.dep.pac.PackageManagerRunner] (build-9) 22 09 2024 09:23:40.355:INFO [Chrome Headless 120.0.6099.224 (Linux aarch64)]: Connected on socket v5kp5x_1ldINxmJaAAAB with id 38902576
2024-09-22 09:23:40,380 INFO  [io.qua.qui.dep.pac.PackageManagerRunner] (build-9) Chrome Headless 120.0.6099.224 (Linux aarch64): Executed 0 of 0 SUC
                                                                                  Chrome Headless 120.0.6099.224 (Linux aarch64): Executed 0 of 0 SUCCESS (0 secs / 0 secs)1 INFO  [io.qua.qui.dep.pac.PackageManagerRunner] (build-9)
2024-09-22 09:23:40,381 INFO  [io.qua.qui.dep.pac.PackageManagerRunner] (build-9) TOTAL: 0 SUCCESS
[ERROR] Tests run: 1, Failures: 0, Errors: 1, Skipped: 0, Time elapsed: 4.099 s <<< FAILURE! -- in quarkitecture.AllWebUITest
[ERROR] quarkitecture.AllWebUITest.runTest -- Time elapsed: 0.001 s <<< ERROR!
java.lang.RuntimeException:
java.lang.RuntimeException: io.quarkus.builder.BuildException: Build failure: Build failed due to errors
        [error]: Build step io.quarkiverse.quinoa.deployment.QuinoaProcessor#processBuild threw an exception: java.lang.RuntimeException: Error in Quinoa while running package manager test command: npm run test
        at io.quarkiverse.quinoa.deployment.packagemanager.PackageManagerRunner.test(PackageManagerRunner.java:95)
....
Caused by: java.lang.RuntimeException: io.quarkus.builder.BuildException: Build failure: Build failed due to errors
        [error]: Build step io.quarkiverse.quinoa.deployment.QuinoaProcessor#processBuild threw an exception: java.lang.RuntimeException: Error in Quinoa while running package manager test command: npm run test
```

das test Kommando kann geändert werden in der application.yml Datei, z.B. so (ohne `npm` prefix bitte!):
```bash
quarkus:
  quinoa:
    enable-spa-routing: true
    build-dir: dist/example-ng/browser
    package-manager-command:
      test: run test
```

die tests klappen aber leider nur, wenn man auf der Kommandozeile tests startet:

```bash
cd src/main/webui/
npm run test
# $ npm run test
# > example-ng@0.0.0 test
# > ng test --no-watch --no-progress --browsers=ChromeHeadless
# 01 03 2024 17:01:21.446:INFO [karma-server]: Karma v6.4.2 server started at http://localhost:9876/
# 01 03 2024 17:01:21.447:INFO [launcher]: Launching browsers ChromeHeadless with concurrency unlimited
# 01 03 2024 17:01:21.451:INFO [launcher]: Starting browser ChromeHeadless
# 01 03 2024 17:01:21.743:INFO [Chrome Headless 120.0.6099.224 (Linux aarch64)]: Connected on socket oT_VsXc42yoRbil5AAAB with id 84140249
# Chrome Headless 120.0.6099.224 (Linux aarch64): Executed 0 of 0 SUCCESS (0.001 secs / 0 secs)
# TOTAL: 0 SUCCESS
```

## Wie man ein Quinoa Projekt erzeugt

1. `yq`installieren

    Mit yq werden gleich die Quarkus Konfiguationseinstellungen des Projekts gesetzt, nachdem diese auf yaml Format umgestellt worden sind.

    * MacOS: `brew install yq@3`
    * DevContainer

      ```bash

      sudo apt update
      sudo apt install python3-pip -y
      pip install yq
      ```

2. Erzeuge ein Quarkus Projekt im aktuellen Ordner

    ```bash
    quarkus create app quarkitecture:fullstack-quinoa --java=17 --wrapper
    ```

    * der Projektorder wird unter dem Namen `fullstack-quinoa` angelegt

3. Ergänze quinoa und initialisiere Angular

    ```bash
    cd fullstack-quinoa
    quarkus ext add config-yaml resteasy-reactive-jackson
    quarkus ext add quinoa

    # statt application.properties eine .yaml benutzen:
    mv src/main/resources/application.properties src/main/resources/application.yml

    yq -i '.quarkus.quinoa.enable-spa-routing = true' src/main/resources/application.yml
    yq -i '.quarkus.quinoa.build-dir = "dist/example-ng/browser"' src/main/resources/application.yml
    mkdir src/main/webui

    # Angular cli installieren
    npm install @angular/cli -g
    # Angular Projekt erzeugen (angular)
    ng new --routing --style scss --directory src/main/webui -g --ssr false --standalone false example-ng
    ```

### Quellen

* Quarkus Quinoa Guide im quarkiverse
  * <https://docs.quarkiverse.io/quarkus-quinoa/dev/index.html>

* Erstes Projekt
  * <https://stephennimmo.com/2023/12/01/full-stack-development-quarkus-and-angular-with-quinoa/>
