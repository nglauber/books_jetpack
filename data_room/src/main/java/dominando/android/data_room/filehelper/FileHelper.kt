package dominando.android.data_room.filehelper

import dominando.android.data.model.BookData

internal interface FileHelper {
    fun saveCover(book: BookData): Boolean
    fun deleteExistingCover(book: BookData): Boolean
}
