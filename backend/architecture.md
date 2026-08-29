# Backend Architecture Rules

## 1. Purpose

This document defines the mandatory architecture and coding rules for this Spring Boot backend.

**IMPORTANT:**

The coding agent MUST read and follow this document before creating, modifying, refactoring, or deleting backend code.

These rules are not suggestions. They are mandatory architectural constraints.

The primary architectural goal is:

* Maintain a clean and predictable codebase.
* Separate business logic from frameworks and infrastructure.
* Keep the Domain independent from Spring Boot, JPA, Hibernate, HTTP, and external systems.
* Make business logic easy to test.
* Prevent controllers and infrastructure code from becoming tightly coupled to business logic.
* Keep dependencies flowing in the correct direction.
* Prevent unnecessary abstractions and architectural violations.

---

# 2. Architecture

This project follows:

**Clean Architecture + Use Case Oriented Design + Ports & Adapters principles.**

The backend is divided into four main layers:

```text
┌──────────────────────────────────────────────┐
│                 Presentation                 │
│        REST Controllers / API DTOs           │
└──────────────────────┬───────────────────────┘
                       │
                       ▼
┌──────────────────────────────────────────────┐
│                 Application                  │
│        Use Cases / Application Services      │
│              Input / Output Ports            │
└──────────────────────┬───────────────────────┘
                       │
                       ▼
┌──────────────────────────────────────────────┐
│                   Domain                    │
│       Entities / Value Objects / Rules       │
│             Domain Interfaces                │
└──────────────────────────────────────────────┘

┌──────────────────────────────────────────────┐
│               Infrastructure                 │
│   Database / JPA / Security / External APIs │
│          Framework-specific code             │
└──────────────────────┬───────────────────────┘
                       │
                       ▼
                 Application / Domain
```

The dependency rule is:

```text
Presentation → Application → Domain

Infrastructure → Application / Domain
```

Dependencies MUST NOT point from inner layers toward outer layers.

---

# 3. Dependency Rule

The following rule is mandatory:

> Inner layers must never depend on outer layers.

The Domain layer is the innermost layer.

Therefore:

```text
Domain
  ↓
MUST NOT depend on:
- Spring
- Spring Boot
- Spring Data
- JPA
- Hibernate
- REST
- HTTP
- Controllers
- Infrastructure
- Database
- External APIs
- Framework-specific classes
```

The Domain must remain framework-independent whenever reasonably possible.

---

# 4. Package Structure

The default package structure is:

```text
src/main/java/com/example/app/

├── domain/
│   ├── entity/
│   ├── valueobject/
│   ├── repository/
│   ├── service/
│   └── exception/
│
├── application/
│   ├── usecase/
│   ├── service/
│   ├── port/
│   │   ├── in/
│   │   └── out/
│   └── dto/
│
├── infrastructure/
│   ├── persistence/
│   │   ├── entity/
│   │   ├── repository/
│   │   └── mapper/
│   │
│   ├── security/
│   ├── configuration/
│   └── external/
│
└── presentation/
    ├── controller/
    ├── dto/
    │   ├── request/
    │   └── response/
    ├── mapper/
    └── exception/
```

The exact package organization may be adapted to the existing project, but the architectural boundaries MUST remain intact.

Do not reorganize the entire project unnecessarily.

Before introducing a new package, inspect the existing structure and reuse the established conventions when they do not violate these rules.

---

# 5. Domain Layer

## Responsibility

The Domain layer contains the core business model and business rules.

It should represent what the business does, not how the system technically implements it.

Examples:

```text
domain/entity/
domain/valueobject/
domain/repository/
domain/service/
domain/exception/
```

## Domain Entity

Domain entities contain business state and behavior.

Example:

```java
@Getter
public class Todo {

    private Long id;
    private String title;
    private boolean completed;

    public void complete() {
        this.completed = true;
    }

    public void reopen() {
        this.completed = false;
    }
}
```

Business rules should live as close as possible to the domain model.

Do not move business rules into controllers.

Do not move business rules into JPA entities merely because JPA is convenient.

---

# 6. Domain MUST NOT Depend on JPA

Do NOT put JPA annotations directly on Domain entities unless there is an explicit architectural decision to do so.

Avoid:

```java
@Entity
@Table(name = "todos")
public class Todo {
}
```

Instead, keep a separate persistence model:

```text
domain/entity/Todo.java
```

and:

```text
infrastructure/persistence/entity/TodoJpaEntity.java
```

The persistence entity belongs to Infrastructure.

---

# 7. Domain Repository Interfaces

If the Domain or Application needs persistence, define an abstraction rather than depending directly on Spring Data JPA.

Example:

```java
public interface TodoRepository {

    Todo save(Todo todo);

    Optional<Todo> findById(Long id);

    void delete(Todo todo);
}
```

The interface belongs to the inner layer.

The implementation belongs to Infrastructure.

Example:

```text
Domain/Application
        │
        ▼
TodoRepository
        ▲
        │
TodoRepositoryAdapter
        │
        ▼
TodoJpaRepository
```

---

# 8. Application Layer

The Application layer contains application-specific business workflows and use cases.

Examples:

```text
CreateTodoUseCase
GetTodoUseCase
UpdateTodoUseCase
DeleteTodoUseCase
CompleteTodoUseCase
```

The Application layer orchestrates Domain objects.

It should answer:

> What operation does the system need to perform?

The Domain should answer:

> What business rules govern the operation?

---

# 9. Use Case Rules

Each meaningful business operation should be represented by a Use Case when appropriate.

Example:

```java
public interface CreateTodoUseCase {

    TodoResult execute(CreateTodoCommand command);
}
```

Implementation:

```java
@RequiredArgsConstructor
public class CreateTodoService implements CreateTodoUseCase {

    private final TodoRepository todoRepository;

    @Override
    public TodoResult execute(CreateTodoCommand command) {
        // application workflow
    }
}
```

Do not create unnecessary Use Case classes for trivial internal operations without a meaningful application boundary.

However, do not bypass an existing Use Case simply to make implementation faster.

---

# 10. Application MUST NOT Depend on Presentation

Application code MUST NOT import:

```text
Controller
Request DTO
Response DTO
HttpServletRequest
ResponseEntity
REST-specific classes
```

For example, this is forbidden:

```java
public TodoResponse create(CreateTodoRequest request) {
}
```

Instead:

```java
public TodoResult execute(CreateTodoCommand command) {
}
```

Presentation is responsible for converting HTTP/API objects into Application inputs.

---

# 11. Presentation Layer

The Presentation layer handles external API communication.

Responsibilities include:

* HTTP endpoints
* Request DTOs
* Response DTOs
* Request validation
* Mapping API models to Application inputs
* Mapping Application outputs to API responses
* HTTP status codes
* API-level exception handling

Controllers MUST be thin.

Example:

```java
@RestController
@RequestMapping("/api/v1/todos")
@RequiredArgsConstructor
public class TodoController {

    private final CreateTodoUseCase createTodoUseCase;

    @PostMapping
    public ResponseEntity<TodoResponse> create(
            @Valid @RequestBody CreateTodoRequest request) {

        CreateTodoCommand command =
                TodoApiMapper.toCommand(request);

        TodoResult result =
                createTodoUseCase.execute(command);

        return ResponseEntity.ok(
                TodoApiMapper.toResponse(result)
        );
    }
}
```

---

# 12. Controller Rules

Controllers MUST NOT contain business logic.

Controllers should NOT:

* Perform database queries directly.
* Access JPA repositories directly.
* Modify Domain entities with complex business logic.
* Perform complex calculations.
* Implement business rules.
* Contain transaction workflows.
* Call external APIs directly.
* Contain large conditional business flows.

The expected flow is:

```text
HTTP Request
     ↓
Controller
     ↓
Request DTO
     ↓
Application Command / Query
     ↓
Use Case
     ↓
Domain
     ↓
Infrastructure
```

---

# 13. DTO Rules

Do not expose Domain entities directly through REST APIs.

Avoid:

```java
@GetMapping("/{id}")
public Todo getTodo(...) {
}
```

Instead use:

```text
Request DTO
Response DTO
```

Example:

```java
@Getter
@Setter
public class CreateTodoRequest {

    @NotBlank
    private String title;
}
```

Response:

```java
@Getter
@Builder
public class TodoResponse {

    private Long id;
    private String title;
    private boolean completed;
}
```

API DTOs belong to Presentation.

They must not leak into Domain.

---

# 14. Persistence Layer

All JPA and database-specific code belongs to Infrastructure.

Example:

```text
infrastructure/persistence/

├── entity/
│   └── TodoJpaEntity.java
│
├── repository/
│   ├── TodoJpaRepository.java
│   └── TodoRepositoryAdapter.java
│
└── mapper/
    └── TodoPersistenceMapper.java
```

Spring Data interfaces belong to Infrastructure.

Example:

```java
public interface TodoJpaRepository
        extends JpaRepository<TodoJpaEntity, Long> {
}
```

Application and Domain MUST NOT directly depend on `JpaRepository`.

---

# 15. Persistence Entity vs Domain Entity

Keep these models separate when following strict Clean Architecture.

```text
Domain Entity
    ↓
Persistence Mapper
    ↓
JPA Entity
```

And:

```text
JPA Entity
    ↓
Persistence Mapper
    ↓
Domain Entity
```

Do not allow database concerns to leak into the Domain.

---

# 16. Mapper Rules

Mappings between architectural boundaries should be explicit.

Typical mappings:

```text
Presentation DTO
        ↓
Application Command
```

```text
Application Result
        ↓
Presentation Response DTO
```

```text
Domain Entity
        ↓
Persistence Entity
```

```text
Persistence Entity
        ↓
Domain Entity
```

Use dedicated mapper classes or methods.

Do not put large mapping logic inside Controllers.

Do not put API mapping logic inside Domain entities.

---

# 17. Infrastructure Layer

Infrastructure contains implementation details.

Examples:

```text
- JPA
- Hibernate
- Database configuration
- Spring Security
- JWT implementation
- Redis
- Kafka
- RabbitMQ
- External REST clients
- File storage
- Email providers
- Third-party APIs
```

Infrastructure can depend on inner layers.

Inner layers must not depend on Infrastructure.

---

# 18. Transaction Rules

Transactions belong to the Application boundary whenever possible.

Prefer:

```java
@Transactional
public TodoResult execute(CreateTodoCommand command) {
}
```

rather than putting transaction management inside Domain entities.

Do not randomly add `@Transactional` everywhere.

Use transactions around application use cases that represent an atomic business operation.

For read-only operations, consider:

```java
@Transactional(readOnly = true)
```

when appropriate.

---

# 19. Validation Rules

API input validation belongs primarily to Presentation.

Example:

```java
@NotBlank
@Size(max = 255)
private String title;
```

However, business invariants must still be protected inside the Domain.

Never assume that API validation alone guarantees Domain correctness.

For example:

```text
API validation:
"title must not be blank"

Domain rule:
"Completed Todo cannot be modified"
```

The Domain must enforce the second rule.

---

# 20. Exception Handling

Do not expose raw exceptions from the API.

Use appropriate application/domain exceptions.

Example:

```text
domain/exception/
    TodoNotFoundException
    InvalidTodoStateException
```

Presentation should translate exceptions into appropriate HTTP responses.

Use a centralized exception handler when appropriate:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
}
```

Do not put HTTP-specific response handling inside Domain exceptions.

---

# 21. Lombok Rules

## MANDATORY LOMBOK POLICY

This project uses Lombok.

The coding agent MUST use Lombok where it reduces boilerplate and does not harm clarity.

### NEVER manually write constructors when Lombok can generate them.

Forbidden:

```java
public TodoService(TodoRepository todoRepository) {
    this.todoRepository = todoRepository;
}
```

Use:

```java
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
}
```

For Spring dependency injection, prefer constructor injection with:

```java
@RequiredArgsConstructor
```

Do NOT use:

```java
@Autowired
private TodoRepository todoRepository;
```

Prefer:

```java
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
}
```

---

# 22. Lombok Annotations

Use appropriate Lombok annotations.

Common examples:

```text
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Data
@Value
@Slf4j
```

However, do not blindly use `@Data` everywhere.

Prefer more explicit annotations when possible.

For example:

```java
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoResponse {
}
```

is preferred over blindly using:

```java
@Data
public class TodoResponse {
}
```

Use Lombok according to the responsibility of the class.

---

# 23. Constructor Rules

The coding agent MUST follow these rules:

1. Do not manually create constructors if Lombok can generate them.
2. Use `@RequiredArgsConstructor` for constructor injection.
3. Use `@NoArgsConstructor` when a no-argument constructor is required.
4. Use `@AllArgsConstructor` only when all-argument construction is actually appropriate.
5. Do not create multiple unnecessary constructors.
6. Do not use field injection with `@Autowired`.
7. Prefer immutable dependencies using `final` fields.

Example:

```java
@RequiredArgsConstructor
@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final TodoMapper todoMapper;
}
```

NOT:

```java
@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final TodoMapper todoMapper;

    public TodoService(
            TodoRepository todoRepository,
            TodoMapper todoMapper) {
        this.todoRepository = todoRepository;
        this.todoMapper = todoMapper;
    }
}
```

---

# 24. Lombok and Domain Objects

Do not use Lombok blindly on Domain entities.

The Domain model must prioritize business correctness over reducing boilerplate.

For example, avoid automatically generating unrestricted setters for Domain entities if that would allow invalid state changes.

Prefer:

```java
@Getter
public class Todo {

    private String title;
    private boolean completed;

    public void complete() {
        this.completed = true;
    }

    public void rename(String title) {
        // validate business rules
        this.title = title;
    }
}
```

instead of:

```java
@Getter
@Setter
public class Todo {
}
```

when unrestricted mutation would violate business rules.

---

# 25. Dependency Injection

Always prefer constructor injection.

Use:

```java
@RequiredArgsConstructor
@Service
public class TodoService {

    private final TodoRepository todoRepository;
}
```

Avoid:

```java
@Autowired
private TodoRepository todoRepository;
```

Avoid service locator patterns.

Avoid static access to Spring-managed services.

---

# 26. Service Rules

Do not create a giant `Service` class containing the entire application.

Bad:

```text
TodoService
    ├── create
    ├── update
    ├── delete
    ├── complete
    ├── search
    ├── export
    ├── notification
    ├── payment
    └── ...
```

Prefer meaningful Use Cases when the application becomes complex:

```text
CreateTodoUseCase
UpdateTodoUseCase
CompleteTodoUseCase
DeleteTodoUseCase
SearchTodoUseCase
```

Do not split classes artificially when the use case is trivial.

Use judgment.

---

# 27. Repository Rules

Repository abstractions should represent business/application needs rather than exposing unnecessary database details.

Avoid leaking JPA types:

```java
Optional<TodoJpaEntity>
```

into Application or Domain.

Prefer:

```java
Optional<Todo>
```

inside the inner layers.

Do not expose:

```text
Page<TodoJpaEntity>
JpaSpecificationExecutor
EntityManager
JpaRepository
```

to Domain/Application unless there is an explicit architectural reason.

---

# 28. Database Queries

Database-specific query logic belongs to Infrastructure.

Do not write SQL/JPA queries inside:

```text
Controller
Domain
Application
```

They belong in:

```text
infrastructure/persistence/
```

---

# 29. Security

Security implementation details belong to Infrastructure.

Examples:

```text
JWT
Spring Security
Authentication filters
Password encoders
OAuth2
Security configuration
```

Domain should not depend directly on Spring Security.

Application should depend on abstractions when it needs authentication-related information.

For example:

```java
public interface CurrentUserProvider {

    Long getCurrentUserId();
}
```

Infrastructure can provide the implementation using Spring Security.

---

# 30. External Services

External API clients belong to Infrastructure.

Do not call third-party APIs directly from Controllers or Domain.

Preferred:

```text
Application
    ↓
Output Port
    ↓
Infrastructure Adapter
    ↓
External API
```

Example:

```java
public interface EmailSender {

    void send(String recipient, String subject, String content);
}
```

Infrastructure:

```java
@Component
@RequiredArgsConstructor
public class SmtpEmailSender implements EmailSender {
}
```

---

# 31. Configuration

Framework and infrastructure configuration belongs to:

```text
infrastructure/configuration/
```

Examples:

```text
DatabaseConfig
SecurityConfig
RedisConfig
KafkaConfig
JacksonConfig
OpenApiConfig
```

Do not put business configuration logic inside configuration classes.

---

# 32. Naming Rules

Use clear and intention-revealing names.

Examples:

```text
CreateTodoUseCase
CreateTodoService
CreateTodoCommand
TodoResult
TodoController
TodoResponse
TodoJpaEntity
TodoRepositoryAdapter
TodoPersistenceMapper
```

Avoid generic names such as:

```text
Manager
Helper
Util
CommonService
BaseService
Processor
Handler
```

unless the responsibility is genuinely clear and justified.

---

# 33. Utility Classes

Do not create utility classes just to avoid deciding where logic belongs.

Bad:

```text
TodoUtils
CommonUtils
ApplicationUtils
StringUtils
```

Ask first:

> Does this logic belong to Domain, Application, Presentation, or Infrastructure?

Business logic should normally belong to Domain.

Application workflow belongs to Application.

Technical helper logic belongs to Infrastructure.

---

# 34. Shared Package

A shared package should remain small.

Use it only for genuinely cross-cutting concepts.

Examples:

```text
shared/
    exception/
    constants/
    response/
    utility/
```

Do not use `shared` as a dumping ground.

If a class belongs to a specific feature, keep it inside that feature or layer.

---

# 35. Feature Boundaries

When adding a new feature:

1. Identify the business capability.
2. Determine which Domain concepts are required.
3. Define the Use Case.
4. Define required input/output ports.
5. Implement Infrastructure adapters.
6. Add Presentation endpoints.
7. Add tests.

Do not immediately modify unrelated modules.

---

# 36. Existing Code First

Before creating new code, the coding agent MUST inspect the existing project.

The agent should identify:

* Existing package structure.
* Existing architectural conventions.
* Existing entities.
* Existing repositories.
* Existing use cases.
* Existing DTOs.
* Existing exception handling.
* Existing security implementation.
* Existing configuration.
* Existing naming conventions.
* Existing tests.

Do not create duplicate implementations when an existing component can be reused.

---

# 37. Do Not Over-Engineer

Clean Architecture does NOT mean creating abstractions for everything.

Do not create:

```text
Interface
    ↓
Abstract class
    ↓
Implementation
    ↓
Factory
    ↓
Builder
```

without a real architectural or business reason.

Every abstraction should have a purpose.

Use interfaces primarily at architectural boundaries and where dependency inversion provides meaningful value.

---

# 38. Do Not Violate Existing Architecture to Save Time

Never bypass architecture just because a shortcut is easier.

Forbidden shortcuts include:

```text
Controller → Repository
Controller → Database
Controller → External API

Application → JpaRepository
Application → JpaEntity

Domain → JPA
Domain → Spring

Domain → Infrastructure
```

If the current architecture makes a task difficult, determine the correct architectural solution instead of bypassing the architecture.

---

# 39. Refactoring Rules

Before refactoring:

1. Understand the current implementation.
2. Identify the architectural problem.
3. Determine the smallest safe change.
4. Preserve existing behavior unless the task explicitly requires behavior changes.
5. Avoid unrelated refactoring.
6. Run tests after the change.

Do not rewrite entire modules unnecessarily.

---

# 40. Testing Architecture

Tests should reflect architectural boundaries.

Recommended structure:

```text
src/test/java/

├── domain/
├── application/
├── infrastructure/
└── presentation/
```

Domain tests should be as independent from Spring as possible.

Application tests should test use-case behavior.

Infrastructure tests should verify database/external-system integration.

Presentation tests should verify HTTP/API behavior.

---

# 41. Before Coding Checklist

Before implementing any task, the coding agent MUST ask itself:

```text
1. Which architectural layer does this code belong to?
2. What is the business use case?
3. Does this logic belong to Domain or Application?
4. Is this a framework/infrastructure concern?
5. Am I introducing an illegal dependency?
6. Can I reuse an existing abstraction?
7. Do I need a new interface?
8. Am I putting business logic inside a Controller?
9. Am I exposing a Domain entity through the API?
10. Am I leaking JPA types outside Infrastructure?
11. Can Lombok remove boilerplate?
12. Am I manually writing a constructor that Lombok can generate?
13. Am I modifying unrelated code?
14. Do existing tests need to be updated?
```

---

# 42. Before Finishing a Task

Before considering the task complete, verify:

```text
[ ] Correct architectural layer
[ ] Correct dependency direction
[ ] No Domain → Framework dependency
[ ] No Application → Presentation dependency
[ ] No Controller → Repository shortcut
[ ] No Application → JPA dependency
[ ] No Domain → Infrastructure dependency
[ ] DTO boundaries are respected
[ ] Persistence entities stay in Infrastructure
[ ] Mapping is handled at boundaries
[ ] Business rules are not inside Controllers
[ ] Transactions are placed appropriately
[ ] Exceptions are handled properly
[ ] Constructor injection is used
[ ] Lombok is used appropriately
[ ] No unnecessary manual constructors
[ ] No unnecessary abstractions
[ ] Existing code was reused where appropriate
[ ] Tests were added/updated when necessary
[ ] No unrelated refactoring was introduced
```

---

# 43. Architecture Violation Policy

If a requested implementation conflicts with this architecture, DO NOT silently violate the architecture.

Instead:

1. Identify the violation.
2. Explain why it violates the architecture.
3. Propose the correct architectural approach.
4. Implement the correct approach unless the user explicitly instructs otherwise.

If the user explicitly requests an architectural violation, clearly identify the trade-off before implementing it.

---

# 44. Golden Rule

Always remember:

```text
Presentation
    ↓
Application
    ↓
Domain

Infrastructure
    ↓
Application / Domain
```

And never:

```text
Domain
    ↓
Infrastructure       ❌

Domain
    ↓
Spring/JPA           ❌

Application
    ↓
Presentation         ❌

Controller
    ↓
Repository           ❌
```

The most important principle is:

> **Business rules must remain independent from technical implementation details.**

The coding agent must preserve this principle in every backend change.
