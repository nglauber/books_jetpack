package dominando.android.presentation

import androidx.lifecycle.*
import dominando.android.domain.interactor.ViewBookDetailsUseCase
import dominando.android.presentation.binding.BookConverter
import dominando.android.presentation.binding.Book as BookBinding

class BookDetailsViewModel(private val useCase: ViewBookDetailsUseCase) : ViewModel() {

    private val state: MutableLiveData<ViewState<BookBinding>> = MutableLiveData()

    fun getState(): LiveData<ViewState<BookBinding>> {
        return state
    }

    fun loadBook(id: String) {
        if (id != state.value?.data?.id) {
            state.postValue(ViewState(ViewState.Status.LOADING))
            useCase.execute(id,
                    { book ->
                        val bookBinding = BookConverter.fromData(book)
                        state.postValue(ViewState(ViewState.Status.SUCCESS, bookBinding))
                    },
                    { e ->
                        state.postValue(ViewState(ViewState.Status.ERROR, error = e))
                    }
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        useCase.dispose()
    }
}
