package dominando.android.domain

import dominando.android.domain.data.DomainEntityFactory
import dominando.android.domain.interactor.ViewBookDetailsUseCase
import dominando.android.domain.repository.BooksRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ViewBookDetailsUseCaseTest {

    private val repository: BooksRepository = mockk()

    private val dummyBook = DomainEntityFactory.dummyBook()

    @Before
    fun init() {
        coEvery { repository.loadBook(any()) } returns flowOf(dummyBook)
    }

    @Test
    fun testBookDetailsIsLoaded() = runBlocking {
        // Given
        val useCase = ViewBookDetailsUseCase(repository)
        // When
        val book = useCase.execute("1").first()
        // Then
        assertEquals(book, dummyBook)
    }
}
