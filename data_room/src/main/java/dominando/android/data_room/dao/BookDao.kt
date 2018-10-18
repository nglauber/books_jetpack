package dominando.android.data_room.dao

import androidx.room.*
import dominando.android.data_room.entity.Book
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(book: Book): Completable

    @Delete
    fun delete(vararg book: Book): Completable

    @Query("SELECT * FROM Book WHERE title LIKE :title ORDER BY title")
    fun bookByTitle(title: String = "%"): Flowable<List<Book>>

    @Query("SELECT * FROM Book WHERE id = :id")
    fun bookById(id: String): Flowable<Book>
}