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

class BookVmFactory(
        private val application: Application,
        private val router: Router
) : ViewModelProvider.NewInstanceFactory() {

    // TODO replace this class by DI with Koin
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Remote repository with Firebase
//        val repo = FbRepository()
        // Local repository with Room
        val repo = RoomRepository(AppDatabase.getDatabase(application), LocalFileHelper())
        return when {
            modelClass.isAssignableFrom(BookListViewModel::class.java) ->
                BookListViewModel(router, ListBooksUseCase(repo), RemoveBookUseCase(repo)) as T
            modelClass.isAssignableFrom(BookDetailsViewModel::class.java) ->
                BookDetailsViewModel(router, ViewBookDetailsUseCase(repo)) as T
            modelClass.isAssignableFrom(BookFormViewModel::class.java) ->
                BookFormViewModel(router, SaveBookUseCase(repo)) as T
            else -> throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}