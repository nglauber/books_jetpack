package dominando.android.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dominando.android.domain.entity.Book
import dominando.android.domain.interactor.ViewBookDetailsUseCase
import dominando.android.presentation.binding.BookConverter
import dominando.android.presentation.data.DataFactory
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class DetailViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private var viewBookDetailsUseCase = mockk<ViewBookDetailsUseCase>()

    @Test
    fun fetchBookDetailsExecutesUseCase() {
        val bookId = "1"
        BookDetailsViewModel(bookId, viewBookDetailsUseCase)
        verify(exactly = 1) { viewBookDetailsUseCase.execute(eq(bookId)) }
    }

    @Test
    fun fetchBookReturnsSuccess() {
        val book = DataFactory.dummyBook()
        initMock(book)
        val bookId = book.id
        val bookDetailsViewModel = BookDetailsViewModel(bookId, viewBookDetailsUseCase)

        verify(exactly = 1) { viewBookDetailsUseCase.execute(eq(bookId)) }

        assertEquals(
                ViewState.Status.SUCCESS,
                bookDetailsViewModel.getState().value?.status)
    }

    @Test
    fun fetchBookReturnsData() {
        val book = DataFactory.dummyBook()
        initMock(book)
        val bookId = book.id
        val bookDetailsViewModel = BookDetailsViewModel(bookId, viewBookDetailsUseCase)

        verify(exactly = 1) { viewBookDetailsUseCase.execute(eq(bookId)) }

        val bookBinding = BookConverter.fromData(book)
        assertEquals(
                bookBinding,
                bookDetailsViewModel.getState().value?.data
        )
    }

    @Test
    fun fetchBookReturnsError() {
        val book = DataFactory.dummyBook()
        val bookId = book.id
        val bookDetailsViewModel = BookDetailsViewModel(bookId, viewBookDetailsUseCase)

        verify(exactly = 1) { viewBookDetailsUseCase.execute(eq(bookId)) }

        assertEquals(
                ViewState.Status.ERROR,
                bookDetailsViewModel.getState().value?.status)
    }

    private fun initMock(
        book: Book = DataFactory.dummyBook()
    ) {
        coEvery { viewBookDetailsUseCase.execute(book.id) } returns flow { emit(book) }
    }
}
