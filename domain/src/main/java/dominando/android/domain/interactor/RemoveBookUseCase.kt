package dominando.android.domain.interactor

import dominando.android.domain.entity.Book
import dominando.android.domain.repository.BooksRepository

open class RemoveBookUseCase(private val repository: BooksRepository) {

    suspend fun execute(params: Book) {
        repository.remove(params)
    }
}
