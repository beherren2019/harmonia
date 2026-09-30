# Harmonia - Music Metadata Service

"Harmonia" ancient Greek goddess of harmony. It it named because of maintenance of music tracks.
 This service "harmonia" is a secure, microservice designed with **Java 21** and **Spring Boot 3.4.x** 
 to handle catalog infrastructure streams, multi-artist track tracking credits, and a fair cyclical "Artist of the Day"
 homepage rotation mechanism.

## Prerequisites

Ensure you have the following tools installed locally:
- **Java 21 LTS** (Eclipse Temurin recommended)
- **Apache Maven 3.9+**
- **Docker & Docker Compose**

## Local Compilation & Testing

To test and compile the microservice locally using the fully-isolated H2 In-Memory profile workspace pipeline:

```bash
# Clean project metadata artifacts and run deterministic test coverage suite
mvn clean test

# Package the runnable fat JAR archive executable file
mvn package -DskipTests
```

---

## Docker Compose Deployment 

We can build the multi-stage container setup and boot up the microservice along with a live PostgreSQL instance 
with a single terminal instruction loop:

```bash
# Boot the entire infrastructure environment in the foreground
docker-compose up --build

# Shutdown the container grid and clean up active memory volume partitions safely
docker-compose down -v
```

The system will build your target source code and start listing endpoints on port **8090**.

## Security Parameters

Every application route requires valid HTTP Basic credentials to authenticate.
- **Default Username:** `user`
- **Default Password:** `harmonia`

---

## 📊 Interactive API (Swagger UI)

When the service container grid finishes starting up, open a private incognito browser pane and navigate 
to the interactive OpenApi mapping documentation layout dashboard:

 **URL:** `http://localhost:8090/api/v1/swagger-ui/index.html`

*Note: Inside the Swagger window, press the **"Authorize"** button 
 and enter the default username and password credentials to enable testing commands 
 inside the web application context panels.*

---

## REST API Blueprints

Please refer the harmonia.http, It has the basic api calls to test via Intellij HTTP Client. 
After application start up, we have option to play around it.
     