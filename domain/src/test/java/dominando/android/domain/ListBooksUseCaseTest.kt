package dominando.android.domain

import dominando.android.data.BooksRepository
import dominando.android.domain.data.DataFactory
import dominando.android.domain.interactor.ListBooksUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ListBooksUseCaseTest {
    private val repository: BooksRepository = mockk()

    private val dummyBooksList = DataFactory.dummyBookList()

    @Before
    fun init() {
        coEvery { repository.loadBooks() } returns flowOf(dummyBooksList)
    }

    @Test
    fun testBooksListIsLoaded() = runBlocking {
        // Given
        val useCase = ListBooksUseCase(repository)
        // When
        val list = useCase.execute().first()
        // Then
        assertEquals(list.size, dummyBooksList.size)
        assert(list.containsAll(dummyBooksList))
    }
}
