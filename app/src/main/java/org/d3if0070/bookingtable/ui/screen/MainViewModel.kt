package org.d3if0070.bookingtable.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.d3if0070.bookingtable.database.TableDao
import org.d3if0070.bookingtable.model.Table

class MainViewModel(dao: TableDao) : ViewModel() {

    val data : StateFlow<List<Table>> = dao.getTable().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )
}