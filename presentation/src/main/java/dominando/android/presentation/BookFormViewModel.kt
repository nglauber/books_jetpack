package dominando.android.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dominando.android.domain.interactor.SaveBookUseCase
import dominando.android.presentation.binding.BookConverter
import dominando.android.presentation.livedata.LiveEvent
import java.io.File
import dominando.android.presentation.binding.Book as BookBinding

class BookFormViewModel(private val useCase: SaveBookUseCase) : ViewModel() {
    var book: BookBinding? = null
    var tempImageFile: File? = null
        set(value) {
            deleteTempPhoto()
            field = value
            shouldDeleteImage = true
        }
    private var shouldDeleteImage: Boolean = true

    private val state: MutableLiveData<LiveEvent<ViewState<Unit>>> = MutableLiveData()

    fun getState(): LiveData<LiveEvent<ViewState<Unit>>> = state

    fun saveBook(book: BookBinding) {
        state.postValue(LiveEvent(ViewState(ViewState.Status.LOADING)))
        useCase.execute(BookConverter.toData(book),
                {
                    shouldDeleteImage = false
                    state.postValue(LiveEvent(ViewState(ViewState.Status.SUCCESS)))
                },
                { e ->
                    state.postValue(LiveEvent(ViewState(ViewState.Status.ERROR, error = e)))
                }
        )
    }

    private fun deleteTempPhoto() {
        if (shouldDeleteImage) {
            tempImageFile?.let {
                if (it.exists()) it.delete()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        deleteTempPhoto()
        useCase.dispose()
    }
}