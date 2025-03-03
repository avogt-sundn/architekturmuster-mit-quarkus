# Namenskonventionen und Modulsichten prüfen mit Archunit

Ziele von Hex Arch.

- denke NICHT in Infrastruktur wie Rest-APIs, SQL, messaging etc.
- Testbarkeit
- Lesbarkeit / Verständlichkeit durch Entkopplung
- Code Dependency Graph hat keine Zirkel und klar erlaubte Richtungen
- Mapping

````bash
.
├── adapter
│   ├── rest
│   │   ├── api
│   │   │   ├── in
│   │   │   │   ├── mapper
│   │   │   │   └── out
│   │   │   │       ├── client
│   │   │   │       └── mock
│   │   │   ├── out
│   │   │   │       ├── client
│   │   │   │       └── mock
│   │   └── ui
│   └── storage
│       └── generic
│           ├── mapper
│           └── repository
├── application
│   └── usecases
│       ├── arv
│       ├── azk
│       ├── init
│       ├── kuv
│       ├── odv
│       ├── util
│       └── validation
├── domain
│   └── subdomain
````

## Schritt 1: Objekte in der Domäne

1. Erstelle das fachliche Datenmodell mit Entitäten und ihren Attributen.
1. Ergänze die Entitäten um fachliche Methoden zu Objekten.

