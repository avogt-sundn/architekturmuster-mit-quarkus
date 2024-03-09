# Redhat Quarkus Build - die Enterprise Edition

## Einleitung

Red Hat Quarkus ist ein Kubernetes-native Java-Stack, der für die OpenShift-Plattform entwickelt wurde. Es ist eine vollständig unterstützte Version von Quarkus, die für Unternehmen entwickelt wurde. Es bietet zusätzliche Funktionen, die für die Entwicklung, Bereitstellung und Verwaltung von Anwendungen in Produktionsumgebungen erforderlich sind.

Der Enterprise Build von Quarkus bietet:

- **Langzeitunterstützung (LTS)**: Red Hat Quarkus bietet eine langfristige Unterstützung, die für Unternehmen erforderlich ist, um Anwendungen in Produktionsumgebungen zu betreiben.
- **Zertifizierte Container Images**: Red Hat Quarkus bietet zertifizierte Container-Images, die auf Red Hat Enterprise Linux basieren und in OpenShift bereitgestellt werden können.

## Umgebung einrichten, damit die redhat-dependencies verwendet werden können

1. Erstellen Sie die settings.xml-Datei im Verzeichnis `~/.m2/` (oder aktualisieren Sie die Datei, wenn sie bereits vorhanden ist) und fügen Sie den folgenden Inhalt ein:

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd">

    <activeProfiles>
        <activeProfile>red-hat-enterprise-maven-repository</activeProfile>
    </activeProfiles>

    <profiles>
        <!-- Configure the Red Hat build of Quarkus Maven repository -->
        <profile>
            <id>red-hat-enterprise-maven-repository</id>
            <repositories>
                <repository>
                    <id>red-hat-enterprise-maven-repository</id>
                    <url>https://maven.repository.redhat.com/ga/</url>
                    <releases>
                        <enabled>true</enabled>
                    </releases>
                    <snapshots>
                        <enabled>false</enabled>
                    </snapshots>
                </repository>
            </repositories>
            <pluginRepositories>
                <pluginRepository>
                    <id>red-hat-enterprise-maven-repository</id>
                    <url>https://maven.repository.redhat.com/ga/</url>
                    <releases>
                        <enabled>true</enabled>
                    </releases>
                    <snapshots>
                        <enabled>false</enabled>
                    </snapshots>
                </pluginRepository>
            </pluginRepositories>
        </profile>
    </profiles>
</settings>
```

2. im Ordner `~/.quarkus` die `conf.yaml`-Datei erstellen und die folgenden Zeilen hinzufügen:

```yaml
# Datei ~/.quarkus/conf.yaml
registries:
  - registry.quarkus.redhat.com
  - registry.quarkus.io
```

- `quarkus dev` greift dann auf die Red Hat Quarkus-Registry zu, um die Abhängigkeiten herunterzuladen.
