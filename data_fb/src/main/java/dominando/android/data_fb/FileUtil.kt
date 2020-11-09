package dominando.android.data_fb

import dominando.android.data.model.BookData
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

internal object FileUtil {

    fun pathFromBook(book: BookData): String {
        return "/data/data/dominando.android.livros/files/${book.id}.jpg"
    }
    @Throws(IOException::class)
    fun copyFile(src: File, dst: File) {
        val input = FileInputStream(src)
        val os = FileOutputStream(dst)
        val buff = ByteArray(1024)
        var len: Int
        len = input.read(buff)
        while (len > 0) {
            os.write(buff, 0, len)
            len = input.read(buff)
        }
        input.close()
        os.close()
    }
}
