package org.d3if0070.bookingtable.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.d3if0070.bookingtable.model.Table

@Dao
interface TableDao {

    @Insert
    suspend fun insert(table: Table)

    @Update
    suspend fun  update(table: Table)

    @Query("SELECT * FROM `table` ORDER BY nama DESC")
    fun getTable(): Flow<List<Table>>

    @Query("SELECT * FROM `table` WHERE id = :id")
    suspend fun getTableById(id: Long):Table?

    @Query("DELETE FROM `table` WHERE id = :id")
    suspend fun deleteById(id:Long)

}