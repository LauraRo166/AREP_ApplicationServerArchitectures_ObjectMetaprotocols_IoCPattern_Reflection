# MicroSpringBoot

This project is a lightweight Java microframework inspired by Spring Boot.
It allows developers to:

Define REST controllers using simple annotations (*@RestController*, *@GetMapping*, *@RequestParam*).

Automatically discover and register services at runtime through reflection.

Handle dynamic requests by mapping HTTP endpoints to annotated methods.

Serve static resources (HTML, CSS, JS, images) alongside REST services.

The framework is implemented from scratch without relying on external frameworks,
demonstrating how annotation processing, reflection, and an HTTP server
can be combined to create a minimal but functional micro web framework in pure Java
---

## 📦 Installation

1. Clone this repository:

   ```bash
   git clone https://github.com/LauraRo166/AREP_ApplicationServerArchitectures_ObjectMetaprotocols_IoCPattern_Reflection.git
   cd AREP_ApplicationServerArchitectures_ObjectMetaprotocols_IoCPattern_Reflection
   ```

2. Make sure you have Java 17+ and Maven installed:

   ```bash
   java -version
   mvn -version
   ```
3. Build the project:

   ```bash
   mvn clean install
   ```
4. Should look something like this:

{Imagen build}

---
## ▶️ How to Run
### In an Initial Commit

1. In initial commits it is necessary to specify the controllers when starting the server:

   ```bash
   java -cp target/classes co.edu.escuelaing.microspringboot.MicroSpringBoot co.edu.escuelaing.microspringboot.examples.GreetingController Starting MicroSpringBoot
   ```

### In the Lastest Commit

1. In the latest versions it is no longer necessary to specify the controllers:

   ```bash
   java -cp target/classes co.edu.escuelaing.microspringboot.MicroSpringBoot 
   ```

2. The server will listen on port 35000, you can open your browser and try the index file with:
   ```
   http://localhost:35000/
   ```
{Imagen pag inicial}

---

## 🏗️ Architecture

{Imagen arquitectura}

This architecture represents a simple web microframework built in Java.

1. **Client:** Sends HTTP requests (e.g., via browser or curl) to port 35000.

2. **REST Services:** Contains controllers (e.g., GreetingController, ParityController) that define the REST endpoints and implement business logic to process client requests.

3. **Microframework Web:** A lightweight web framework that handles HTTP communication, consisting of:

4. **HttpServer:** Accepts and routes incoming HTTP requests.

5. **HttpRequest:** Represents the client's request, including method, path, headers, parameters, and body.

6. **HttpResponse:** Constructs and sends responses with status codes, headers, and body.

7. **microspringboot Module:** A minimal framework inspired by Spring Boot, enabling declarative request handling using annotations:

    *@RestController, @GetMapping, @RequestParam.*

The client communicates with the REST services over HTTP. The microframework parses, routes, and responds to requests using custom server and annotation-based logic.

---

## ✅ Evaluation (Tests)

### Unit tests
Unit tests were created using JUnit to validate the server’s functionality:

To run the tests:

   ```bash
   mvn test
   ```

Should look something like this:

{Imagen test build}

### Testing 

- Get a greeting with:
   ```
   http://localhost:35000/app/greeting?name=Jenny
   ```

{Imagen a secas}
{imagen con bot+on}

- Get if a number is even or odd with:
   ```
   http://localhost:35000/app/parity?number=364281
   ```

{Imagen a secas}
{imagen con bot+on}

- You can try other resources stored in the resources folder, for example:
   ```
   perrito.jpg
   ```
  or
   ```
   gatito.jpg
   ```

  With:
   ```
   http://localhost:35000/gatito.jpg
   ```

{imagen arch estatico}

---

## 👩‍💻 Author

Laura Daniela Rodríguez
