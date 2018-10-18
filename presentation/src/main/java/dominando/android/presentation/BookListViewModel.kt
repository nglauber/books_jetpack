package dominando.android.presentation

import androidx.lifecycle.*
import dominando.android.domain.interactor.ListBooksUseCase
import dominando.android.domain.interactor.RemoveBookUseCase
import dominando.android.presentation.binding.BookConverter
import dominando.android.presentation.livedata.LiveEvent
import dominando.android.presentation.binding.Book as BookBinding

class BookListViewModel(
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
            state.postValue(ViewState(ViewState.Status.LOADING))
            loadBooksUseCase.execute(null,
                    { books ->
                        val booksBinding = books.map { book -> BookConverter.fromData(book) }
                        state.postValue(ViewState(ViewState.Status.SUCCESS, booksBinding))
                    },
                    { e ->
                        state.postValue(ViewState(ViewState.Status.ERROR, error = e))
                    }
            )
        }
    }

    fun remove(bookBinding: BookBinding) {
        val book = BookConverter.toData(bookBinding)
        removeBookUseCase.execute(book,
                {
                    removeOperation.postValue(LiveEvent(ViewState(ViewState.Status.SUCCESS)))
                },
                {
                    removeOperation.postValue(LiveEvent(ViewState(ViewState.Status.ERROR, error = it)))
                }
        )
    }

    override fun onCleared() {
        super.onCleared()
        loadBooksUseCase.dispose()
        removeBookUseCase.dispose()
    }
}
