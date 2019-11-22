package dominando.android.presentation

import androidx.lifecycle.*
import dominando.android.domain.interactor.SaveBookUseCase
import dominando.android.presentation.binding.Book as BookBinding
import dominando.android.presentation.binding.BookConverter
import dominando.android.presentation.binding.MediaType
import dominando.android.presentation.binding.Publisher
import dominando.android.presentation.livedata.LiveEvent
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

    private val saveBookEvent: MutableLiveData<LiveEvent<ViewState<Unit>>> = MutableLiveData()
    private val book = MutableLiveData<BookBinding>(BookBinding())

    val state = Transformations.map(saveBookEvent) {
        /* Retrieve data before call consumeEvent() */
        val data = it.peekContent()
        if (it.peekContent()?.status == ViewState.Status.ERROR) {
            it.consumeEvent()
        }
        data
    }

    fun book(): LiveData<BookBinding> = book

    fun saveBook() {
        saveBookEvent.postValue(LiveEvent(ViewState(ViewState.Status.LOADING)))
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    book.value?.let { saveBookUseCase.execute(BookConverter.toData(it)) }
                }
                shouldDeleteImage = false
                saveBookEvent.postValue(LiveEvent(ViewState(ViewState.Status.SUCCESS)))
            } catch (e: Exception) {
                saveBookEvent.postValue(LiveEvent(ViewState(ViewState.Status.ERROR, error = e)))
            }
        }
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
