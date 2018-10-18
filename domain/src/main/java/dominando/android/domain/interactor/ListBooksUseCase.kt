package dominando.android.domain.interactor

import dominando.android.data.BooksRepository
import dominando.android.data.model.Book
import dominando.android.domain.FlowableUseCase
import dominando.android.domain.executor.PostExecutionThread
import io.reactivex.Flowable

open class ListBooksUseCase(
        private val repository: BooksRepository,
        postExecutionThread: PostExecutionThread
) : FlowableUseCase<List<Book>, Unit>(postExecutionThread) {
    override fun buildUseCaseFlowable(params: Unit?): Flowable<List<Book>> {
        return repository.loadBooks()
    }
}