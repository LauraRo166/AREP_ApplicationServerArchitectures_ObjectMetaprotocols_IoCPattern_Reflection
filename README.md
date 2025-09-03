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

<img width="785" height="288" alt="image" src="https://github.com/user-attachments/assets/a33831e5-1713-41ff-9d2a-8d75ca55d0b8" />

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
   
<img width="1920" height="983" alt="image" src="https://github.com/user-attachments/assets/4d289c1f-8228-49cd-ab13-2158e73a8e97" />

---

## 🏗️ Architecture

![Arquitectura (1)](https://github.com/user-attachments/assets/814b78ca-3713-438f-936e-b86bba16bf04)


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

<img width="739" height="327" alt="image" src="https://github.com/user-attachments/assets/b7bb44b4-7cad-4860-ae3a-bbc3611ff937" />


### Testing 

- Get a greeting with:
   ```
   http://localhost:35000/app/greeting?name=Jenny
   ```

<img width="555" height="130" alt="image" src="https://github.com/user-attachments/assets/12bb83f0-7bd9-4fb2-bfc0-3abe29de7911" />


<img width="1920" height="977" alt="image" src="https://github.com/user-attachments/assets/057d957a-d5fd-4634-9274-3e49a089e34e" />


- Get if a number is even or odd with:
   ```
   http://localhost:35000/app/parity?number=364281
   ```

<img width="566" height="120" alt="image" src="https://github.com/user-attachments/assets/4fb93842-2fd0-4c82-8d9c-1152cb14af66" />


<img width="1920" height="972" alt="image" src="https://github.com/user-attachments/assets/518d8299-435c-481b-a49b-aca8b33be9fd" />

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
   http://localhost:35000/perrito.jpg
   ```

<img width="1920" height="981" alt="image" src="https://github.com/user-attachments/assets/b685c184-9fca-4109-8380-e89d8bc68610" />


---

## 👩‍💻 Author

Laura Daniela Rodríguez
