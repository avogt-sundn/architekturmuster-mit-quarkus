# openfga

## Test-Modus

`mvn test` nutzt entweder

1. devcontainers um einen openfga container zu starten.
2. oder docker compose.yaml

Nach dem Test kann openfga betrachtet werden unter
- http://localhost:3000/playground


Passend zum gewählten Modus muss die applicatiom.properties richtig konfiguriert werden

- für devcontainer

````properties
quarkus.devservices.enabled=true
#quarkus.openfga.url=http://localhost:8080
````

- für docker compose

````properties
quarkus.devservices.enabled=false
quarkus.openfga.url=http://localhost:8080
````

- starten mit `docker compose up -d`
-

Vollständige und korrekte Ausgabe von mvn test sieht so aus:

````shell
❯ mvn test
[INFO] Scanning for projects...
[INFO]
[INFO] -----------------------< quarkitecture:openfga >------------------------
[INFO] Building openfga 1.0.0-SNAPSHOT
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- resources:3.3.1:resources (default-resources) @ openfga ---
[INFO] Copying 4 resources from src/main/resources to target/classes
[INFO]
[INFO] --- quarkus:3.7.4:generate-code (default) @ openfga ---
[INFO]
[INFO] --- compiler:3.12.1:compile (default-compile) @ openfga ---
[INFO] Nothing to compile - all classes are up to date.
[INFO]
[INFO] --- quarkus:3.7.4:generate-code-tests (default) @ openfga ---
[WARNING] [io.quarkus.bootstrap.resolver.maven.workspace.WorkspaceLoader] Module(s) under /Users/avogt/projects/architekturmuster-mit-quarkus/getting-started will be handled as thirdparty dependencies because /Users/avogt/projects/architekturmuster-mit-quarkus/getting-started/pom.xml does not exist
[INFO]
[INFO] --- resources:3.3.1:testResources (default-testResources) @ openfga ---
[INFO] Copying 2 resources from src/test/resources to target/test-classes
[INFO]
[INFO] --- compiler:3.12.1:testCompile (default-testCompile) @ openfga ---
[INFO] Nothing to compile - all classes are up to date.
[INFO]
[INFO] --- surefire:3.2.5:test (default-test) @ openfga ---
[INFO] Using auto detected provider org.apache.maven.surefire.junitplatform.JUnitPlatformProvider
[INFO]
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running quarkitecture.DevservicesTest
[WARNING] Tests run: 2, Failures: 0, Errors: 0, Skipped: 2, Time elapsed: 0.003 s -- in quarkitecture.DevservicesTest
[INFO] Running quarkitecture.OpenFGATest
10:58:51 WARN  [io.qu.bo.re.ma.wo.WorkspaceLoader] (main) {} Module(s) under /Users/avogt/projects/architekturmuster-mit-quarkus/getting-started will be handled as thirdparty dependencies because /Users/avogt/projects/architekturmuster-mit-quarkus/getting-started/pom.xml does not exist
10:58:52 INFO  [or.te.do.DockerClientProviderStrategy] (build-23) {} Loaded org.testcontainers.dockerclient.UnixSocketClientProviderStrategy from ~/.testcontainers.properties, will try it first
10:58:53 INFO  [or.te.do.DockerClientProviderStrategy] (build-23) {} Found Docker environment with local Unix socket (unix:///var/run/docker.sock)
10:58:53 INFO  [or.te.DockerClientFactory] (build-23) {} Docker host IP address is localhost
10:58:53 INFO  [or.te.DockerClientFactory] (build-23) {} Connected to docker:
  Server Version: 26.1.4
  API Version: 1.45
  Operating System: Docker Desktop
  Total Memory: 8934 MB
10:58:53 INFO  [or.te.im.PullPolicy] (build-23) {} Image pull policy will be performed by: DefaultPullPolicy()
10:58:53 INFO  [or.te.ut.ImageNameSubstitutor] (build-23) {} Image name substitution will be performed by: DefaultImageNameSubstitutor (composite of 'ConfigurationFileImageNameSubstitutor' and 'PrefixingImageNameSubstitutor')
10:58:53 INFO  [or.te.DockerClientFactory] (build-23) {} Checking the system...
10:58:53 INFO  [or.te.DockerClientFactory] (build-23) {} ✔︎ Docker server version should be at least 1.6.0
10:58:53 INFO  [io.qu.op.de.DevServicesOpenFGAProcessor] (build-23) {} Starting OpenFGA...
10:58:53 INFO  [tc.openfga/openfga:latest] (build-23) {} Creating container for image: openfga/openfga:latest
10:58:53 INFO  [or.te.ut.RegistryAuthLocator] (build-23) {} Credential helper/store (docker-credential-desktop) does not have credentials for https://index.docker.io/v1/
10:58:53 INFO  [tc.te.6.0] (build-23) {} Creating container for image: testcontainers/ryuk:0.6.0
10:58:53 INFO  [tc.te.6.0] (build-23) {} Container testcontainers/ryuk:0.6.0 is starting: 031750eda26fcb7cf6cde8675ed3251a34d649a1179a6ab5305180304e715973
10:58:53 INFO  [tc.te.6.0] (build-23) {} Container testcontainers/ryuk:0.6.0 started in PT0.282294S
10:58:53 INFO  [tc.openfga/openfga:latest] (build-23) {} Container openfga/openfga:latest is starting: 6ab17fbe6eab2686b96e0cb611e8031ac36423768b3348fdb7b9198bdd537174
10:58:53 INFO  [or.te.co.wa.st.HttpWaitStrategy] (build-23) {} /nostalgic_hamilton: Waiting for 60 seconds for URL: http://localhost:58636/healthz (where port 58636 maps to container port 8080)
10:58:53 INFO  [tc.openfga/openfga:latest] (build-23) {} Container openfga/openfga:latest started in PT0.632426S
10:58:53 INFO  [io.qu.op.de.DevServicesOpenFGAProcessor] (build-23) {} Initializing authorization store...
10:58:54 INFO  [io.qu.op.de.DevServicesOpenFGAProcessor] (build-23) {} Dev Services for OpenFGA started.
10:58:54 INFO  [io.qu.op.de.DevServicesOpenFGAProcessor] (build-23) {} Other Quarkus applications in dev mode will find the instance automatically. For Quarkus applications in production mode, you can connect to this by starting your application with -Dquarkus.openfga.url=http://localhost:58636
10:58:54 INFO  [io.quarkus] (main) {} openfga 1.0.0-SNAPSHOT on JVM (powered by Quarkus 3.7.4) started in 2.433s. Listening on: http://localhost:8081
10:58:54 INFO  [io.quarkus] (main) {} Profile test activated.
10:58:54 INFO  [io.quarkus] (main) {} Installed features: [cdi, openfga-client, resteasy, resteasy-jackson, smallrye-context-propagation, vertx]
10:58:54 INFO  [qu.OpenFGATest] (main) {} json:{
  "schema_version" : "1.1",
  "type_definitions" : [ {
    "type" : "user",
    "relations" : { }
  }, {
    "type" : "document",
    "relations" : {
      "reader" : {
        "this" : {
          "a" : 1
        }
      },
      "writer" : {
        "this" : {
          "b" : 2
        }
      }
    },
    "metadata" : {
      "relations" : {
        "reader" : {
          "directly_related_user_types" : [ {
            "type" : "user"
          } ]
        },
        "writer" : {
          "directly_related_user_types" : [ {
            "type" : "user"
          } ]
        }
      }
    }
  } ]
}
10:58:55 INFO  [qu.OpenFGATest] (main) {} randomAlphabetic:NCOVVD
10:58:55 INFO  [qu.OpenFGATest] (main) {} listAllStores:[Store[id=01J5D6JH9HJRED4Q31R2E0K7Z8, name=dev, createdAt=2024-08-16T08:58:54.129679595Z, updatedAt=2024-08-16T08:58:54.129679595Z, deletedAt=null], Store[id=01J5D6JJ54YDBAPNW8YMB3AZZ9, name=NCOVVD, createdAt=2024-08-16T08:58:55.012910095Z, updatedAt=2024-08-16T08:58:55.012910095Z, deletedAt=null]]
[WARNING] Tests run: 4, Failures: 0, Errors: 0, Skipped: 1, Time elapsed: 3.371 s -- in quarkitecture.OpenFGATest
[INFO] Running quarkitecture.finegrainedauth.BenutzerUndTeamTest
[WARNING] Tests run: 2, Failures: 0, Errors: 0, Skipped: 2, Time elapsed: 0 s -- in quarkitecture.finegrainedauth.BenutzerUndTeamTest
10:58:55 INFO  [io.quarkus] (main) {} openfga stopped in 0.012s
[INFO]
[INFO] Results:
[INFO]
[WARNING] Tests run: 8, Failures: 0, Errors: 0, Skipped: 5
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  6.147 s
[INFO] Finished at: 2024-08-16T10:58:55+02:00
[INFO] ------------------------------------------------------------------------
````

# Quellen

- Docker Beschreibung
  - <https://openfga.dev/docs/getting-started/setup-openfga/docker>
