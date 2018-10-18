package dominando.android.presentation

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModel
import dominando.android.data_fb.FbRepository
import dominando.android.data_room.database.AppDatabase
import dominando.android.data_room.LocalFileHelper
import dominando.android.data_room.RoomRepository
import dominando.android.domain.interactor.ListBooksUseCase
import dominando.android.domain.interactor.RemoveBookUseCase
import dominando.android.domain.interactor.SaveBookUseCase
import dominando.android.domain.interactor.ViewBookDetailsUseCase
import dominando.android.presentation.executor.UiThread

class BookVmFactory(private val application: Application) : ViewModelProvider.NewInstanceFactory() {
    // TODO replace this class by DI with Koin
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Remote repository with Firebase
//        val repo = FbRepository()
        // Local repository with Room
        val repo = RoomRepository(AppDatabase.getDatabase(application), LocalFileHelper())
        return when {
            modelClass.isAssignableFrom(BookListViewModel::class.java) ->
                BookListViewModel(ListBooksUseCase(repo, UiThread()), RemoveBookUseCase(repo, UiThread())) as T
            modelClass.isAssignableFrom(BookDetailsViewModel::class.java) ->
                BookDetailsViewModel(ViewBookDetailsUseCase(repo, UiThread())) as T
            modelClass.isAssignableFrom(BookFormViewModel::class.java) ->
                BookFormViewModel(SaveBookUseCase(repo, UiThread())) as T
            else -> throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}