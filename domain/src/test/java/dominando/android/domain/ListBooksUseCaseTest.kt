package dominando.android.domain

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import dominando.android.data.BooksRepository
import dominando.android.domain.data.DataFactory
import dominando.android.domain.executor.PostExecutionThread
import dominando.android.domain.interactor.ListBooksUseCase
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test

class ListBooksUseCaseTest {
    private val postExecutionThread: PostExecutionThread = mock()

    private val repository: BooksRepository = mock()

    private val dummyBooksList = DataFactory.dummyBookList()

    @Before
    fun init() {
        whenever(repository.loadBooks())
                .thenReturn(
                        Flowable.just(dummyBooksList)
                )
    }

    @Test
    fun testBooksListIsLoaded() {
        // Given
        val useCase = ListBooksUseCase(repository, postExecutionThread)
        // When
        val testFlowable = useCase.buildUseCaseFlowable().test()
        // Then
        testFlowable.assertValue {
            it.size == dummyBooksList.size && it.containsAll(dummyBooksList)
        }
    }
}