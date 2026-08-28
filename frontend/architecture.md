# Android Development Guidelines

You are an expert Android developer. Build this application using modern Android development practices and follow the architecture and conventions below strictly.

## 1. Technology Stack

Use:

* Kotlin as the primary programming language
* Jetpack Compose for UI
* Material 3 for UI components and design
* MVVM for the Presentation layer
* Clean Architecture with Presentation, Domain, and Data layers
* Repository Pattern
* Coroutines for asynchronous operations
* StateFlow for UI state
* Retrofit for REST API communication
* Kotlin Serialization or Gson for JSON serialization, depending on the existing project setup
* Hilt for Dependency Injection
* Navigation Compose for navigation

The backend is a Java Spring Boot REST API.

Do not introduce Flutter, Dart, XML-based UI, or other UI frameworks unless explicitly requested.

---

# 2. Architecture

Follow this dependency direction strictly:

Presentation → Domain → Data

The layers must have the following responsibilities:

### Presentation

Responsible for:

* Jetpack Compose UI
* ViewModels
* UI state
* UI events
* Screen-level state management
* Navigation-related UI logic

Presentation must NOT directly call Retrofit, API services, Room, or other data sources.

Example:

Compose Screen
      ↓
   ViewModel
      ↓
   UseCase

---

### Domain

Responsible for:

* Business logic
* Use Cases
* Domain models
* Repository interfaces

The Domain layer must be independent of Android framework details and external data sources whenever practical.

For example:

domain/
├── model/
├── repository/
└── usecase/

A UseCase should represent one meaningful business operation.

Example:

class LoginUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        username: String,
        password: String
    ): Result<User> {
        return repository.login(username, password)
    }
}

The Domain layer must depend on repository interfaces, not repository implementations.

---

### Data

Responsible for:

* API communication
* Local database/cache when required
* DTOs
* Repository implementations
* Data mapping

Example:

data/
├── remote/
│   ├── api/
│   └── datasource/
├── local/
├── dto/
├── mapper/
└── repository/

The Data layer implements interfaces defined in the Domain layer.

Example:

Domain
    ↓
AuthRepository interface

Data
    ↓
AuthRepositoryImpl

---

# 3. Dependency Direction

Always maintain this dependency direction:

Presentation
      ↓
   Domain
      ↑
      │
     Data

More specifically:

Presentation → Domain
Data → Domain

Domain must NOT depend on Data.

Domain must NOT know about:

* Retrofit
* API services
* DTOs
* Room
* Android Context
* Compose
* ViewModel
* Spring Boot

---

# 4. MVVM Rules

Use MVVM in the Presentation layer.

The typical flow should be:

User
 ↓
Composable
 ↓
ViewModel
 ↓
UseCase
 ↓
Repository
 ↓
Data Source
 ↓
REST API

For the response:

REST API
 ↓
Data Source
 ↓
Repository
 ↓
UseCase
 ↓
ViewModel
 ↓
UiState
 ↓
Composable

Do not put business logic directly inside Composable functions.

Do not make Composables responsible for API calls.

Do not put Repository logic inside ViewModels.

---

# 5. UI State

Prefer a single immutable UI state for each screen.

Example:

data class LoginUiState(
    val isLoading: Boolean = false,
    val username: String = "",
    val password: String = "",
    val errorMessage: String? = null,
    val isLoginSuccessful: Boolean = false
)

Expose state from ViewModel using StateFlow.

Example:

private val _uiState = MutableStateFlow(LoginUiState())
val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

The UI should observe the state and render itself based on that state.

Prefer unidirectional data flow:

UI
 ↓ events
ViewModel
 ↓ state
UI

---

# 6. API Communication

The backend is a Java Spring Boot REST API.

Keep API-specific models separate from Domain models.

For example:

LoginRequestDto
UserResponseDto

must not automatically become Domain models.

Use mappers:

DTO
 ↓
Mapper
 ↓
Domain Model

Example:

fun UserResponseDto.toDomain(): User {
    return User(
        id = id,
        name = name,
        email = email
    )
}

Do not expose Retrofit DTOs directly to the Presentation layer.

---

# 7. Error Handling

Do not let raw exceptions leak throughout the application.

Handle API and application errors at the appropriate layer.

The UI should receive a clean representation of the state, for example:

Loading
Success
Error

Prefer explicit and predictable error handling.

For example:

sealed interface UiState<out T> {

    data object Loading : UiState<Nothing>

    data class Success<T>(
        val data: T
    ) : UiState<T>

    data class Error(
        val message: String
    ) : UiState<Nothing>
}

Use this pattern when it makes sense for the specific feature. Do not create unnecessary abstractions for trivial screens.

---

# 8. Jetpack Compose Rules

Use Jetpack Compose instead of XML layouts.

Prefer:

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onEvent: (HomeEvent) -> Unit
)

Keep Composables as stateless as reasonably possible.

Prefer state hoisting.

Separate:

Screen
↓
UI components

For example:

HomeScreen
├── HomeTopBar
├── SearchBar
├── ProductList
└── ProductCard

Do not create unnecessary components for very small pieces of UI.

---

# 9. Navigation

Use Navigation Compose.

Navigation should be centralized and predictable.

Prefer route definitions instead of scattering route strings throughout the application.

For example:

navigation/
├── AppNavigation.kt
└── Screen.kt

---

# 10. Dependency Injection

Use Hilt for dependency injection.

Dependencies such as:

* Retrofit
* API services
* Repositories
* UseCases
* ViewModels

should be provided through dependency injection rather than manually constructing them throughout the application.

Avoid service locator patterns and global singleton objects unless there is a strong reason.

---

# 11. Project Structure

Start with a clean and understandable structure.

Recommended structure:

com.example.app/
│
├── core/
│   ├── network/
│   ├── common/
│   ├── navigation/
│   └── di/
│
├── data/
│   ├── remote/
│   │   ├── api/
│   │   └── datasource/
│   ├── local/
│   ├── dto/
│   ├── mapper/
│   └── repository/
│
├── domain/
│   ├── model/
│   ├── repository/
│   └── usecase/
│
└── presentation/
    ├── home/
    │   ├── HomeScreen.kt
    │   ├── HomeViewModel.kt
    │   ├── HomeUiState.kt
    │   └── components/
    │
    ├── login/
    │   ├── LoginScreen.kt
    │   ├── LoginViewModel.kt
    │   ├── LoginUiState.kt
    │   └── components/
    │
    └── navigation/

As the application grows, organize code by feature where appropriate.

Do not create folders or abstractions that are not currently needed.

---

# 12. Kotlin Coding Style

Use idiomatic modern Kotlin.

Prefer:

* `val` over `var` whenever possible
* immutable data
* data classes
* sealed interfaces/classes when appropriate
* extension functions
* null safety
* coroutines
* StateFlow
* suspend functions

Avoid:

* unnecessary mutable state
* `!!` unless absolutely necessary
* large God classes
* static/global mutable state
* duplicated code

Keep functions small and focused.

Use meaningful names.

---

# 13. Separation of Models

Maintain clear separation between:

DTO
Domain Model
UI Model

Do not automatically use one model everywhere.

For example:

UserResponseDto
      ↓
User
      ↓
UserUiModel

Only introduce separate UI models when the UI actually benefits from them. Avoid unnecessary duplication.

---

# 14. Testing

Architecture should remain testable.

Business logic should be testable independently from Android UI and network implementation.

Prioritize tests for:

* UseCases
* ViewModels
* Repository behavior
* important business rules

Do not write meaningless tests simply to increase test count.

---

# 15. Important Agent Rules

Before implementing a feature:

1. Understand the existing project structure.
2. Inspect existing architecture and conventions.
3. Reuse existing abstractions when appropriate.
4. Do not introduce a new library if the existing project already has a suitable solution.
5. Do not refactor unrelated code.
6. Do not change architecture without explaining why.
7. Keep changes focused on the requested feature.
8. Follow the dependency direction strictly.
9. Do not put API calls directly inside Composables.
10. Do not put business logic directly inside Composables.
11. Do not let Presentation depend directly on Data implementations.
12. Do not expose DTOs directly to the UI.
13. Do not create unnecessary layers or abstractions.

When there are multiple valid implementation choices, choose the simplest solution that preserves the architecture and maintainability.

---

# 16. Implementation Process

For every new feature, follow this general process:

1. Define Domain Model
        ↓
2. Define Repository Interface
        ↓
3. Create UseCase
        ↓
4. Create DTO / API models
        ↓
5. Create API service
        ↓
6. Implement Repository
        ↓
7. Create UiState
        ↓
8. Create ViewModel
        ↓
9. Create Compose Screen
        ↓
10. Connect Navigation
        ↓
11. Add Dependency Injection
        ↓
12. Test the feature

Do not blindly follow these steps if a feature does not require every component. Use engineering judgment.

---

# 17. Backend Integration

The Android application communicates with a Java Spring Boot backend through REST APIs.

Assume:

Android
   ↓
HTTP/HTTPS
   ↓
Spring Boot REST API

The Android application must treat the backend as an external data source.

Do not put Spring Boot-specific business logic inside the Android application.

Backend business rules remain on the backend.

Android should handle:

* UI
* UI state
* client-side validation
* API communication
* local state/cache
* navigation
* user interaction

Spring Boot should handle server-side:

* authentication/authorization
* business rules
* database operations
* server-side validation
* security
* persistence

---

# Final Principle

The most important rule is:

> Keep UI, business logic, and data access separated.

The desired architecture is:

                 Android App

┌─────────────────────────────────────┐
│          Presentation               │
│                                     │
│   Compose → ViewModel → UiState     │
└──────────────────┬──────────────────┘
                   ↓
┌─────────────────────────────────────┐
│             Domain                  │
│                                     │
│   UseCases → Repository Interfaces  │
│             Domain Models            │
└──────────────────┬──────────────────┘
                   ↓
┌─────────────────────────────────────┐
│              Data                   │
│                                     │
│   Repository → API → DTO → Mapper   │
└──────────────────┬──────────────────┘
                   ↓
             REST / HTTPS
                   ↓
          Java Spring Boot

Always prioritize **clear responsibility, one-way data flow, testability, maintainability, and simplicity** over blindly following patterns.
