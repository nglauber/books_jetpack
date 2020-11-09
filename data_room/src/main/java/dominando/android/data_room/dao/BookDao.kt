package dominando.android.data_room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dominando.android.data_room.entity.Book
import kotlinx.coroutines.flow.Flow

@Dao
internal interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(book: Book)

    @Delete
    suspend fun delete(vararg book: Book)

    @Query("SELECT * FROM Book WHERE title LIKE :title ORDER BY title")
    fun bookByTitle(title: String = "%"): Flow<List<Book>>

    @Query("SELECT * FROM Book WHERE id = :id")
    fun bookById(id: String): Flow<Book>
}
