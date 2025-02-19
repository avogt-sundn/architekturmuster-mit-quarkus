# Load testing mit K6

k6 ist ein Lastgenerator und ein Projekt von Grafana.

- https://grafana.com/docs/k6/latest/

## Starten der Umgebung mit docker compose

```bash
docker compose up -d
```

## Grafana Open Telemetry Docker image

```bash
docker compose up -d

docker compose logs -f
# [Die  Log-Ausgabe endet mit den Zeilen:]
#
# otel-1  | The OpenTelemetry collector and the Grafana LGTM stack are up and running. (created /tmp/ready)
# otel-1  | Open ports:
# otel-1  |  - 4317: OpenTelemetry GRPC endpoint
# otel-1  |  - 4318: OpenTelemetry HTTP endpoint
# otel-1  |  - 3000: Grafana. User: admin, password: admin
```

- https://grafana.com/docs/k6/latest/results-output/real-time/opentelemetry/
-
## Wie kann ich k6 aufrufen?

k6 ist
- ein Kommandozeilen-Tool
- ein server, der den Lasttest ausführt


 1. Der k6 Server wird in diesem Projekt als Container  aus dem [docker-compose.yml](docker-compose.yml) gestartet.

1. Das k6 Kommando ist im VS Code Devcontainer bereits installiert. UNter WSL2 oder MacOS muss es manuell installiert werden mittels

   - WSL2: `sudo apt install k6`
   - MacOS: `brew install k6`
   - Windows ohne WSL2: Hier kann das k6 Kommando innerhalb des k6 Containers interaktiv benutzt werden, in dem dazu eine shell dort geöffnet wird:

    ```bash
    docker compose exec k6 /bin/ash
    # /home/k6 #
    # jetzt kann k6 ausgeführt werden
    ```

## k6 Kommando startet die Testläufe

`k6`  benötigt zwei Parameter:

1. das Test-Skript
1. die URL der influxdb-Zeitreihendatenbank

   - das Ausgabeziel ist als Umgebungsvariable K6_OUT hinterlegt:
        ```bash
        export INFLUXDB=host.docker.internal
        export K6_OUT=influxdb=http://$INFLUXDB:8086/k6
        k6 run /scripts/script.js
        ```

   - oder mit Kommandozeilenparameter `--out`:

        ```bash
        export INFLUXDB=host.docker.internal
        k6 run /scripts/script.js --out influxdb=http://$INFLUXDB:8086/k6
        ```
   > der hostname `influxdb` funktioniert nur, wenn k6 im Docker-container ausgeführt wird. Das ist der Fall, wenn VS Code im Devcontainer gestartet wird. Wenn k6 auf dem host installiert ist, muss hier die url auf http://localhost:8086/k6 geändert werden, alternativ funktioniert in beiden Fällen auch http://host.docker.internal:8086/k6

### Starten im VS Code - Devcontainer

Das Projekt sollte im Dev Container ("reopen in devcontainer") gestartet werden, damit alle tools verfügbar sind.

Vor dem Testlauf ist das quarkus Projekt mit `quarkus dev` zu starten, so dass es auf der URL, so wie im script angegeben, erreichbar ist:

- die script.js Datei wird im k6 Container gemountet und enthält die zu testende URL:

    ```bash
    export default function () {
    const response = http.get("http://host.docker.internal:8080/organizations?_pagesize=100&beschreibung=GetPaginated", {headers: {Accepts: "application/json"}});
    check(response, { "status is 200": (r) => r.status === 200 });
    sleep(.300);
    };
    ```

   - `host.docker.interal` ist der hostname, so wie das Gastsystem aus einem Devcontainer aus erreicht werden kann


### Starten der Applikation

Für Lasttests sollte die Quarkus Applikation wie für Produktion gebaut werden. Der Dev Modus beeinflusst die Leistung und das Fehler-handling.

````bash
# erzeugt ein runner.jar
mvn package -DskipTests
java -jar target/load-testing-1.0.0-SNAPSHOT-runner.jar
````

- in der [application.properties](src/main/*resources**/application.properties) wurde dazu das packaging Modell auf `uber-jar` eingestellt:
    - `quarkus.package.jar.type=uber-jar`
    -
## Starten des k6 Tests

Mit der Ausführung erscheint diese Meldung und der Test startet.

Am Ende wird eine Statistik ausgegeben:

```bash

# /home/k6 # k6 run /scripts/script.js
#
#           /\      |‾‾| /‾‾/   /‾‾/
#      /\  /  \     |  |/  /   /  /
#     /  \/    \    |     (   /   ‾‾\
#    /          \   |  |\  \ |  (‾)  |
#   / __________ \  |__| \__\ \_____/ .io
#
#   execution: local
#      script: /scripts/script.js
#      output: InfluxDBv1 (http://influxdb:8086)
#
#   scenarios: (100.00%) 1 scenario, 500 max VUs, 1m50s max duration (incl. graceful stop):
#            * default: Up to 500 looping VUs for 1m20s over 5 stages (gracefulRampDown: 30s, gracefulStop: 30s)
#
#      ✓ status is 200
#
#      checks.........................: 100.00% ✓ 9472      ✗ 0
#      data_received..................: 816 kB  4.2 kB/s
#      data_sent......................: 1.7 MB  8.8 kB/s
#      http_req_blocked...............: avg=317.4µs  min=625ns    med=3µs      max=245.03ms p(90)=8.12µs  p(95)=18.75µs
#      http_req_connecting............: avg=229.64µs min=0s       med=0s       max=244.96ms p(90)=0s      p(95)=0s
#      http_req_duration..............: avg=601.73ms min=1.86ms   med=9.68ms   max=2m10s    p(90)=1.23s   p(95)=3.68s
#        { expected_response:true }...: avg=588.05ms min=1.86ms   med=9.68ms   max=46.26s   p(90)=1.23s   p(95)=3.68s
#      http_req_failed................: 0.01%   ✓ 1         ✗ 9479
#      http_req_receiving.............: avg=5.76ms   min=0s       med=44.72µs  max=9.11s    p(90)=161.8µs p(95)=492.29µs
#      http_req_sending...............: avg=642.2µs  min=2.7µs    med=13.2µs   max=602.33ms p(90)=48.42µs p(95)=101.04µs
#      http_req_tls_handshaking.......: avg=0s       min=0s       med=0s       max=0s       p(90)=0s      p(95)=0s
#      http_req_waiting...............: avg=595.33ms min=1.83ms   med=9.59ms   max=2m10s    p(90)=1.22s   p(95)=3.68s
#      http_reqs......................: 9480    49.02627/s
#      iteration_duration.............: avg=872.63ms min=302.21ms med=310.52ms max=28.09s   p(90)=1.55s   p(95)=3.9s
#      iterations.....................: 9472    48.984898/s
#      vus............................: 0       min=0       max=500
#      vus_max........................: 500     min=500     max=500
#
#
# running (3m13.4s), 000/500 VUs, 9472 complete and 476 interrupted iterations
# default ✓ [======================================] 476/500 VUs  1m20s
# /home/k6 #

```

## Statistiken im Grafana Dashboard

Unter der URL [http://localhost:3000/d/k6/k6-load-testing-results?orgId=1&refresh=5s&from=now-15m&to=now]() erscheint das Dashboard mit Daten, nachdem der Test gelaufen ist.

![Alt text](grafana/screenshot1.png)

### Welche Dashboards gibt es

https://grafana.com/grafana/dashboards/4701-jvm-micrometer/

- k6 tool

https://grafana.com/grafana/dashboards/2587-k6-load-testing-results/


## Schreiben der Test-Skripte

1. IDE Extension

   kann installiert werden für vs code und intellij.

2. Typescript Types

    ````bash
    npm install --save-dev @types/k6
    ````

- https://grafana.com/docs/k6/latest/set-up/configure-your-code-editor/


## troubleshooting

#### Welche metriken werden wirklich gesendet?

Dazu gibt es auf der Quarkus app immer diesen endpunkt:

- http://localhost:8080/q/metrics

#### Zeitverzögerung in grafana

Verkürze die Meldezeiten von alle 60 Sekunden auf jede Sekunde:

````bash
export OTEL_METRIC_EXPORT_INTERVAL=1000
quarkus dev
# -- es erscheint diese Zeile im Laufe der Ausgabe
# 13:29:39 INFO  traceId=, parentId=, spanId=, sampled= [io.mi.co.in.pu.PushMeterRegistry] (main) Publishing metrics for OtlpMeterRegistry every 1s to http://grafana-otel-lgtm:4318/v1/metrics with resource attributes {}
````

- https://grafana.com/blog/2024/03/13/an-opentelemetry-backend-in-a-docker-image-introducing-grafana/otel-lgtm/

#### Caused by: jakarta.enterprise.inject.UnsatisfiedResolutionException: Unsatisfied dependency for type io.opentelemetry.api.metrics.Meter and qualifiers [@Default]
  - Ursache: in der application.properties fehlt `quarkus.otel.metrics.enabled=true`

#### 11:59:04 WARNING traceId=, parentId=, spanId=, sampled= [io.op.ex.in.gr.GrpcExporter] (vert.x-eventloop-thread-2) Failed to export metrics. Server responded with gRPC status code 2. Error message: Failed to export MetricsRequestMarshalers. The request could not be executed. Full error message: Connection refused: localhost/127.0.0.1:4317

#### 12:04:16 WARN  traceId=, parentId=, spanId=, sampled= [io.mi.re.ot.OtlpMeterRegistry] (otlp-metrics-publisher-5) Failed to publish metrics to OTLP receiver (context: url=http://localhost:4318/v1/metrics, resource-attributes={}): java.net.ConnectException: Connection refused
-  Ursache: in der application.properties fehlt `quarkus.micrometer.export.otlp.url=http://otel:4318`

### Aufruf K6 mit otlp
export K6_OTEL_EXPORTER_OTLP_PROTOCOL=grpc K6_OTEL_GRPC_EXPORTER_INSECURE=true K6_OTEL_METRIC_PREFIX=k6_ K6_OTEL_GRPC_EXPORTER_ENDPOINT=localhost:4317;  k6 run  src/scripts/classic.js  --out experimental-opentelemetry