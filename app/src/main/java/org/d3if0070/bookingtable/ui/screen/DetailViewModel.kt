package org.d3if0070.bookingtable.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if0070.bookingtable.database.TableDao
import org.d3if0070.bookingtable.model.Table

class DetailViewModel(private val dao: TableDao) : ViewModel() {
    fun insert(nama:String, nim:String, kelas: String){
        val table = Table (
            nama = nama,
            nim = nim,
            kelas = kelas
        )
        viewModelScope.launch (Dispatchers.IO){
            dao.insert(table)
        }
    }

    suspend fun getTable(id:Long): Table?{
        return dao.getTableById(id)
    }

    fun  update(id: Long, nama: String,nim: String,kelas: String){
        val table = Table(
            id = id,
            nama = nama,
            nim = nim,
            kelas = kelas
        )
        viewModelScope.launch (Dispatchers.IO){
            dao.update(table)
        }

    }

    fun delete(id: Long){
        viewModelScope.launch(Dispatchers.IO){
            dao.deleteById(id)
        }
    }
}