# Artifantasy
An experimental roleplay application which uses LLMs.

The idea here is to create a middleware to connect different AI frontend (but one is provided) to AI providers (like chat backends, voice backends, etc) in order to create a roleplay experience.

I'll apply a DDD approach because I want to use this project as an exercise, and for the frontend, I'm using [Vaadin](https://vaadin.com/docs/latest/getting-started), which can be used completely with Java code, since I have no idea of Javascript 😁.

You can find documentation and info in the ./doc folder, like this [functional draft](/doc/functional_draft.md)

To launch this, you need Java 21 and Maven 3, then, clone this repository and run:
```bash
mvn spring-boot:run
```

To explore the API, you can use the embedded swagger ui:
[localhost swagger UI](http://localhost:8080/swagger-ui/index.html)