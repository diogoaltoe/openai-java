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

### OpenAI Assistants
To use `Assistants` feature, you need to:
- Go to OpenAI Platform Playground (https://platform.openai.com/playground);
- Select `Assistants` option;
- Create a new assistant;
- In `Name` field, give a proper name for it;
- Copy the `Assistant ID` that is below to the `Name` field;
- In the `application.properties` file inside `src/main/resources` folder, add:
  - openai.assistant.id={with-copied-assistant-id}

### Knowledge Retrieval

To work with Retrieval feature, you need to:
- Go to OpenAI Platform Playground (https://platform.openai.com/playground);
- Select `Assistants` option;
- In `TOOLS` option:
  - Enable `Retrieval`;
  - Upload the 2 files inside `openai-java/src/main/resources`:
    - info.md
    - policy.md

### Swagger

After run the `MainApplication`, to access the Swagger page, open this link: http://localhost:8080/swagger


## Features

This project provide the below features:
- OpenAI Chat
- OpenAI Assistants
- Token Counter
- OpenAI Exception Handler
- SpringDoc OpenAPI UI (Swagger)
- Communication with Stream
- Assistant keeping or not the thread history
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
