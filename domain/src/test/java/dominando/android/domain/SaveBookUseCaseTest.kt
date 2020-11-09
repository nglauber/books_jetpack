package dominando.android.domain

import dominando.android.domain.data.DomainEntityFactory
import dominando.android.domain.interactor.SaveBookUseCase
import dominando.android.domain.repository.BooksRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class SaveBookUseCaseTest {

    private val repository: BooksRepository = mockk()

    private val dummyBook = DomainEntityFactory.dummyBook()

    @Before
    fun init() {
        coEvery { repository.saveBook(any()) } returns Unit
    }

    @Test
    fun testBookIsSaved() = runBlocking {
        // Given
        val useCase = SaveBookUseCase(repository)
        // When
        useCase.execute(dummyBook)
        // Then
        // No exceptions
    }
}
