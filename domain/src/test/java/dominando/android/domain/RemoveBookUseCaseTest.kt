package dominando.android.domain

import dominando.android.data.BooksRepository
import dominando.android.domain.data.DataFactory
import dominando.android.domain.interactor.RemoveBookUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class RemoveBookUseCaseTest {

    private val repository: BooksRepository = mockk()

    private val dummyBook = DataFactory.dummyBook()

    @Before
    fun init() {
        coEvery { repository.remove(any()) } returns Unit
    }

    @Test
    fun testBookDetailsIsRemoved() = runBlocking {
        // Given
        val useCase = RemoveBookUseCase(repository)
        // When
        useCase.execute(dummyBook)
        // Then
        // no exceptions
    }
}