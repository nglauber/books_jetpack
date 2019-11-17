package dominando.android.presentation

import androidx.lifecycle.*
import dominando.android.domain.interactor.ViewBookDetailsUseCase
import dominando.android.presentation.binding.BookConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.RuntimeException
import dominando.android.presentation.binding.Book as BookBinding

class BookDetailsViewModel(
        private val route: Router,
        private val useCase: ViewBookDetailsUseCase
) : ViewModel() {

    private val state: MutableLiveData<ViewState<BookBinding>> = MutableLiveData()

    fun getState(): LiveData<ViewState<BookBinding>> = state

    fun loadBook(id: String) {
        if (id != state.value?.data?.id) {
            viewModelScope.launch {
                state.postValue(ViewState(ViewState.Status.LOADING))
                try {
                    useCase.execute(id)
                            .flowOn(Dispatchers.IO)
                            .collect { book ->
                                if (book != null) {
                                    val bookBinding = BookConverter.fromData(book)
                                    state.postValue(
                                            ViewState(ViewState.Status.SUCCESS, bookBinding)
                                    )
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

    fun editBook(book: BookBinding) {
        route.showBookForm(book)
    }
}
