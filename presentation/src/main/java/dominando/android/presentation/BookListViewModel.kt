package dominando.android.presentation

import androidx.lifecycle.*
import dominando.android.domain.interactor.ListBooksUseCase
import dominando.android.domain.interactor.RemoveBookUseCase
import dominando.android.presentation.binding.BookConverter
import dominando.android.presentation.livedata.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import dominando.android.presentation.binding.Book as BookBinding

class BookListViewModel(
        private val router: Router,
        private val loadBooksUseCase: ListBooksUseCase,
        private val removeBookUseCase: RemoveBookUseCase

) : ViewModel(), LifecycleObserver {

    private val state: MutableLiveData<ViewState<List<BookBinding>>> = MutableLiveData()
    private val removeOperation: MutableLiveData<LiveEvent<ViewState<Unit>>> = MutableLiveData()

    fun getState(): LiveData<ViewState<List<BookBinding>>> {
        return state
    }

    fun removeOperation(): LiveData<LiveEvent<ViewState<Unit>>> {
        return removeOperation
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun loadBooks() {
        if (state.value == null) {
            viewModelScope.launch {
                state.postValue(ViewState(ViewState.Status.LOADING))
                try {
                    loadBooksUseCase.execute()
                            .flowOn(Dispatchers.IO)
                            .collect { books ->
                                val booksBinding = books.map { book -> BookConverter.fromData(book) }
                                state.postValue(ViewState(ViewState.Status.SUCCESS, booksBinding))
                            }
                } catch (e: Exception) {
                    state.postValue(ViewState(ViewState.Status.ERROR, error = e))
                }
            }
        }
    }

    fun remove(bookBinding: BookBinding) {
        val book = BookConverter.toData(bookBinding)
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    removeBookUseCase.execute(book)
                }
                removeOperation.postValue(LiveEvent(ViewState(ViewState.Status.SUCCESS)))
            } catch (e: Exception) {
                removeOperation.postValue(LiveEvent(ViewState(ViewState.Status.ERROR, error = e)))
            }
        }
    }

    fun showBookDetails(book: BookBinding) {
        router.showBookDetails(book)
    }

    fun showBookForm() {
        router.showBookForm(null)
    }
}
