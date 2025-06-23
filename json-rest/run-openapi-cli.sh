#!/bin/bash

[ -f /tmp/openapi-generator-cli-7.11.0.jar ] || mvn dependency:copy -Dopenapi-generator  -Dartifact=org.openapitools:openapi-generator-cli:7.11.0 -DoutputDirectory=/tmp
java -jar /tmp/openapi-generator-cli-7.11.0.jar "$@"
