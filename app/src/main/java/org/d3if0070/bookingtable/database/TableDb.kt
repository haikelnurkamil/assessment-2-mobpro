package org.d3if0070.bookingtable.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.d3if0070.bookingtable.model.Table

@Database(entities = [Table::class], version = 1, exportSchema = false)
abstract class TableDb : RoomDatabase() {

    abstract val dao: TableDao

    companion object {

        @Volatile
        private var INSTANCE: TableDb? = null

        fun getInstance(context: Context): TableDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TableDb::class.java,
                        "table.db"
                    ).build()
                    INSTANCE  = instance
                }
                return instance
            }
        }
    }
}