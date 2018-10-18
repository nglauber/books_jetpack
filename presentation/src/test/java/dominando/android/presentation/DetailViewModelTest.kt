package dominando.android.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.*
import dominando.android.data.model.Book
import dominando.android.domain.interactor.ViewBookDetailsUseCase
import dominando.android.presentation.binding.BookConverter
import dominando.android.presentation.data.DataFactory
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.Captor
import dominando.android.presentation.binding.Book as BookBinding

class DetailViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private var viewBookDetailsUseCase = mock<ViewBookDetailsUseCase>()
    private var bookDetailsViewModel = BookDetailsViewModel(viewBookDetailsUseCase)

    @Captor
    private val captor = argumentCaptor<((Book) -> Unit)>()

    @Captor
    private val captorError = argumentCaptor<((Throwable) -> Unit)>()

    @Test
    fun fetchBookDetailsExecutesUseCase() {
        val bookId = "1"
        bookDetailsViewModel.loadBook(bookId)
        verify(viewBookDetailsUseCase, times(1))
                .execute(eq(bookId), any(), any(), eq(null))
    }

    @Test
    fun fetchBookReturnsSuccess() {
        val book = DataFactory.dummyBook()
        val bookId = book.id
        bookDetailsViewModel.loadBook(bookId)

        verify(viewBookDetailsUseCase)
                .execute(eq(bookId), captor.capture(), any(), eq(null))
        captor.firstValue.invoke(book)

        assertEquals(
                ViewState.Status.SUCCESS,
                bookDetailsViewModel.getState().value?.status)
    }

    @Test
    fun fetchBookReturnsData() {
        val book = DataFactory.dummyBook()
        val bookId = book.id
        bookDetailsViewModel.loadBook(bookId)

        verify(viewBookDetailsUseCase)
                .execute(eq(bookId), captor.capture(), any(), eq(null))
        captor.firstValue.invoke(book)

        val bookBinding = BookConverter.fromData(book)
        assertEquals(
                bookBinding,
                bookDetailsViewModel.getState().value?.data
        )
    }

    @Test
    fun fetchSpeakersReturnsError() {
        val book = DataFactory.dummyBook()
        val bookId = book.id
        bookDetailsViewModel.loadBook(bookId)

        verify(viewBookDetailsUseCase)
                .execute(any(), captor.capture(), captorError.capture(), eq(null))
        captorError.firstValue.invoke(RuntimeException())

        assertEquals(
                ViewState.Status.ERROR,
                bookDetailsViewModel.getState().value?.status)
    }
}