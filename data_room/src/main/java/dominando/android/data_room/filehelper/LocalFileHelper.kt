package dominando.android.data_room.filehelper

import android.util.Log
import dominando.android.data.model.BookData
import java.io.File

internal class LocalFileHelper : FileHelper {

    override fun saveCover(book: BookData): Boolean {
        Log.d("NGVL", "saveCover: ${book.coverUrl}")
        return true
    }

    override fun deleteExistingCover(book: BookData): Boolean {
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
