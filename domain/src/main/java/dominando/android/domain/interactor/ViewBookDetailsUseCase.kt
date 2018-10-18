package dominando.android.domain.interactor

import dominando.android.data.BooksRepository
import dominando.android.data.model.Book
import dominando.android.domain.FlowableUseCase
import dominando.android.domain.executor.PostExecutionThread
import io.reactivex.Flowable
import java.lang.IllegalArgumentException

open class ViewBookDetailsUseCase(private val repository: BooksRepository,
                       postExecutionThread: PostExecutionThread
) : FlowableUseCase<Book, String>(postExecutionThread) {
    override fun buildUseCaseFlowable(params: String?): Flowable<Book> {
        if (params != null) {
            return repository.loadBook(params)
        } else {
            throw IllegalArgumentException("You must pass a book id")
        }
    }
}