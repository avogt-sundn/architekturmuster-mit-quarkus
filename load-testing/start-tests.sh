#!/bin/sh

set -e
# es gibt kein quarkus property quarkus.envotel.metric.export.interval:
export OTEL_METRIC_EXPORT_INTERVAL=5000

docker compose up -d

ping -c 1 influxdb >/dev/null || { echo "influxdb nicht erreichbar"; exit 1; }
curl http://host.docker.internal:8080/q >/dev/null || { echo "quarkus dev nicht erreichbar"; exit 1; }


k6 run  src/scripts/virtual.js --out influxdb=http://influxdb:8086/k6
sleep 5
k6 run  src/scripts/classic.js --out influxdb=http://influxdb:8086/k6