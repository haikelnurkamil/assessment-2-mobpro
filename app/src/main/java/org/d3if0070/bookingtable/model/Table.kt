package org.d3if0070.bookingtable.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table")
data class Table(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nama: String,
    val nim: String,
    val kelas: String
)