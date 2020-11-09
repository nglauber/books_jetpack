package dominando.android.domain

import dominando.android.domain.data.DomainEntityFactory
import dominando.android.domain.interactor.RemoveBookUseCase
import dominando.android.domain.repository.BooksRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class RemoveBookUseCaseTest {

    private val repository: BooksRepository = mockk()

    private val dummyBook = DomainEntityFactory.dummyBook()

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
