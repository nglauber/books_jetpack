package dominando.android.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dominando.android.domain.entity.Book
import dominando.android.domain.interactor.ListBooksUseCase
import dominando.android.domain.interactor.RemoveBookUseCase
import dominando.android.presentation.binding.BookConverter
import dominando.android.presentation.data.DataFactory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ListBooksViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private var listBooksUseCase = mockk<ListBooksUseCase>()
    private var removeBookUseCase = mockk<RemoveBookUseCase>()
    private var bookListViewModel = BookListViewModel(listBooksUseCase, removeBookUseCase)

    @Before
    fun setUp() {
        initMocks()
    }

    @Test
    fun fetchBookListExecutesUseCase() = testCoroutineRule.runBlockingTest {
        initMocks()
        bookListViewModel.loadBooks()
        coVerify(exactly = 1) { listBooksUseCase.execute() }
    }

    @Test
    fun fetchBookReturnsSuccess() = testCoroutineRule.runBlockingTest {
        val books = DataFactory.dummyBookList()
        initMocks(books)
        bookListViewModel.loadBooks()

        coVerify(exactly = 1) { listBooksUseCase.execute() }

        val currentState = bookListViewModel.state().value
        assertEquals(
                ViewState.Status.SUCCESS,
                currentState?.status)
        assertEquals(
                books.map { BookConverter.fromData(it) },
                currentState?.data)
    }

    @Test
    fun fetchBookReturnsData() = testCoroutineRule.runBlockingTest {
        val bookList = DataFactory.dummyBookList()
        initMocks(bookList)

        bookListViewModel.loadBooks()

        coVerify(exactly = 1) { listBooksUseCase.execute() }

        assertEquals(
                bookList.map { BookConverter.fromData(it) },
                bookListViewModel.state().value?.data
        )
    }

    @Test
    fun deleteBookExecutesUseCase() = testCoroutineRule.runBlockingTest {
        val book = DataFactory.dummyBook()
        val bookBinding = BookConverter.fromData(book)
        bookListViewModel.remove(bookBinding)
        coVerify(exactly = 1) {
            removeBookUseCase.execute(eq(book))
        }
    }

    @Test
    fun deleteBookReturnsSuccess() = testCoroutineRule.runBlockingTest {
        val book = DataFactory.dummyBook()
        val bookBinding = BookConverter.fromData(book)

        bookListViewModel.remove(bookBinding)

        coVerify(exactly = 1) {
            removeBookUseCase.execute(eq(book))
        }

        assertEquals(
                ViewState.Status.SUCCESS,
                bookListViewModel.removeOperation().value?.status
        )
    }

    private fun initMocks(
        list: List<Book> = DataFactory.dummyBookList()
    ) {
        coEvery { listBooksUseCase.execute() } returns flow { emit(list) }
        coEvery { removeBookUseCase.execute(any()) } returns Unit
    }
}
