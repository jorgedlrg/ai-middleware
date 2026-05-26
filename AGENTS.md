# Artifantasy

## Run
```bash
mvn spring-boot:run
```

## Test
```bash
mvn test
```

## Build
```bash
mvn package
```

## Tech Stack
- Spring Boot 4.X, Java 25, Maven 3
- H2 database at `~/aimiddleware/mydb` (file-based, H2 console at `/h2-console`)
- Liquibase for schema management (changeLog at `db/changelog/changelog.h2.sql`)
- Annotation processors: MapStruct + Lombok (uses `lombok-mapstruct-binding` for compatibility)
- Vaadin 25 for UI, Springdoc OpenAPI for API docs (Swagger at `/swagger-ui/index.html`)

## Architecture

Hexagonal (Ports & Adapters) with layered structure:

```
src/main/java/com/jorgedelarosa/aimiddleware/
├── domain/           # Pure Java - entities, value objects, domain events
├── application/
│   └── port/
│       ├── in/       # Use case interfaces (commands)
│       ├── out/      # Port interfaces (driven adapters)
│       └── mapper/   # MapStruct mappers
├── adapter/
│   ├── in/           # REST controllers, Vaadin UI, event consumers
│   └── out/          # JPA adapters, Web clients, message publishers
└── infrastructure/   # Spring @Configuration classes
```

### Domain

Domain entities extend:
- **Entity** - provides uuid + equals/hashCode/tostring
- **AggregateRoot** - extends Entity, adds `AggregateId` with URN format: `urn:{package}.{class}:{uuid}`

Every method that mutates a Domain class should call validate() method at the end.

### Naming Conventions

| Element | Pattern | Example |
|---------|---------|---------|
| Use case interface | `{Verb}{Entity}UseCase` | `SaveActorUseCase` |
| Use case impl | `{Verb}{Entity}UseCaseImpl` | `SaveActorUseCaseImpl` |
| Out port | `{Verb}{Entity}OutPort` | `SaveActorOutPort` |
| JPA entity | `{Entity}Entity` | `ActorEntity` |
| Persistence adapter | `{Entity}Adapter` | `ActorAdapter` |
| REST controller | `{Entity}Controller` | `ActorController` |
| Mapper | `{Entity}Mapper` | `ActorMapper` |

### Layer Rules

- **Domain**: Pure Java, no external dependencies, self-validating
- **Application**: Depends only on domain + port interfaces
- **Adapters**: Implement ports, depend on domain
- **Infrastructure**: Spring wiring only

### DTO Pattern

Use cases accept `Command` records and return `Dto` records. Mappers live in `application/port/mapper/`.

## Agent behavior

Don't assume things: if you have doubts, ask the user. This is imperative for functional behavior.