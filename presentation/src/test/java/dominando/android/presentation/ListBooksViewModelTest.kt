package dominando.android.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.*
import dominando.android.data.model.Book
import dominando.android.domain.interactor.ListBooksUseCase
import dominando.android.domain.interactor.RemoveBookUseCase
import dominando.android.presentation.binding.BookConverter
import dominando.android.presentation.data.DataFactory
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.Captor
import dominando.android.presentation.binding.Book as BookBinding

class ListBooksViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private var listBooksUseCase = mock<ListBooksUseCase>()
    private var removeBookUseCase = mock<RemoveBookUseCase>()
    private var bookListViewModel = BookListViewModel(listBooksUseCase, removeBookUseCase)

    @Captor
    private val captor = argumentCaptor<((List<Book>) -> Unit)>()

    @Captor
    private val captorRemove = argumentCaptor<(() -> Unit)>()

    @Test
    fun fetchBookListExecutesUseCase() {
        bookListViewModel.loadBooks()
        verify(listBooksUseCase, times(1))
                .execute(eq(null), any(), any(), eq(null))
    }

    @Test
    fun fetchBookReturnsSuccess() {
        val book = DataFactory.dummyBookList()
        bookListViewModel.loadBooks()

        verify(listBooksUseCase)
                .execute(eq(null), captor.capture(), any(), eq(null))
        captor.firstValue.invoke(book)

        assertEquals(
                ViewState.Status.SUCCESS,
                bookListViewModel.getState().value?.status)
    }

    @Test
    fun fetchBookReturnsData() {
        val books = DataFactory.dummyBookList()
        bookListViewModel.loadBooks()

        verify(listBooksUseCase)
                .execute(eq(null), captor.capture(), any(), eq(null))
        captor.firstValue.invoke(books)

        val bookBindingList = books.map { BookConverter.fromData(it) }
        Assert.assertEquals(
                bookBindingList,
                bookListViewModel.getState().value?.data
        )
    }

    @Test
    fun deleteBookExecutesUseCase() {
        val book = DataFactory.dummyBook()
        val bookBinding = BookConverter.fromData(book)
        bookListViewModel.remove(bookBinding)
        verify(removeBookUseCase, times(1))
                .execute(eq(book), any(), any())
    }

    @Test
    fun deleteBookReturnsSuccess() {
        val book = DataFactory.dummyBook()
        val bookBinding = BookConverter.fromData(book)

        bookListViewModel.remove(bookBinding)

        verify(removeBookUseCase, times(1))
                .execute(eq(book), captorRemove.capture(), any())
        captorRemove.firstValue.invoke()

        assertEquals(
                ViewState.Status.SUCCESS,
                bookListViewModel.removeOperation().value?.status
        )
    }
}