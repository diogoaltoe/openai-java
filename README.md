# OpenAI Java
OpenAI GPT integration project using Java.

## Installation

Download the project using the below GIT command:
```
$ git clone https://github.com/diogoaltoe/openai-java.git
```

## Usage

### Environment Variable
In the `application.properties` file inside `src/main/resources` folder, set:
  - `openai-api-key=${OPENAI_API_KEY}` with your OpenAi API key.

### Swagger
After run the `MainApplication`, to access the Swagger page, open this link: http://localhost:8080/swagger

## Features
- OpenAI Chat
- OpenAI Assistant
- Token Counter
- OpenAI Exception Handler
- SpringDoc OpenAPI UI (Swagger)
- Communication with Stream
- Keep thread history
- Clear thread history
- Knowledge Retrieval
- Function Calling


## Libraries

- [Spring Boot](https://spring.io/)
- [Lombok](https://projectlombok.org/)
- [Theo Kanning - openai-java](https://github.com/TheoKanning/openai-java)
- [SpringDoc OpenAPI UI](https://springdoc.org/)
- [JTokkit - Java Tokenizer Kit](https://github.com/knuddelsgmbh/jtokkit)


## License

This project is licensed under the [MIT License](https://opensource.org/licenses/MIT).
