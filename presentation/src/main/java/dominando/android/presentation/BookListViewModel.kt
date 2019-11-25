package dominando.android.presentation

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dominando.android.domain.interactor.ListBooksUseCase
import dominando.android.domain.interactor.RemoveBookUseCase
import dominando.android.presentation.binding.Book as BookBinding
import dominando.android.presentation.binding.BookConverter
import dominando.android.presentation.livedata.LiveEvent
import java.lang.Exception
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookListViewModel(
    private val loadBooksUseCase: ListBooksUseCase,
    private val removeBookUseCase: RemoveBookUseCase
) : ViewModel(), LifecycleObserver {

    private val state = MutableLiveData<ViewState<List<BookBinding>>>()
    private val removeOperation = MutableLiveData<LiveEvent<ViewState<Unit>>>()

    fun state(): LiveData<ViewState<List<BookBinding>>> = state
    fun removeOperation(): LiveData<LiveEvent<ViewState<Unit>>> = removeOperation

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
}
