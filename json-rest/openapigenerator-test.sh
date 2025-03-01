#!/bin/bash

mvn -Popenapi-generator  clean compile
cp pom.xml target/generated-sources/openapi/pom.xml

cd target/generated-sources/openapi/ || exit
[ -d '../../../partial' ] || { echo "Wo ist denn der Ordner mit den partiellen sourcen?"; exit 1; }
[ -f ../../../src/main/resources/openapi/openapi.yaml ] || { echo "Wo ist denn die openapi.yaml?"; exit 1;}
#export QUARKUS_SMALLRYE_OPENAPI_ADDITIONAL_DOCS_DIRECTORY=../../../src/main/resources/openapi/
mkdir -p src/main/resources/META-INF
cp -i ../../../src/main/resources/openapi/openapi.yaml src/main/resources/META-INF/openapi.yaml | echo "openapi.yaml konnte nicht kopiert werden"

printf "\n\nProjekt bauen und testen\n\n"
mvn clean test -Prun-generated
printf "\n\nDev Mode wird gestartet\n\n"
mvn quarkus:dev -Prun-generated
exit 0;