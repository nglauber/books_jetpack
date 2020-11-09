package dominando.android.data_room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dominando.android.data_room.dao.BookDao
import dominando.android.data_room.entity.Book

@Database(entities = [Book::class], version = 1, exportSchema = false)
internal abstract class AppDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao

    companion object {
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "booksDb")
                        .build()
            }
            return instance as AppDatabase
        }
        fun destroyInstance() {
            instance = null
        }
    }
}
