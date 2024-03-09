# OpenAI Java

OpenAI GPT integration project using Java.


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


## Installation

Download the project using the below GIT command:
```
$ git clone https://github.com/diogoaltoe/openai-java.git
```


## Usage

Basically, you can use this project in 2 ways:
- Via `Swagger`:
  - After run the `MainApplication`, to access the Swagger page, open this link: http://localhost:8080/swagger
- Via `Test Cases`:
  - Go to `test` folder to check the different ways to test and use the features.
  - **NOTE:** The test cases have a few examples on how to use the project, maybe it can help you.

Below we have more details on how to setup the OpenAI features.

### OpenAI API

To use `OpenAI API`, you need to:
- Go to OpenAI Platform Playground (https://platform.openai.com);
- In the left menu, click on `API keys` option;
- Click on `+ Create new secret key` button;
- Fill the `Name` field and click on `Create secret key` button;
- Copy the `key` and set the `OPENAI_API_KEY` environment variable with it.
  - This environment variable will be used in the `application.properties` file inside `src/main/resources` folder:
    - `openai-api-key=${OPENAI_API_KEY}`

### OpenAI Assistants

To use `Assistants` feature, you need to:
- Go to OpenAI Platform Playground (https://platform.openai.com/playground);
- Select `Assistants` option;
- Create a new assistant;
- In `Name` field, give a proper name for it;
- In `Instructions` field, add these instructions:
```
  You are a chatbot for an ecommerce app.
  Try to be direct in you answer.
  If someone ask you to list something, use list numbers.
```
- Copy the `Assistant ID` that is below to the `Name` field;
- In the `application.properties` file inside `src/main/resources` folder, add:
  - openai.assistant.id={with-copied-assistant-id} 

### Knowledge Retrieval

To work with `Retrieval` feature, you need to:
- Go to OpenAI Platform Playground (https://platform.openai.com/playground);
- Select `Assistants` option;
- In `TOOLS` option:
  - Enable `Retrieval`;
  - Upload the 2 files inside `openai-java/src/main/resources`:
    - info.md
    - policy.md

### Functions Calling

To work with `Functions` feature, you need to:
- Go to OpenAI Platform Playground (https://platform.openai.com/playground);
- Select `Assistants` option;
- In `TOOLS` option:
  - In `Functions`, click in `+Function` button;
  - Open the `calculateShipping.json` file inside `openai-java/src/main/resources`, copy the content and paste it in the function field;
  - Then, click on `Save` button;
  - It should save the new function and show below `Functions` the `calculateShipping` function name.


## Libraries

- [Spring Boot](https://spring.io/)
- [Lombok](https://projectlombok.org/)
- [Theo Kanning - openai-java](https://github.com/TheoKanning/openai-java)
- [SpringDoc OpenAPI UI](https://springdoc.org/)
- [JTokkit - Java Tokenizer Kit](https://github.com/knuddelsgmbh/jtokkit)


## License

This project is licensed under the [MIT License](https://opensource.org/licenses/MIT).
