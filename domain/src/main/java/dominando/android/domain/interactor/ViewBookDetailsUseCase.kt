package dominando.android.domain.interactor

import dominando.android.data.BooksRepository
import dominando.android.data.model.Book
import dominando.android.domain.FlowableUseCase
import dominando.android.domain.executor.PostExecutionThread
import io.reactivex.Flowable

open class ViewBookDetailsUseCase(private val repository: BooksRepository,
                       postExecutionThread: PostExecutionThread
) : FlowableUseCase<Book, String>(postExecutionThread) {
    override fun buildUseCaseFlowable(params: String?): Flowable<Book> {
        require(params != null) { "You must pass a book id" }
        return repository.loadBook(params)
    }
}