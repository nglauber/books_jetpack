package dominando.android.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dominando.android.domain.interactor.ViewBookDetailsUseCase
import dominando.android.presentation.binding.Book as BookBinding
import dominando.android.presentation.binding.BookConverter
import java.lang.Exception
import java.lang.RuntimeException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class BookDetailsViewModel(
    private val bookId: String,
    private val viewBookDetailsUseCase: ViewBookDetailsUseCase
) : ViewModel() {

    private val state: MutableLiveData<ViewState<BookBinding>> = MutableLiveData()
    val book = Transformations.map(state) { it.data }

    init { loadBook() }

    fun getState(): LiveData<ViewState<BookBinding>> = state

    fun loadBook() {
        viewModelScope.launch {
            state.postValue(ViewState(ViewState.Status.LOADING))
            try {
                viewBookDetailsUseCase.execute(bookId)
                        .flowOn(Dispatchers.IO)
                        .collect { book ->
                            if (book != null) {
                                val bookBinding = BookConverter.fromData(book)
                                state.postValue(ViewState(ViewState.Status.SUCCESS, bookBinding))
                            } else {
                                state.postValue(
                                        ViewState(
                                                ViewState.Status.ERROR,
                                                error = RuntimeException("Book not found")
                                        )
                                )
                            }
                        }
            } catch (e: Exception) {
                state.postValue(ViewState(ViewState.Status.ERROR, error = e))
            }
        }
    }
}
