package dominando.android.domain

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import dominando.android.data.BooksRepository
import dominando.android.domain.data.DataFactory
import dominando.android.domain.executor.PostExecutionThread
import dominando.android.domain.interactor.RemoveBookUseCase
import io.reactivex.Completable
import org.junit.Before
import org.junit.Test

class RemoveBookUseCaseTest {
    private val postExecutionThread: PostExecutionThread = mock()

    private val repository: BooksRepository = mock()

    private val dummyBook = DataFactory.dummyBook()

    @Before
    fun init() {
        whenever(repository.remove(any()))
                .thenReturn(
                        Completable.complete()
                )
    }

    @Test
    fun testBookDetailsIsRemoved() {
        // Given
        val useCase = RemoveBookUseCase(repository, postExecutionThread)
        // When
        val test = useCase.buildUseCaseCompletable(dummyBook).test()
        // Then
        test.assertComplete()
    }
}