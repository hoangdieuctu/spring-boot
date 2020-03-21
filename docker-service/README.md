# Docker Service #
The service will create an endpoint for saying hello.

# Code
```java
@RestController
@SpringBootApplication
public class ServerApplication {

    @ResponseBody
    @GetMapping("/hello")
    public String hello() {
        return "Say Hello!";
    }

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

}
```
# Config
The server port is: 8080

```properties
server.port=8080
```

# Testing
Browser at: http://localhost:8080/hello

# Dockerize #

**Add Dockerfile**
```
FROM openjdk:8-jdk-alpine
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

**Build image**
```bash
docker build -t sample/docker-service .
```

*====>*
```
Sending build context to Docker daemon  17.66MB
Step 1/3 : FROM openjdk:8-jdk-alpine
 ---> a3562aa0b991
Step 2/3 : COPY target/*.jar app.jar
 ---> 06e0e3e3fb4d
Step 3/3 : ENTRYPOINT ["java","-jar","/app.jar"]
 ---> Running in 59a482815d86
Removing intermediate container 59a482815d86
 ---> 074db5f80437
Successfully built 074db5f80437
Successfully tagged sample/docker-service:latest
```

**List image**
```bash
docker image list
```
*====>*
```
REPOSITORY              TAG                 IMAGE ID            CREATED              SIZE
sample/docker-service   latest              074db5f80437        About a minute ago   122MB
```

**Run the image**
```bash
docker run -p 8080:8080 -d --name docker-service sample/docker-service
```

**List running containers (add '-a' for showing all containers)**
```bash
docker container list
```

*====>*
```
NTAINER ID        IMAGE                   COMMAND                CREATED             STATUS              PORTS                    NAMES
88d39d2afc57        sample/docker-service   "java -jar /app.jar"   42 seconds ago      Up 41 seconds       0.0.0.0:8080->8080/tcp   docker-service
```

**View container log**
```bash
docker logs docker-service
```
*or tail log*
```bash
docker logs -f docker-service 
```

*====>*
```
2020-03-21 00:46:54.123  INFO 1 --- [           main] c.h.boot.docker.ServerApplication        : Starting ServerApplication v0.0.1-SNAPSHOT on 88d39d2afc57 with PID 1 (/app.jar started by root in /)
2020-03-21 00:46:54.134  INFO 1 --- [           main] c.h.boot.docker.ServerApplication        : No active profile set, falling back to default profiles: default
2020-03-21 00:46:55.845  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2020-03-21 00:46:55.859  INFO 1 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2020-03-21 00:46:55.860  INFO 1 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.31]
2020-03-21 00:46:55.932  INFO 1 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2020-03-21 00:46:55.933  INFO 1 --- [           main] o.s.web.context.ContextLoader            : Root WebApplicationContext: initialization completed in 1697 ms
2020-03-21 00:46:56.247  INFO 1 --- [           main] o.s.s.concurrent.ThreadPoolTaskExecutor  : Initializing ExecutorService 'applicationTaskExecutor'
2020-03-21 00:46:56.444  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2020-03-21 00:46:56.447  INFO 1 --- [           main] c.h.boot.docker.ServerApplication        : Started ServerApplication in 2.991 seconds (JVM running for 3.545)
```

**Stop container**
```bash
docker stop docker-service
```

**Start container**
```bash
docker start docker-service
```