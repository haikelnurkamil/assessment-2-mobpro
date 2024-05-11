package org.d3if0070.bookingtable.ui.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if0070.bookingtable.R
import org.d3if0070.bookingtable.database.TableDb
import org.d3if0070.bookingtable.ui.theme.BookingTableTheme
import org.d3if0070.bookingtable.util.ViewModelFactory

const val KEY_ID_TABLE = "idTable"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController,id: Long?= null) {
    val context = LocalContext.current
    val db = TableDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var nama by remember { mutableStateOf("") }
    var nim by remember { mutableStateOf("") }

    var showDialog by remember {
        mutableStateOf(false)
    }

    val radioOptions = listOf(
        stringResource(id = R.string.A01),
        stringResource(id = R.string.A02),
        stringResource(id = R.string.A03),
        stringResource(id = R.string.A04),
        stringResource(id = R.string.A05)
    )

    var selectedKelas by rememberSaveable { mutableStateOf(radioOptions[0]) }

    LaunchedEffect(true) {
        if (id == null) return@LaunchedEffect
        val data = viewModel.getTable(id) ?: return@LaunchedEffect
        nama = data.nama
        nim = data.nim
        selectedKelas = data.kelas
    }
    Scaffold (
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.kembali),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    if (id == null)
                        Text(text = stringResource(id = R.string.tambah_data))
                    else
                        Text(text = stringResource(id = R.string.edit_data))

                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = {
                        if (nama =="" || nim == "" ){
                            Toast.makeText(context,R.string.invalid, Toast.LENGTH_LONG).show()
                            return@IconButton
                        }
                        if (id == null){
                            viewModel.insert(nama,nim, selectedKelas)
                        } else{
                            viewModel.update(id,nama,nim, selectedKelas)
                        }
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = stringResource(id = R.string.simpan),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (id != null){
                        DeleteAction {
                            showDialog = true
                        }
                        DisplayAlertDialog(
                            openDialog = showDialog,
                            onDismissRequest = { showDialog = false}) {
                            showDialog = false
                            viewModel.delete(id)
                            navController.popBackStack()
                        }
                    }

                }
            )
        }
    ) { padding ->
        FormTable(
            title = nama,
            onTitleChange = {nama = it} ,
            desc = nim,
            onDescChange = {nim = it},
            pilihanKelas = selectedKelas,
            kelasBerubah = {selectedKelas = it},
            radioOpsi = radioOptions,
            modifier = Modifier.padding(padding)
        )
//        ScreenContent(Modifier.padding(padding))
    }
}

@Composable
fun DeleteAction(delete:()->Unit ){
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = {expanded = true}) {
        Icon(
            imageVector = Icons.Filled.MoreVert ,
            contentDescription = stringResource(R.string.opsi_lainnya),
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(
            expanded = expanded ,
            onDismissRequest = { expanded = false}
        ) {
            DropdownMenuItem(text = { Text(text = stringResource(R.string.hapus))},
                onClick = {
                    expanded = false
                    delete()
                }
            )
        }
    }
}

@Composable
fun FormTable(

    title:String,onTitleChange:(String)-> Unit,
    desc:String,onDescChange:(String)->Unit,
    pilihanKelas: String,kelasBerubah:(String)->Unit,
    radioOpsi:List<String>,
    modifier: Modifier
){
    Column(
        modifier= modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        //nama pemesan
        OutlinedTextField(
            value = title,
            onValueChange = {onTitleChange(it)},
            singleLine = true,
            label = { Text(text = stringResource(id = R.string.nama))},
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier=Modifier.fillMaxWidth()
        )
        //nomer telfon pemesan
        OutlinedTextField(
            value = desc,
            onValueChange = {onDescChange(it)},
            singleLine = true,
            label = { Text(text = stringResource(id = R.string.nim))},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier=Modifier.fillMaxWidth()
        )
        //RadioOption Meja yang di pesan
        Column(
            modifier = Modifier
                .padding(top = 6.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
        ){
            radioOpsi.forEach { text -> TableOption(
                label = text, isSelected = pilihanKelas == text, modifier = Modifier
                    .selectable(
                        selected = pilihanKelas == text,
                        onClick = { kelasBerubah(text) },
                        role = Role.RadioButton
                    )

                    .padding(16.dp)
                    .fillMaxWidth()
            )
            }
        }
    }
}


@Composable
fun TableOption (label:String, isSelected: Boolean, modifier: Modifier ){
    Row (
        modifier = modifier,

        ){
        RadioButton(selected = isSelected, onClick = null)
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}


@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DetailScreenPreview(){
    BookingTableTheme {
        DetailScreen(rememberNavController())
    }
}