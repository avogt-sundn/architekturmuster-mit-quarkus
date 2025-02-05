#!/bin/bash

set -e

docker compose up -d

SERVICE=server;

if ping -c 1 $SERVICE >/dev/null 2>&1 ; then
 echo "ping auf $SERVICE erfolgreich.";
else
    echo "ping auf $SERVICE fehlgeschlagen. versuche localhost.";
    SERVICE=localhost;
    if curl http://$SERVICE:8080 >/dev/null 2>&1; then
       echo "curl auf $SERVICE erfolgreich.";
    else
       echo "curl auf $SERVICE fehlgeschlagen. beende.";
       exit 1;
    fi
fi

sleep 1
export SERVICE_URL=http://$SERVICE:8080

INFLUX_HOST=influxdb
if ping -c 1 $INFLUX_HOST >/dev/null 2>&1 ; then
    echo "$INFLUX_HOST erreichbar";
else
    echo "influxdb nicht erreichbar. versuche localhost:8086";
    INFLUX_HOST=localhost;
    if curl http://$INFLUX_HOST:8086 >/dev/null 2>&1; then
       echo "curl auf $INFLUX_HOST:8086 erfolgreich.";
    else
       echo "curl auf $INFLUX_HOST:8086 fehlgeschlagen. beende.";
       exit 1;
    fi
fi

k6 run  src/scripts/run.js --out influxdb=http://$INFLUX_HOST:8086/k6
