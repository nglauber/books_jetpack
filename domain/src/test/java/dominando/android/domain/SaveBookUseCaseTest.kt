package dominando.android.domain

import dominando.android.data.BooksRepository
import dominando.android.domain.data.DataFactory
import dominando.android.domain.interactor.SaveBookUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class SaveBookUseCaseTest {

    private val repository: BooksRepository = mockk()

    private val dummyBook = DataFactory.dummyBook()

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