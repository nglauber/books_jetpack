package dominando.android.domain

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import dominando.android.data.BooksRepository
import dominando.android.domain.data.DataFactory
import dominando.android.domain.executor.PostExecutionThread
import dominando.android.domain.interactor.ViewBookDetailsUseCase
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

class ViewBookDetailsUseCaseTest {
    private val postExecutionThread: PostExecutionThread = mock()

    private val repository: BooksRepository = mock()

    private val dummyBook = DataFactory.dummyBook()

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        whenever(repository.loadBook(any()))
                .thenReturn(
                        Flowable.just(dummyBook)
                )
    }

    @Test
    fun testBookDetailsIsLoaded() {
        // Given
        val useCase = ViewBookDetailsUseCase(repository, postExecutionThread)
        // When
        val test = useCase.buildUseCaseFlowable("1").test()
        // Then
        test.assertValue(dummyBook)
    }
}