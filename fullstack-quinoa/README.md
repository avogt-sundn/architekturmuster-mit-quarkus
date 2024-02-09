# fullstack-quinoa

This project uses Quarkus with the quinoa extension:

* an Angular project lives in the src tree with the java code, specifically at [./src/main/webui]()



## Wie man ein Quinoa Projekt erzeugt

1. `yq`installieren

    Mit yq werden gleich die Quarkus Konfiguationseinstellungen des Projekts gesetzt, nachdem diese auf yaml Format umgestellt worden sind.

    - MacOS: `brew install yq@3`
    - DevContainer
      ```bash

      sudo apt update
      sudo apt install python3-pip -y
      pip install yq
      ```


2. Erzeuge ein Quarkus Projekt im aktuellen Ordner

    ```bash
    quarkus create app quarkitecture:fullstack-quinoa --java=17 --wrapper
    ```

    - der Projektorder wird unter dem Namen `fullstack-quinoa` angelegt

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


### Quarkus Guides

[Related guide section...](https://docs.quarkiverse.io/quarkus-quinoa/dev/index.html)

- https://stephennimmo.com/2023/12/01/full-stack-development-quarkus-and-angular-with-quinoa/