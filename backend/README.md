# Backend

One of the modules of this Survey application.

Features:
- create a survey, empty or with questions
- add questions to a survey
- see surveys
- public or private surveys
- share a survey (not yet)
- published or unpublished survey (not yet)
- take a survey
- see responses of your surveys
- log in as USER or ADMIN
- authentication and authorization for all types of object access (read, write, etc)

## Basic Class Diagrams

> It all begins with a survey, represented by the Survey class:

![backend survey](/img/backend/survey.png)

> A lightweight object that contains a name, a description (purpose) and the owner of a survey. Questions can be added later.

> We represent questions in a survey via the Question class.

![backend question](/img/backend/question.png)

> The answer to a question is represented via the QuestionResponse class.

![backend question response](/img/backend/question-response.png)

> The QuestionResponse has a reference to the Question it answers, and is part of a bigger object called SurveyResponse:

![backend survey response](/img/backend/survey-response.png)

> This class contains a reference to a User (who took the survey), and to a list of question responses.

## JPA

All above classes are annotated with JPA annotations to control the different relationships between objects.

#### An example:

In the `QuestionResponse` class, we want/need to hold a reference to a Question object.

This is done via a @ManyToOne annotation, meaning many QuestionResponse objects hold a reference to the same Question object.

We also say (via the @JoinColumn annotation) that the name of the column in the QuestionResponse table will be 'question_id'.

In this way, we are taking the `id` of a `Question` row and putting it into the `question_id` column value of a QuestionResponse object or row.
```java
public abstract class QuestionResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
    //rest of the code...
}
```

## MVC pattern

We define a clear separation of concerns between application components via the MVC (Model-View-Component) pattern, primarily just by using the Spring Boot 6 framework and following the recommended workflow that its `.prototype.*` annotations provide:
- controller (@RestController)
- service (@Service)
- repository (@Repository)

We define responsibilities for these application layers: 
- a controller takes in the request as it comes from the httpServlet and returns a response. It may process any authorization step (like obtaining the ID of the authenticated user and passing it to a service, so that testing is easier)
```java
@PostMapping("/api/survey")
    public ResponseEntity<SurveyDTO> createSurvey(@Valid @RequestBody SurveyRequest surveyRequest) {
        String userEmail = SecurityUtils.getAuthenticatedUserEmail();

        return ResponseEntity.ok(surveyService.createSurvey(surveyRequest, userEmail));
    }
```
- the service takes care of executing the business logic
```java
public SurveyDTO createSurvey(SurveyRequest surveyRequest, String userEmail) {
        log("Survey: " + surveyRequest.getName());
        log("User: " + userEmail);

        User owner = userRepository.findByEmail(userEmail).orElseThrow(() -> {
            String errorText = "User not found for email: " + userEmail;
            return new EntityNotFoundException(errorText);
        });

        Survey survey = surveyRequest.toSurvey(owner);
        return SurveyDTO.toSurveyDto(surveyRepository.save(survey));
    }
```

It may perform integrity checks, or log operations for debugging or auditing purposes.

The response, in the case of business objects, will be a DTO (Data Transfer Object): a serializable, secure object that can be exposed via an API. This object may not contain any passwords or useless information.

- the repository is the interface to the storage system: in our case, its Hibernate configured to talk to the PostgreSQL backend instance.

## Exceptions

We add a global exception handler class for catching all thrown exceptions and processing them.

When a client hits a 400 or a 500, we want to present the problem in a readable and structured way:

Our `ErrorResponse` class helps us structure the error message sent out to the client.
```java
public class ErrorResponse implements Serializable {
    private int status;
    private String message;
    private Map<String, String> errors;
    private long timestamp;
    //...
}
```

And this is how we catch an `IllegalArgumentException` exception.

```java
@ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        HashMap<String, String> map = new HashMap<>(); // In some cases, more than one error can be included in the response. So we use a map 
        map.put("Illegal argument", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse( // build the object
                HttpStatus.BAD_REQUEST.value(),
                "Illegal argument provided",
                map
        );
        logger.error("{}: {} {} {}", errorResponse.getTimestamp(), errorResponse.getStatus(), errorResponse.getMessage(), errorResponse.getErrors()); // log it
        httpLogger.httpLog("backend", HttpStatus.BAD_REQUEST.value() + " Illegal Argument Exception-> " + map, "ERROR"); // we also log to our http logging service
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
```

## Testing

We showcase testing by testing the controller layer and the service layer of the Question package.

### Controller layer testing: the QuestionControllerTest class

```java
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class QuestionControllerTest {
    //...
}
```

- There is a `test` profile we run via the `application-test.properties` file, which is picked by the `@ActiveProfile` annotation.

- We also use `@SpringBootTest`, which allows us to automatically set up the Spring test application context, and `@AutoConfigureMockMvc`, which allows us to mock http requests without setting up a real http server.

In the following example, we're testing this endpoint:

The actual controller (from the QuestionController class):
```java
@GetMapping("/public/questions")
    public ResponseEntity<List<QuestionDTO>> getAll() {
        return ResponseEntity.ok(questionService.getAllQuestions());
    }
```

Tested with this test method

```java
//..annotations
public class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QuestionService questionService;
    //...

    @Test
    public void getAllQuestions_shouldReturnAll() throws Exception {
        // Arrange
        List<QuestionDTO> questions = getDtoQuestions();

        when(questionService.getAllQuestions()).thenReturn(questions);

        // Act & Assert
        mockMvc.perform(get("/public/questions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].questionText").value("What is your favorite color?"))
                .andExpect(jsonPath("$[1].questionText").value("What is your favorite fruit?"));
    }
    //...
}
```

We use a MockMvc object for mocking http requests.

We divide the test method with Arrange-Act-Assert stages.
- Arrange: set up the DTO questions that will be returned when the `getAllQuestions()` method is called inside the service method we're testing.
- Act: perform the http request
- Assert: check that the questions contain the text that was returned. This is the simplest of tests.


### Service layer testing

Check the [QuestionServiceTest](src/test/java/com/maurooyhanart/surveyq/backend/question/QuestionServiceTest.java) class for a more in depth test of the QuestionService class.
 
## The HttpClient: a small, shared module for connecting to other services

We define a bean of type HttpLogger (explained in the [shared README file](../shared/README.md)) in the `HttpClient` config class:

```java
@Configuration
public class HttpClientConfig {
    @Bean
    public HttpLogger httpLogger(@Value("${logging.url}") String loggingUrl,
                                 @Value("${api.key}") String apiKey,
                                 RestTemplate restTemplate) {
        return new HttpLogger(new HttpClient(
                loggingUrl + "/log", apiKey, restTemplate
        ));
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

This way we can register the `HttpLogger` in our application as a spring bean, as done in the `GlobalExceptionHandler` shown in the [exceptions](#exceptions) section of this documentation.

## Maven Profiles

Two (three) profiles are defined:
- 1: a default profile used for local execution
- 2: a docker profile used for docker + docker-compose or docker swarm.
- 3: a test profile, for running tests

We choose the profile by doing one of three things:
- 1: nothing
- 2: this:
```Dockerfile
RUN mvn clean package -DskipTests -Dspring.profiles.active=docker -f ./pom.xml && \
    mv target/*.jar target/survey-${MODULE_NAME}-${VERSION}.jar
```

- 3: this:
```java
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class QuestionControllerTest {
    //...
}
```