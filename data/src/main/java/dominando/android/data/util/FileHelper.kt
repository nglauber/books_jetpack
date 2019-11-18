package dominando.android.data.util

import dominando.android.data.model.Book

interface FileHelper {
    fun saveCover(book: Book): Boolean
    fun deleteExistingCover(book: Book): Boolean
}
