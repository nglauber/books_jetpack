package dominando.android.data_room

import android.util.Log
import dominando.android.data.model.Book
import dominando.android.data.util.FileHelper
import java.io.File

class LocalFileHelper : FileHelper {
    override fun saveCover(book: Book): Boolean {
        Log.d("NGVL", "saveCover: ${book.coverUrl}")
        return true
    }

    override fun deleteExistingCover(book: Book): Boolean {
        return if (book.coverUrl.startsWith("file:")) {
            val coverFile = File(book.coverUrl.replace("file://", ""))
            return if (coverFile.exists()) {
                coverFile.delete()
            } else {
                true
            }
        } else {
            true
        }
    }
}
