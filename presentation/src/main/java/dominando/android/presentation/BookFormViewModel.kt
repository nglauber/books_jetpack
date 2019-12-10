package dominando.android.presentation

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dominando.android.domain.interactor.SaveBookUseCase
import dominando.android.presentation.binding.Book as BookBinding
import dominando.android.presentation.binding.BookConverter
import dominando.android.presentation.binding.MediaType
import dominando.android.presentation.binding.Publisher
import dominando.android.presentation.livedata.SingleLiveEvent
import java.io.File
import java.lang.Exception
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookFormViewModel(
    private val saveBookUseCase: SaveBookUseCase
) : ViewModel(), LifecycleObserver {

    var tempImageFile: File? = null
        set(value) {
            deleteTempPhoto()
            field = value
            shouldDeleteImage = true
        }

    val publishers = listOf(
            Publisher("1", "Novatec"),
            Publisher("2", "Outra")
    )

    private var shouldDeleteImage: Boolean = true

    private val saveBookEvent = SingleLiveEvent<ViewState<Unit>>()
    private val book = MutableLiveData<BookBinding>(BookBinding())

    fun state(): LiveData<ViewState<Unit>> = saveBookEvent
    fun book(): LiveData<BookBinding> = book

    fun saveBook() {
        book.value?.let {
            saveBookEvent.postValue(ViewState(ViewState.Status.LOADING))
            viewModelScope.launch {
                try {
                    withContext(Dispatchers.IO) {
                        saveBookUseCase.execute(BookConverter.toData(it))
                    }
                    shouldDeleteImage = false
                    saveBookEvent.postValue(ViewState(ViewState.Status.SUCCESS))
                } catch (e: Exception) {
                    saveBookEvent.postValue(ViewState(ViewState.Status.ERROR, error = e))
                }
            }
        } ?: saveBookEvent.postValue(ViewState(ViewState.Status.ERROR,
                error = IllegalArgumentException("Book is null")))
    }

    /**
     * This method should be called to edit book
     */
    fun setBook(book: BookBinding) {
        this.book.value = book
    }

    fun onMediaTypeChanged(mediaType: MediaType, isChecked: Boolean) {
        if (isChecked) {
            book.value?.mediaType = mediaType
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun deleteTempPhoto() {
        if (shouldDeleteImage) {
            tempImageFile?.let {
                if (it.exists())
                    viewModelScope.launch(Dispatchers.IO) {
                        it.delete()
                    }
            }
        }
    }
}
