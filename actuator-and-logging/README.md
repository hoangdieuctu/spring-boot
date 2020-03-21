# Docker Service #
The service will create an endpoint for logging of all levels.

# Code
```java
@RestController
@SpringBootApplication
public class ServerApplication {

    private static Logger logger = LoggerFactory.getLogger(ServerApplication.class);

    @ResponseBody
    @GetMapping("/log")
    public void log() {
        logger.trace("TRACE message");
        logger.debug("DEBUG message");
        logger.info("INFO message");
        logger.warn("WARNING message");
        logger.error("ERROR message");
    }

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

}
```

# Dependencies
Add *spring-boot-starter-actuator*
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

# Config
Starting from Spring Boot 2.x, most of actuator endpoints are disabled by default.
Enable loggers actuator enpoints.

```properties
management.endpoints.web.exposure.include=loggers
management.endpoint.loggers.enabled=true
```

# Logger levels
```
ALL < DEBUG < INFO < WARN < ERROR < FATAL < OFF
```

# Testing
**Default logger level is INFO**

Browser at: http://localhost:8080/actuator/loggers/com.hoangdieuctu.boot.actuator

*====>*
```json
{
  "configuredLevel": null,
  "effectiveLevel": "INFO"
}
```

Browser at: http://localhost:8080/log

*====>*
```
2020-03-22 06:07:18.863  INFO 14079 --- [nio-8080-exec-5] c.h.boot.actuator.ServerApplication      : INFO message
2020-03-22 06:07:18.863  WARN 14079 --- [nio-8080-exec-5] c.h.boot.actuator.ServerApplication      : WARNING message
2020-03-22 06:07:18.863 ERROR 14079 --- [nio-8080-exec-5] c.h.boot.actuator.ServerApplication      : ERROR message
```

**Change the logger level to TRACE**

POST: http://localhost:8080/actuator/loggers/com.hoangdieuctu.boot.actuator
```json
{
  "configuredLevel": "TRACE"
}
```

Browser at: http://localhost:8080/log

*====>*
```
2020-03-22 06:09:27.764 TRACE 14079 --- [nio-8080-exec-2] c.h.boot.actuator.ServerApplication      : TRACE message
2020-03-22 06:09:27.765 DEBUG 14079 --- [nio-8080-exec-2] c.h.boot.actuator.ServerApplication      : DEBUG message
2020-03-22 06:09:27.765  INFO 14079 --- [nio-8080-exec-2] c.h.boot.actuator.ServerApplication      : INFO message
2020-03-22 06:09:27.765  WARN 14079 --- [nio-8080-exec-2] c.h.boot.actuator.ServerApplication      : WARNING message
2020-03-22 06:09:27.765 ERROR 14079 --- [nio-8080-exec-2] c.h.boot.actuator.ServerApplication      : ERROR message
```