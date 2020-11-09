# Books Jetpack
This sample demonstrates how to use some of the Android Architecture Components available in  [Jetpack library](https://developer.android.com/jetpack/).

In this sample I tried to follow the [Clean Architecture](https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html) principles and use some cool Android libraries.

The Application is divided in modules as displayed below:

<img src="./docs/imgs/app_layers.png" width="600"/>

* __UI__ - contains all UI related classes (Activities, Fragments, Adapters, etc.). <br/>
The authentication process is also implemented here (I need to improve that) using [Firebase Authentication](https://firebase.google.com/docs/auth).<br/>
This layer is using Jetpack's [Navigation API](https://developer.android.com/topic/libraries/architecture/navigation.html) to implement the "single activity" approach for the navigation flow.
* __Presentation__ - once this project is folowing (or trying to) the [MVVM pattern](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel), this layer is using [View Model](https://developer.android.com/topic/libraries/architecture/viewmodel) library to keep data between config changes.<br/>
To expose observable data from the View Models, this module is using [LiveData](https://developer.android.com/topic/libraries/architecture/livedata).<br/>
This layer is using [Lifecycle library](https://developer.android.com/topic/libraries/architecture/lifecycle) in order to tie the View Model with UI lifecycle (in some cases).<br/>
Finally, this module is also using [Data Binding](https://developer.android.com/topic/libraries/data-binding/) to keep the data in sync during user input.
* __Domain__ - in this module are declared the application's use cases.
* __Data__ - declares the basic operations that must be provided by the application's repository and the basic data classes used as [DTO](https://en.wikipedia.org/wiki/Data_transfer_object).
* __Data Remote__ - contains a implementation of a remote data source using [Firebase Cloud Firestore](https://firebase.google.com/docs/firestore). The book's cover are stored in the [Firebase Cloud Storage](https://firebase.google.com/docs/storage).
* __Data Local__ - contains a implementation of a local data source [Room library](https://developer.android.com/topic/libraries/architecture/room). The book's cover are stored in the local file system.

The architecture flow is displayed below:<p/>
<img src="./docs/imgs/app_arch.png" width="600"/><br/>
(*Source: [Lessons learnt using Coroutines Flow](https://medium.com/androiddevelopers/lessons-learnt-using-coroutines-flow-4a6b285c0d06) article by [Manuel Vivo](https://twitter.com/manuelvicnt)*)

## Screenshots
### Books List
This screen displays all books saved on the repository (local or remote).

<img src="./docs/imgs/screen_book_list.png" width="200"/>

### Book details
This screen display the details of a book selected in books list.

<img src="./docs/imgs/screen_book_details.png" width="200"/>

### Book Form
In this screen, the user can add a new book or edit an existing one.

<img src="./docs/imgs/screen_book_form.png" width="200"/>

### Login Screen
The user must perform the login with their Google account to access the application.

<img src="./docs/imgs/screen_login.png" width="200"/>

## Libraries
This project is written in Kotlin and it's using the following libraries:
* AppCompat;
* ConstraintLayout;
* Material Design Components Library;
* [Jetpack Libraries](https://developer.android.com/jetpack/) (Navigation, Data Binding, View Model, Lifecycle, LiveData and Room);
* [Firebase Libraries for Android](https://firebase.google.com/docs/android/setup) (Authentication, Firestore and Storage);
* [Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
* [Picasso](http://square.github.io/picasso/)
* [Koin](https://github.com/InsertKoinIO/koin)
* JUnit
* [Mockito](http://site.mockito.org/) and [MockK](https://github.com/mockk/mockk)

## Get started
The project is compatible with Android Studio 3.5.2.
To run this application, you must have to [create a Firebase Project](https://firebase.google.com/docs/android/setup) and [enable Google Authentication](https://firebase.google.com/docs/auth/android/google-signin).
Afterwards, you must have to download the `google_services.json` file from the firebase console and add it `app` module root folder.
If you want to save data on Firebase Cloud Firestore, [enable this database](https://firebase.google.com/docs/firestore/quickstart) for your project in Firebase Console. Also [enable the Firebase Cloud Storage](https://firebase.google.com/docs/storage/android/start).

To choose the data source you will save application data, just make the following change in the [BooksRepositoryImpl.kt](./data/src/main/java/dominando/android/data/repository/BooksRepositoryImpl.kt) file.

```kotlin
internal class BooksRepositoryImpl(
   // Change RoomLocalData to FBData interface
   private val localData: RoomLocalData,
   private val entityMapper: Mapper<BookData, Book>,
   private val dataMapper: Mapper<Book, BookData>
) : BooksRepository {

    ...
}
```

And that's it! You're good to go.


## Contribute ;)

This project is just a demo, but any feedback and/or PR's are appreciated! :)