package dominando.android.presentation

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dominando.android.domain.interactor.SaveBookUseCase
import dominando.android.presentation.binding.BookConverter
import dominando.android.presentation.livedata.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.Exception
import dominando.android.presentation.binding.Book as BookBinding

class BookFormViewModel(
        private val router: Router,
        private val useCase: SaveBookUseCase
) : ViewModel(), LifecycleObserver {

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
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    useCase.execute(BookConverter.toData(book))
                }
                shouldDeleteImage = false
                state.postValue(LiveEvent(ViewState(ViewState.Status.SUCCESS)))
                router.back()
            } catch (e: Exception) {
                state.postValue(LiveEvent(ViewState(ViewState.Status.ERROR, error = e)))
            }
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