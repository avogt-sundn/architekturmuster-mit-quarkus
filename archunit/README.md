# Namenskonventionen und Modulsichten prüfen mit Archunit

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

