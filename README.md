# Artifantasy

[![Java](https://img.shields.io/badge/Java-25-00758F?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-6DB33F?logo=spring&logoColor=white)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3-C71A36?logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-Proprietary-red.svg)](LICENSE)

An experimental roleplay application powered by LLMs. Create scenarios, build characters, and let AI bring them to life.

## Key Features

- **Scenarios** — Create and manage roleplay scenarios with custom settings
- **Actors** — Build AI characters with distinct personalities and traits
- **LLM Integration** — Let LLMs interpret and roleplay any character dynamically
- **REST API** — Full OpenAPI-documented API with Swagger UI
- **Web UI** — Modern Vaadin-based interface
- **Persistent Storage** — H2 file-based database with Liquibase migrations

## Tech Stack

| Component | Technology |
|-----------|------------|
| Framework | Spring Boot 4.0.6 |
| UI | Vaadin 25 |
| Database | H2 (file-based) |
| Migrations | Liquibase |
| API Docs | Springdoc OpenAPI (Swagger) |
| Mapping | MapStruct + Lombok |
| Metrics | Micrometer + Prometheus |

## Quick Start

```bash
# Prerequisites: Java 25, Maven 3

# Clone and run
git clone https://github.com/jorgedlrg/ai-middleware.git
cd ai-middleware
mvn spring-boot:run
```

## Links

| Resource | URL |
|----------|-----|
| Swagger UI | http://localhost:8080/swagger-ui/index.html |
| H2 Console | http://localhost:8080/h2-console |
| API Docs | http://localhost:8080/v3/api-docs |

## Project Info

- **Version:** 0.0.1-SNAPSHOT
- **Developer:** Jorge de la Rosa Giner
- **Documentation:** [`./doc`](./doc)

---

**License:** Proprietary — See [`LICENSE`](LICENSE) for details. Commercial use is prohibited without prior written consent.