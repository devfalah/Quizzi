# **Quizzi**

**Quizzi** is a simple quiz app 🚀

<img src="https://firebasestorage.googleapis.com/v0/b/task-8566c.appspot.com/o/screens.png?alt=media&token=72b9dc85-c1eb-418d-a618-b9f96231c66f" width="1200"/>

# **Quizzi Video 👇**
- https://youtu.be/yR5LZqvGGjU
![](media/QuizZon_Head.png)
## Built With 🛠
- [Kotlin](https://kotlinlang.org/) - is a modern but already mature programming language aimed to make developers happier. It’s concise, safe, interoperable with Java and other languages, and provides many ways to reuse code between multiple platforms for productive programming.
- [RXJava](https://github.com/ReactiveX/RxJava) - is a Java VM implementation of Reactive Extensions: a library for composing asynchronous and event-based programs by using observable sequences.
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - Collection of libraries that help you design robust, testable, and maintainable apps.
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Stores UI-related data that isn't destroyed on UI changes.
  - [ViewBinding](https://developer.android.com/topic/libraries/view-binding) - Generates a binding class for each XML layout file present in that module and allows you to more easily write code that interacts with views.
- [Retrofit](https://square.github.io/retrofit/) - A type-safe HTTP client for Android and Java.
- [GSON Converter](https://github.com/square/retrofit/tree/master/retrofit-converters/gson) - A Converter which uses Moshi for serialization to and from JSON.
- [Material Components for Android](https://github.com/material-components/material-components-android) - Modular and customizable Material Design UI components for Android.

# Package Structure

    com.devfalah.Quizzi    # Root Package
    .
    ├── data               
    |   ├── model          
    |   ├── repository          
    │   ├── service          
    │
    |
    ├── ui                  
    |   ├── base        
    │   ├── dialogs       
    │   ├── home        
    |   ├── mcq      
    |   ├── result     
    |
    └── utils               # Utility Classes / Kotlin extensions


## Architecture
This app uses [***MVVM (Model View View-Model)***](https://developer.android.com/jetpack/docs/guide#recommended-app-arch) architecture.

![](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)


<br>
