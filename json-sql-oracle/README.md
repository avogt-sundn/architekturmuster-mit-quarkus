# Json speichern in Oracle SQL Datenbank

## Arm64

Oracle images für Arm64 (für Apple Silicon) stehen bisher nicht zur Verfügung.

Umgehung:
- colima docker daemon mit eingebautem QEMU Emulator

      brew install colima

      colima start --arch x86_64 --memory 4

- dann erst mit docker compose oracle starten:

    ```bash
    docker compose up -d
    # [+] Running 1/1
    # ✔ Container json-sql-oracle-oracle-1  Started                                                                                                                                      1.9s
    docker compose logs -f
    # oracle-1  | CONTAINER: starting up...
    # oracle-1  | CONTAINER: first database startup, initializing...
    # oracle-1  | CONTAINER: starting up Oracle Database...
    # oracle-1  |
    # oracle-1  | LSNRCTL for Linux: Version 23.0.0.0.0 - Production on 31-JAN-2024 15:59:38
    # oracle-1  |
    # oracle-1  | Copyright (c) 1991, 2023, Oracle.  All rights reserved.
    # oracle-1  |
    # oracle-1  | Starting /opt/oracle/product/23c/dbhomeFree/bin/tnslsnr: please wait...
    # oracle-1  |
    # oracle-1  | TNSLSNR for Linux: Version 23.0.0.0.0 - Production
    # ```

## Parameter für Oracle

```properties
# application.properties
quarkus.datasource.password=password
quarkus.datasource.username=system
```
 - oracle user ist `system`
-
```dockerfile
image: docker.io/gvenzl/oracle-free:23-slim-faststart
#    image: container-registry.oracle.com/database/express:latest
environment:
    ORACLE_PASSWORD: password
```
- hier dasselbe Passwort wie in `application.properties` verwenden