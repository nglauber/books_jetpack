package dominando.android.data_room.di

import dominando.android.data.source.RoomLocalData
import dominando.android.data_room.RoomLocalDataImpl
import dominando.android.data_room.database.AppDatabase
import dominando.android.data_room.filehelper.LocalFileHelper
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object DataRoomModules {

    fun load() {
        loadKoinModules(module {
            single<RoomLocalData> {
                RoomLocalDataImpl(AppDatabase.getDatabase(context = get()), LocalFileHelper())
            }
        })
    }
}