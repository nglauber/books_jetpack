package dominando.android.domain.interactor

import dominando.android.data.BooksRepository
import dominando.android.data.model.Book
import dominando.android.domain.CompletableUseCase
import dominando.android.domain.executor.PostExecutionThread
import io.reactivex.Completable

open class RemoveBookUseCase(val repository: BooksRepository,
                        postExecutionThread: PostExecutionThread
) : CompletableUseCase<Book>(postExecutionThread) {
    override fun buildUseCaseCompletable(params: Book?): Completable {
        return if (params == null) {
            Completable.error(IllegalArgumentException("Book must not be null"))
        } else {
            repository.remove(params)
        }
    }
}