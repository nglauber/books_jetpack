package dominando.android.domain.interactor

import dominando.android.data.BooksRepository
import dominando.android.data.model.Book

open class RemoveBookUseCase(
        private val repository: BooksRepository
) {
    suspend fun execute(params: Book) {
        repository.remove(params)
    }
}